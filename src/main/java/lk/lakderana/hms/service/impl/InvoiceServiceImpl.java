package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.config.EntityValidator;
import lk.lakderana.hms.dto.InvoiceDTO;
import lk.lakderana.hms.dto.InvoiceDetDTO;
import lk.lakderana.hms.dto.ReservationDTO;
import lk.lakderana.hms.entity.TMsReservation;
import lk.lakderana.hms.entity.TRfBranch;
import lk.lakderana.hms.entity.TTrInvoice;
import lk.lakderana.hms.entity.TTrInvoiceDet;
import lk.lakderana.hms.exception.DataNotFoundException;
import lk.lakderana.hms.exception.NoRequiredInfoException;
import lk.lakderana.hms.exception.OperationException;
import lk.lakderana.hms.exception.TransactionConflictException;
import lk.lakderana.hms.mapper.InvoiceDetMapper;
import lk.lakderana.hms.mapper.InvoiceMapper;
import lk.lakderana.hms.repository.*;
import lk.lakderana.hms.service.InvoiceService;
import lk.lakderana.hms.service.PaymentService;
import lk.lakderana.hms.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Strings;
import org.springframework.context.annotation.Lazy;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static lk.lakderana.hms.util.constant.Constants.STATUS_ACTIVE;

@Slf4j
@Service
public class InvoiceServiceImpl extends EntityValidator implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceDetRepository invoiceDetRepository;
    private final ReservationRepository reservationRepository;
    private final NumberGeneratorRepository numberGeneratorRepository;
    private final BranchRepository branchRepository;
    private final RoomReservationRepository roomReservationRepository;
    private final FacilityReservationRepository facilityReservationRepository;
    private final ItemReservationRepository itemReservationRepository;

    private final PaymentService paymentService;
    private final ReservationService reservationService;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository,
                              InvoiceDetRepository invoiceDetRepository,
                              ReservationRepository reservationRepository,
                              NumberGeneratorRepository numberGeneratorRepository,
                              BranchRepository branchRepository,
                              RoomReservationRepository roomReservationRepository,
                              FacilityReservationRepository facilityReservationRepository,
                              ItemReservationRepository itemReservationRepository,
                              PaymentService paymentService,
                              @Lazy ReservationService reservationService) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceDetRepository = invoiceDetRepository;
        this.reservationRepository = reservationRepository;
        this.numberGeneratorRepository = numberGeneratorRepository;
        this.branchRepository = branchRepository;
        this.roomReservationRepository = roomReservationRepository;
        this.facilityReservationRepository = facilityReservationRepository;
        this.itemReservationRepository = itemReservationRepository;
        this.paymentService = paymentService;
        this.reservationService = reservationService;
    }

    @Transactional
    @Override
    public InvoiceDTO createInvoice(Long reservationId, InvoiceDTO invoiceDTO) {

        validateEntity(invoiceDTO);
        final TMsReservation tMsReservation = validateReservationById(reservationId);

        final BigDecimal dueAmount = paymentService.calculateDueAmountForAReservation(reservationId, true);

        if(dueAmount.compareTo(BigDecimal.ZERO) != 0)
            throw new OperationException("Settle Dues before creating the Invoice. Due Amount : " + dueAmount);

        final String invoiceNumber = numberGeneratorRepository.generateNumber("IV", "Y", "#", "#",
                "#", "#", "#", "#");

        if(Strings.isNullOrEmpty(invoiceNumber))
            throw new OperationException("Invoice Number not generated successfully");

        invoiceDTO.setInvoiceNumber(invoiceNumber);
        invoiceDTO.setStatus(STATUS_ACTIVE.getShortValue());
        invoiceDTO.setBranchId(captureBranchIds().get(0));
        invoiceDTO.setGrossAmount(reservationService.calculateTotalReservationAmount(reservationId));
        invoiceDTO.setReservationId(reservationId);

        calculateInvoiceAmounts(invoiceDTO);

        final TTrInvoice createdInvoice = persistEntity(InvoiceMapper.INSTANCE.dtoToEntity(invoiceDTO));

        insertToInvoiceDet(reservationId, createdInvoice);

        return InvoiceMapper.INSTANCE.entityToDTO(createdInvoice);
    }

    @Override
    public InvoiceDTO getInvoiceDataByReservation(Long reservationId) {
        return null;
    }

    private void insertToInvoiceDet(Long reservationId, TTrInvoice createdInvoice) {

        final ReservationDTO reservationDTO = reservationService.getReservationById(reservationId);

        final TMsReservation tMsReservation = reservationRepository.findByResvIdAndBranch_BrnhIdIn(reservationId, captureBranchIds());

        List<TTrInvoiceDet> tTrInvoiceDetList = new ArrayList<>();

        final TRfBranch tRfBranch = branchRepository.findByBrnhIdAndBrnhStatus(captureBranchIds().get(0), STATUS_ACTIVE.getShortValue());

        reservationDTO.getRoomReservationList().forEach(roomReservationDTO -> {

            TTrInvoiceDet tTrInvoiceDet = new TTrInvoiceDet();

            tTrInvoiceDet.setInvoice(createdInvoice);
            tTrInvoiceDet.setBranch(tRfBranch);
            tTrInvoiceDet.setIndtStatus(STATUS_ACTIVE.getShortValue());
            tTrInvoiceDet.setReservation(tMsReservation);
            tTrInvoiceDet.setRoomReservation(roomReservationRepository.getById(roomReservationDTO.getRoomReservationId()));
            tTrInvoiceDet.setIndtUnitPrice(roomReservationDTO.getRoom().getRoomPrice());
            tTrInvoiceDet.setIndtReservedQuantity(1);
            tTrInvoiceDet.setIndtAmount(roomReservationDTO.getRoom().getRoomPrice());

            tTrInvoiceDetList.add(tTrInvoiceDet);

        });

        reservationDTO.getFacilityReservationList().forEach(facilityReservationDTO -> {

            TTrInvoiceDet tTrInvoiceDet = new TTrInvoiceDet();

            tTrInvoiceDet.setInvoice(createdInvoice);
            tTrInvoiceDet.setBranch(tRfBranch);
            tTrInvoiceDet.setIndtStatus(STATUS_ACTIVE.getShortValue());
            tTrInvoiceDet.setReservation(tMsReservation);
            tTrInvoiceDet.setFacilityReservation(facilityReservationRepository.getById(facilityReservationDTO.getFacilityReservationId()));
            tTrInvoiceDet.setIndtUnitPrice(facilityReservationDTO.getFacility().getPrice());
            tTrInvoiceDet.setIndtReservedQuantity(facilityReservationDTO.getQuantity().intValue());
            tTrInvoiceDet.setIndtAmount(facilityReservationDTO.getFacility().getPrice().multiply(facilityReservationDTO.getQuantity()));

            tTrInvoiceDetList.add(tTrInvoiceDet);
        });

        reservationDTO.getItemReservationList().forEach(itemReservationDTO -> {

            TTrInvoiceDet tTrInvoiceDet = new TTrInvoiceDet();

            tTrInvoiceDet.setInvoice(createdInvoice);
            tTrInvoiceDet.setBranch(tRfBranch);
            tTrInvoiceDet.setIndtStatus(STATUS_ACTIVE.getShortValue());
            tTrInvoiceDet.setReservation(tMsReservation);
            tTrInvoiceDet.setItemReservation(itemReservationRepository.getById(itemReservationDTO.getItemReservationId()));
            tTrInvoiceDet.setIndtUnitPrice(itemReservationDTO.getItem().getPrice());
            tTrInvoiceDet.setIndtReservedQuantity(itemReservationDTO.getQuantity().intValue());
            tTrInvoiceDet.setIndtAmount(itemReservationDTO.getItem().getPrice().multiply(itemReservationDTO.getQuantity()));

            tTrInvoiceDetList.add(tTrInvoiceDet);
        });

        invoiceDetRepository.saveAll(tTrInvoiceDetList);
    }

    private void calculateInvoiceAmounts(InvoiceDTO invoiceDTO) {

        final BigDecimal HUNDRAD = BigDecimal.valueOf(100.00);
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal taxAmount = BigDecimal.ZERO;
        BigDecimal netAmount = BigDecimal.ZERO;

        if(invoiceDTO.getGrossAmount() == null)
            throw new OperationException("Gross Amount is Required");

        if(invoiceDTO.getTaxPercentage() != null && invoiceDTO.getTaxAmount() == null)
            taxAmount = invoiceDTO.getGrossAmount().multiply(invoiceDTO.getTaxPercentage()).divide(HUNDRAD, 4, RoundingMode.HALF_UP);
        else if(invoiceDTO.getTaxPercentage() == null && invoiceDTO.getTaxAmount() != null)
            taxAmount = invoiceDTO.getTaxAmount();

        if(invoiceDTO.getDiscountPercentage() != null && invoiceDTO.getDiscountAmount() == null)
            discountAmount = invoiceDTO.getGrossAmount().multiply(invoiceDTO.getDiscountPercentage()).divide(HUNDRAD, 4, RoundingMode.HALF_UP);
        else if(invoiceDTO.getDiscountPercentage() == null && invoiceDTO.getDiscountAmount() != null)
            discountAmount = invoiceDTO.getDiscountAmount();

        netAmount = invoiceDTO.getGrossAmount().add(taxAmount).subtract(discountAmount);

        invoiceDTO.setTaxAmount(taxAmount);
        invoiceDTO.setDiscountAmount(discountAmount);
        invoiceDTO.setNetAmount(netAmount);
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

    private TTrInvoice persistEntity(TTrInvoice tTrInvoice) {
        try {
            validateEntity(tTrInvoice);
            return invoiceRepository.save(tTrInvoice);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new TransactionConflictException("Transaction Updated by Another User.");
        } catch (Exception e) {
            log.error("Error while persisting : " + e.getMessage());
            throw new OperationException(e.getMessage());
        }
    }
}
