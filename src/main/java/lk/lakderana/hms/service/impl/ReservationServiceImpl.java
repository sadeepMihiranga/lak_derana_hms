package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.config.EntityValidator;
import lk.lakderana.hms.dto.*;
import lk.lakderana.hms.entity.TMsReservation;
import lk.lakderana.hms.entity.TRfInquiry;
import lk.lakderana.hms.exception.DataNotFoundException;
import lk.lakderana.hms.exception.NoRequiredInfoException;
import lk.lakderana.hms.exception.OperationException;
import lk.lakderana.hms.exception.TransactionConflictException;
import lk.lakderana.hms.mapper.ReservationMapper;
import lk.lakderana.hms.repository.*;
import lk.lakderana.hms.service.*;
import lk.lakderana.hms.util.constant.CommonReferenceCodes;
import lk.lakderana.hms.util.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Strings;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static lk.lakderana.hms.util.constant.CommonReferenceCodes.*;
import static lk.lakderana.hms.util.constant.status.ReservationStatus.*;

@Slf4j
@Service
public class ReservationServiceImpl extends EntityValidator implements ReservationService {

    private final InquiryService inquiryService;
    private final RoomReservationService roomReservationService;
    private final FacilityReservationService facilityReservationService;
    private final ItemReservationService itemReservationService;
    private final PaymentService paymentService;
    private final InvoiceService invoiceService;
    private final PartyService partyService;

