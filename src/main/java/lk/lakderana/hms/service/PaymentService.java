package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.PaginatedEntity;
import lk.lakderana.hms.dto.PaymentDTO;

import java.math.BigDecimal;

public interface PaymentService {

    PaginatedEntity paymentPaginatedSearch(String paymentMethod, Long reservationId, Short status, Integer page, Integer size);

    PaymentDTO createPayment(Long reservationId, PaymentDTO paymentDTO, Boolean isReservationValidated);

    BigDecimal calculateDueAmountForAReservation(Long reservationId, Boolean isReservationValidated);

    BigDecimal calculatePayedAmountForAReservation(Long reservationId, Boolean isReservationValidated);

    Boolean cancelPayment(Long paymentId, PaymentDTO paymentDTO);
}
