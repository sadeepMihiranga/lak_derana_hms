package lk.lakderana.hms.service.impl;

import com.google.common.base.Strings;
import lk.lakderana.hms.config.EntityValidator;
import lk.lakderana.hms.dto.CommonReferenceDTO;
import lk.lakderana.hms.dto.PaginatedEntity;
import lk.lakderana.hms.dto.PaymentDTO;
import lk.lakderana.hms.entity.TMsReservation;
import lk.lakderana.hms.entity.TTrPayment;
import lk.lakderana.hms.exception.DataNotFoundException;
import lk.lakderana.hms.exception.NoRequiredInfoException;
import lk.lakderana.hms.exception.OperationException;
import lk.lakderana.hms.exception.TransactionConflictException;
import lk.lakderana.hms.mapper.PaymentMapper;
import lk.lakderana.hms.repository.PaymentRepository;
import lk.lakderana.hms.repository.ReservationRepository;
import lk.lakderana.hms.service.CommonReferenceService;
import lk.lakderana.hms.service.PaymentService;
import lk.lakderana.hms.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static lk.lakderana.hms.util.constant.CommonReferenceTypeCodes.PAYMENT_TYPES;
import static lk.lakderana.hms.util.constant.Constants.STATUS_ACTIVE;
import static lk.lakderana.hms.util.constant.Constants.STATUS_INACTIVE;