    private final ReservationRepository reservationRepository;
    private final InquiryRepository inquiryRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  InquiryService inquiryService,
                                  RoomReservationService roomReservationService,
                                  FacilityReservationService facilityReservationService,
                                  ItemReservationService itemReservationService,
                                  @Lazy PaymentService paymentService,
                                  @Lazy InvoiceService invoiceService,
                                  PartyService partyService, 
                                  InquiryRepository inquiryRepository) {
        this.reservationRepository = reservationRepository;
        this.inquiryService = inquiryService;
        this.roomReservationService = roomReservationService;
        this.facilityReservationService = facilityReservationService;
        this.itemReservationService = itemReservationService;
        this.paymentService = paymentService;
        this.invoiceService = invoiceService;
        this.partyService = partyService;
        this.inquiryRepository = inquiryRepository;
    }

    @Override
    public PaginatedEntity reservationPaginatedSearch(Short status, Integer noOfPersons, Integer page, Integer size) {

        validatePaginateIndexes(page, size);

        PaginatedEntity paginatedReservationList = null;
        List<ReservationDTO> reservationList = null;

        validatePaginateIndexes(page, size);

        Page<TMsReservation> tMsReservationPage = reservationRepository
                .searchReservations(noOfPersons, status, captureBranchIds(), PageRequest.of(page - 1, size));

        if (tMsReservationPage.getSize() == 0)
            return null;

        paginatedReservationList = new PaginatedEntity();
        reservationList = new ArrayList<>();

        for (TMsReservation tMsReservation : tMsReservationPage) {

            ReservationDTO reservationDTO = ReservationMapper.INSTANCE.entityToDTO(tMsReservation);

            reservationList.add(reservationDTO);

            collectCustomerInfo(tMsReservation, reservationDTO);
            collectReservationReferenceData(reservationDTO);
            collectPaymentDetails(reservationDTO);
        }

        paginatedReservationList.setTotalNoOfPages(tMsReservationPage.getTotalPages());
        paginatedReservationList.setTotalNoOfRecords(tMsReservationPage.getTotalElements());
        paginatedReservationList.setEntities(reservationList);

        return paginatedReservationList;
    }

    @Transactional
    @Override
    public ReservationDTO createReservation(ReservationDTO reservationDTO) {

        TMsReservation tMsReservation = null;

        if(reservationDTO.getInquiryId() != null) {
            final TRfInquiry tRfInquiry = inquiryRepository
                    .findByInqrIdAndBranch_BrnhIdIn(reservationDTO.getInquiryId(), captureBranchIds());

            if(tRfInquiry == null)
                throw new DataNotFoundException("An Inquiry not found for the id " + reservationDTO.getInquiryId());

            reservationDTO.setBranchId(captureBranchIds().get(0));

            tMsReservation = ReservationMapper.INSTANCE.dtoToEntity(reservationDTO);
            tMsReservation.setInquiry(tRfInquiry);

        } else {
            InquiryDTO inquiryDTO = new InquiryDTO();
            inquiryDTO.setCustomerName(reservationDTO.getCustomerName());
            inquiryDTO.setCustomerContactNo(reservationDTO.getCustomerContactNo());
            inquiryDTO.setRemarks(reservationDTO.getRemarks());

            final InquiryDTO inquiry = inquiryService.createInquiry(inquiryDTO);

            reservationDTO.setBranchId(captureBranchIds().get(0));
            reservationDTO.setInquiryId(inquiry.getInquiryId());

            tMsReservation = ReservationMapper.INSTANCE.dtoToEntity(reservationDTO);
        }
        tMsReservation.setResvStatus(CONFIRMED.getShortValue());

        /** update inquiry to reserved */
        inquiryService.reserveInquiryById(reservationDTO.getInquiryId());

        final ReservationDTO createdReservation = ReservationMapper.INSTANCE.entityToDTO(persistEntity(tMsReservation));

        reserveRelatedReservationData(reservationDTO, createdReservation.getReservationId());

        reservationDTO.setReservationId(createdReservation.getReservationId());
        handlePayment(reservationDTO);

        return createdReservation;
    }

    @Transactional
    @Override
    public Long cancelReservation(Long reservationId, ReservationDTO reservationDTO) {

        TMsReservation tMsReservation = validateReservationById(reservationId);
        tMsReservation.setResvStatus(CANCELLED.getShortValue());
        tMsReservation.setResvCancellationReasons(reservationDTO.getCancellationReasons());

        removeRelatedReservationData(reservationId);

        final BigDecimal dueAmount = paymentService.calculateDueAmountForAReservation(reservationId, true);

        if(dueAmount.compareTo(BigDecimal.ZERO) != 0)
            throw new OperationException("Settle Dues before canceling the Reservation. Due Amount : " + dueAmount);

        return persistEntity(tMsReservation).getResvId();
    }

    @Transactional
    @Override
    public ReservationDTO updateReservation(Long reservationId, ReservationDTO reservationDTO) {

        TMsReservation tMsReservation = validateReservationById(reservationId);

        tMsReservation.setResvCheckInDateTime(reservationDTO.getCheckInDateTime());
        tMsReservation.setResvCheckOutDateTime(reservationDTO.getCheckOutDateTime());
        tMsReservation.setResvNoOfPersons(reservationDTO.getNoOfPersons());
        tMsReservation.setResvRemarks(reservationDTO.getRemarks());

        final ReservationDTO updatedReservation = ReservationMapper.INSTANCE.entityToDTO(persistEntity(tMsReservation));

        removeRelatedReservationData(reservationId);
        reserveRelatedReservationData(reservationDTO, reservationId);
        collectReservationReferenceData(updatedReservation);

        return updatedReservation;
    }

    @Override
    public ReservationDTO getReservationById(Long reservationId) {

        final TMsReservation tMsReservation = validateReservationById(reservationId);
        
        ReservationDTO reservationDTO = ReservationMapper.INSTANCE.entityToDTO(tMsReservation);

        collectCustomerInfo(tMsReservation, reservationDTO);
        collectReservationReferenceData(reservationDTO);
        collectPaymentDetails(reservationDTO);

        return reservationDTO;
    }

    @Override
    public BigDecimal calculateTotalReservationAmount(Long reservationId) {

        BigDecimal totalReservationAmount = BigDecimal.ZERO;

        validateReservationById(reservationId);

        final BigDecimal roomReservationAmount = roomReservationService.calculateRoomReservationAmount(reservationId);
        final BigDecimal facilityReservationAmount = facilityReservationService.calculateFacilityReservationAmount(reservationId);
        final BigDecimal itemReservationAmount = itemReservationService.calculateItemReservationAmount(reservationId);

        totalReservationAmount = roomReservationAmount
                .add(roomReservationAmount)
                .add(facilityReservationAmount)
                .add(itemReservationAmount);

        return totalReservationAmount;
    }

    @Override
    public Boolean releaseReservation(Long reservationId) {

        TMsReservation tMsReservation = validateReservationById(reservationId);
        tMsReservation.setResvStatus(RELEASED.getShortValue());

        final BigDecimal dueAmount = paymentService.calculateDueAmountForAReservation(reservationId, true);

        if(dueAmount.compareTo(BigDecimal.ZERO) != 0)
            throw new OperationException("Please Settle Dues before releasing the Reservation. Due Amount : " + dueAmount);

        persistEntity(tMsReservation);
        return true;
    }

    private void collectCustomerInfo(TMsReservation tMsReservation, ReservationDTO reservationDTO) {
        if(!Strings.isNullOrEmpty(tMsReservation.getInquiry().getInqrCustomerCode())) {
            final PartyDTO partyDTO = partyService.getPartyByPartyCode(tMsReservation.getInquiry().getInqrCustomerCode());
            reservationDTO.setCustomerName(partyDTO.getName());

            List<String> contacts = partyDTO.getContactList().stream()
                    .filter(partyContactDTO -> partyContactDTO.getContactType().equals(PARTY_CONTACT_MOBILE.getValue()))
                    .map(PartyContactDTO::getContactNumber)
                    .collect(Collectors.toList());

            if(!contacts.isEmpty())
                reservationDTO.setCustomerContactNo(contacts.get(0));
        } else {
            reservationDTO.setCustomerName(tMsReservation.getInquiry().getInqrCustomerName());
            reservationDTO.setCustomerContactNo(tMsReservation.getInquiry().getInqrCustomerContactNo());
        }
    }

    private void handlePayment(ReservationDTO reservationDTO) {
        if(reservationDTO.getAdvancePayment().compareTo(BigDecimal.ZERO) != 0)
        /** create advance payment */
            paymentService.createPayment(
                    reservationDTO.getReservationId(),
                    new PaymentDTO(reservationDTO.getPaymentType(), reservationDTO.getAdvancePayment()), true);
    }

    private void collectPaymentDetails(ReservationDTO reservationDTO) {
        reservationDTO.setTotalAmount(calculateTotalReservationAmount(reservationDTO.getReservationId()));
        reservationDTO.setPaidAmount(paymentService.calculatePayedAmountForAReservation(reservationDTO.getReservationId(), true));
        reservationDTO.setDueAmount(paymentService.calculateDueAmountForAReservation(reservationDTO.getReservationId(), true));
    }

    private void reserveRelatedReservationData(ReservationDTO reservationDTO, Long reservationId) {
        reservationDTO.getRoomReservationList().forEach(roomReservationDTO -> {
            roomReservationService.reserveRoom(reservationId, roomReservationDTO.getRoom());
        });

        reservationDTO.getFacilityReservationList().forEach(facilityReservationDTO -> {
            facilityReservationService.reserveFacility(reservationId, facilityReservationDTO.getFacility());
        });

        reservationDTO.getItemReservationList().forEach(itemReservationDTO -> {
            itemReservationService.reserveItem(reservationId, itemReservationDTO.getItem());
        });
    }

    private void removeRelatedReservationData(Long reservationId) {
        final Boolean isRoomReservationCanceled = roomReservationService.cancelRoomReservationByReservation(reservationId);
        final Boolean isFacilityReservationCanceled = facilityReservationService.cancelFacilityReservationByReservation(reservationId);
        final Boolean isItemsReservationCanceled = itemReservationService.cancelItemReservationsByReservation(reservationId);

        if(!isRoomReservationCanceled || !isFacilityReservationCanceled || !isItemsReservationCanceled)
            throw new OperationException("Error while canceling Room, Facility or Item Reservation");
    }

    private void collectReservationReferenceData(ReservationDTO reservationDTO) {
        reservationDTO.setRoomReservationList(
                roomReservationService.getRoomReservationsByReservation(reservationDTO.getReservationId()));
        reservationDTO.setFacilityReservationList(
                facilityReservationService.getFacilityReservationsByReservation(reservationDTO.getReservationId()));
        reservationDTO.setItemReservationList(
                itemReservationService.getItemReservationsByReservation(reservationDTO.getReservationId()));
    }

    private TMsReservation validateReservationById(Long reservationId) {

        if(reservationId == null)
            throw new NoRequiredInfoException("Reservation Id is required " + reservationId);

        final TMsReservation tMsReservation = reservationRepository
                .findByResvIdAndBranch_BrnhIdIn(reservationId, captureBranchIds());

        if(tMsReservation == null)
            throw new DataNotFoundException("Reservation not found for the Id " + reservationId);

        return tMsReservation;
    }

    private TMsReservation persistEntity(TMsReservation tMsReservation) {
        try {
            validateEntity(tMsReservation);
            return reservationRepository.save(tMsReservation);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new TransactionConflictException("Transaction Updated by Another User.");
        } catch (Exception e) {
            log.error("Error while persisting : " + e.getMessage());
            throw new OperationException(e.getMessage());
        }
    }
}
