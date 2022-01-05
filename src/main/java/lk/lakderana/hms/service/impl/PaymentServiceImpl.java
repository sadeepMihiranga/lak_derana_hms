package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.config.EntityValidator;
import lk.lakderana.hms.dto.PaymentDTO;
import lk.lakderana.hms.entity.TMsReservation;
import lk.lakderana.hms.entity.TMsRoom;
import lk.lakderana.hms.entity.TTrPayment;
import lk.lakderana.hms.entity.TTrRoomReservation;
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
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static lk.lakderana.hms.util.constant.CommonReferenceTypeCodes.PAYMENT_TYPES;
import static lk.lakderana.hms.util.constant.Constants.STATUS_ACTIVE;

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
    public PaymentDTO createPayment(Long reservationId, PaymentDTO paymentDTO, Boolean isReservationValidated) {

        validateEntity(paymentDTO);
        validateReferenceData(paymentDTO);

        if(!isReservationValidated)
            validateReservation(reservationId);

        if(paymentDTO.getAmount().compareTo(BigDecimal.ZERO) == 0)
            throw new OperationException("Payment Amount is not valid");

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
                .getByCmrfCodeAndCmrtCode(PAYMENT_TYPES.getValue(), paymentDTO.getPaymentType());
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
