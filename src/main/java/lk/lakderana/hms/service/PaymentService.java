package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.PaymentDTO;

import java.math.BigDecimal;

public interface PaymentService {

    PaymentDTO createPayment(Long reservationId, PaymentDTO paymentDTO, Boolean isReservationValidated);

    BigDecimal calculateDueAmountForAReservation(Long reservationId, Boolean isReservationValidated);

    BigDecimal calculatePayedAmountForAReservation(Long reservationId, Boolean isReservationValidated);
}
