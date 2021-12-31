package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.config.EntityValidator;
import lk.lakderana.hms.dto.InquiryDTO;
import lk.lakderana.hms.dto.PaginatedEntity;
import lk.lakderana.hms.dto.ReservationDTO;
import lk.lakderana.hms.entity.TMsReservation;
import lk.lakderana.hms.entity.TRfInquiry;
import lk.lakderana.hms.exception.DataNotFoundException;
import lk.lakderana.hms.exception.NoRequiredInfoException;
import lk.lakderana.hms.exception.OperationException;
import lk.lakderana.hms.exception.TransactionConflictException;
import lk.lakderana.hms.mapper.ReservationMapper;
import lk.lakderana.hms.repository.*;
import lk.lakderana.hms.service.FacilityReservationService;
import lk.lakderana.hms.service.InquiryService;
import lk.lakderana.hms.service.ReservationService;
import lk.lakderana.hms.service.RoomReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static lk.lakderana.hms.util.constant.status.ReservationStatus.*;

@Slf4j
@Service
public class ReservationServiceImpl extends EntityValidator implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final InquiryService inquiryService;
    private final RoomReservationService roomReservationService;
    private final FacilityReservationService facilityReservationService;

    private final InquiryRepository inquiryRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  InquiryService inquiryService,
                                  RoomReservationService roomReservationService,
                                  FacilityReservationService facilityReservationService,
                                  InquiryRepository inquiryRepository) {
        this.reservationRepository = reservationRepository;
        this.inquiryService = inquiryService;
        this.roomReservationService = roomReservationService;
        this.facilityReservationService = facilityReservationService;
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

        final ReservationDTO createdReservation = ReservationMapper.INSTANCE.entityToDTO(persistEntity(tMsReservation));

        reservationDTO.getRoomList().forEach(roomDTO -> {
            roomReservationService.reserveRoom(createdReservation.getReservationId(), roomDTO);
        });

        reservationDTO.getFacilityList().forEach(facilityDTO -> {
            facilityReservationService.reserveFacility(createdReservation.getReservationId(), facilityDTO);
        });

        return createdReservation;
    }

    @Transactional
    @Override
    public Long cancelReservation(Long reservationId, ReservationDTO reservationDTO) {

        TMsReservation tMsReservation = validateReservationById(reservationId);
        tMsReservation.setResvStatus(CANCELLED.getShortValue());
        tMsReservation.setResvCancellationReasons(reservationDTO.getCancellationReasons());

        final Boolean isRoomReservationCanceled = roomReservationService.cancelRoomReservationByReservation(reservationId);

        final Boolean isFacilityReservationCanceled = facilityReservationService.cancelFacilityReservationByReservation(reservationId);

        if(!isRoomReservationCanceled || !isFacilityReservationCanceled)
            throw new OperationException("Error while canceling Room or Facility Reservation");

        //TODO : need to check pending payments

        return persistEntity(tMsReservation).getResvId();
    }

    @Override
    public ReservationDTO updateReservation(Long reservationId, ReservationDTO reservationDTO) {

        TMsReservation tMsReservation = validateReservationById(reservationId);

        return null;
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