@Slf4j
@Service
public class PaymentServiceImpl extends EntityValidator implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    private final ReservationService reservationService;
    private final CommonReferenceService commonReferenceService;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              ReservationRepository reservationRepository,
                              @Lazy ReservationService reservationService,
                              CommonReferenceService commonReferenceService) {
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
        this.reservationService = reservationService;
        this.commonReferenceService = commonReferenceService;
    }

    @Override
    public PaginatedEntity paymentPaginatedSearch(String paymentMethod, Long reservationId, Short status, Integer page, Integer size) {

        PaginatedEntity paginatedPaymentList = null;
        List<PaymentDTO> paymentList = null;

        validatePaginateIndexes(page, size);

        final Page<TTrPayment> tTrPaymentPage = paymentRepository
                .searchPayments(paymentMethod, reservationId, captureBranchIds(), status, PageRequest.of(page - 1, size));

        if (tTrPaymentPage.getSize() == 0)
            return null;

        paginatedPaymentList = new PaginatedEntity();
        paymentList = new ArrayList<>();

        for (TTrPayment tTrPayment : tTrPaymentPage) {

            PaymentDTO paymentDTO = PaymentMapper.INSTANCE.entityToDTO(tTrPayment);
            setReferenceData(paymentDTO);

            paymentList.add(paymentDTO);
        }

        paginatedPaymentList.setTotalNoOfPages(tTrPaymentPage.getTotalPages());
        paginatedPaymentList.setTotalNoOfRecords(tTrPaymentPage.getTotalElements());
        paginatedPaymentList.setEntities(paymentList);

        return paginatedPaymentList;
    }

    @Override
    public PaymentDTO createPayment(Long reservationId, PaymentDTO paymentDTO, Boolean isReservationValidated) {

        validateEntity(paymentDTO);
        validateReferenceData(paymentDTO);

        if(!isReservationValidated)
            validateReservation(reservationId);

        if(paymentDTO.getAmount().compareTo(BigDecimal.ZERO) == 0)
            throw new OperationException("Payment Amount is not valid");

        final BigDecimal dueAmount = calculateDueAmountForAReservation(reservationId, true);

        if(paymentDTO.getAmount().compareTo(dueAmount) > 0)
            throw new OperationException("Paying more than need. Excess amount : " + paymentDTO.getAmount().subtract(dueAmount));

        paymentDTO.setReservationId(reservationId);
        paymentDTO.setBranchId(captureBranchIds().get(0));
        paymentDTO.setStatus(STATUS_ACTIVE.getShortValue());

        final TTrPayment tTrPayment = PaymentMapper.INSTANCE.dtoToEntity(paymentDTO);

        return PaymentMapper.INSTANCE.entityToDTO(persistEntity(tTrPayment));
    }

    @Override
    public BigDecimal calculateDueAmountForAReservation(Long reservationId, Boolean isReservationValidated) {

        BigDecimal dueAmount = BigDecimal.ZERO;

        if(!isReservationValidated)
            validateReservation(reservationId);

        final BigDecimal totalReservationAmount = reservationService.calculateTotalReservationAmount(reservationId);

        final BigDecimal payedAmountForAReservation = calculatePayedAmountForAReservation(reservationId, true);

        dueAmount = totalReservationAmount.subtract(payedAmountForAReservation);

        return dueAmount;
    }

    @Override
    public BigDecimal calculatePayedAmountForAReservation(Long reservationId, Boolean isReservationValidated) {

        BigDecimal totalPayedAmount = BigDecimal.ZERO;

        if(!isReservationValidated)
            validateReservation(reservationId);

        final List<TTrPayment> tTrPaymentList = paymentRepository
                .findAllByReservation_ResvIdAndPaytStatusAndBranch_BrnhIdIn(reservationId, STATUS_ACTIVE.getShortValue(), captureBranchIds());

        totalPayedAmount = tTrPaymentList.stream().map(TTrPayment::getPaytAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalPayedAmount;
    }

    @Override
    public Boolean cancelPayment(Long paymentId, PaymentDTO paymentDTO) {

        if(Strings.isNullOrEmpty(paymentDTO.getCancelReason()))
            throw new NoRequiredInfoException("Payment Cancel Reason is required");

        final TTrPayment tTrPayment = validatePaymentById(paymentId);

        tTrPayment.setPaytStatus(STATUS_INACTIVE.getShortValue());
        tTrPayment.setPaytCancelReason(paymentDTO.getCancelReason());

        persistEntity(tTrPayment);

        return true;
    }

    private TTrPayment validatePaymentById(Long paymentId) {

        if(paymentId == null)
            throw new NoRequiredInfoException("Payment Id is required " + paymentId);

        final TTrPayment tTrPayment = paymentRepository
                .findByPaytIdAndPaytStatusAndBranch_BrnhIdIn(paymentId, STATUS_ACTIVE.getShortValue(), captureBranchIds());

        if(tTrPayment == null)
            throw new DataNotFoundException("Payment not found for the Id " + paymentId);

        return tTrPayment;
    }

    private void setReferenceData(PaymentDTO paymentDTO) {
        if(!Strings.isNullOrEmpty(paymentDTO.getPaymentMethod())) {
            final CommonReferenceDTO commonReferenceDTO = commonReferenceService
                    .getByCmrfCodeAndCmrtCode(PAYMENT_TYPES.getValue(), paymentDTO.getPaymentMethod());

            paymentDTO.setPaymentMethodName(commonReferenceDTO.getDescription());
        }
    }

    private void validateReservation(Long reservationId) {
        if(reservationId == null)
            throw new NoRequiredInfoException("Reservation Id is required " + reservationId);

        final TMsReservation tMsReservation = reservationRepository
                .findByResvIdAndBranch_BrnhIdIn(reservationId, captureBranchIds());

        if(tMsReservation == null)
            throw new DataNotFoundException("Reservation not found for the Id " + reservationId);
    }

    private void validateReferenceData(PaymentDTO paymentDTO) {

        commonReferenceService
                .getByCmrfCodeAndCmrtCode(PAYMENT_TYPES.getValue(), paymentDTO.getPaymentMethod());
    }

    private TTrPayment persistEntity(TTrPayment tTrPayment) {
        try {
            validateEntity(tTrPayment);
            return paymentRepository.save(tTrPayment);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new TransactionConflictException("Transaction Updated by Another User.");
        } catch (Exception e) {
            log.error("Error while persisting : " + e.getMessage());
            throw new OperationException(e.getMessage());
        }
    }
}
