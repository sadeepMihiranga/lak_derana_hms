package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.PaginatedEntity;
import lk.lakderana.hms.dto.ReservationDTO;

import java.math.BigDecimal;

public interface ReservationService {

    PaginatedEntity reservationPaginatedSearch(Short status, Integer noOfAdults, Integer page, Integer size);

    ReservationDTO createReservation(ReservationDTO reservationDTO);

    Long cancelReservation(Long reservationId, ReservationDTO reservationDTO);

    ReservationDTO updateReservation(Long reservationId, ReservationDTO reservationDTO);

    ReservationDTO getReservationById(Long reservationId);

    BigDecimal calculateTotalReservationAmount(Long reservationId);

    Boolean releaseReservation(Long reservationId);
}
