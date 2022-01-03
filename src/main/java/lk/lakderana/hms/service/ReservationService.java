package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.PaginatedEntity;
import lk.lakderana.hms.dto.ReservationDTO;

public interface ReservationService {

    PaginatedEntity reservationPaginatedSearch(Short status, Integer noOfPersons, Integer page, Integer size);

    ReservationDTO createReservation(ReservationDTO reservationDTO);

    Long cancelReservation(Long reservationId, ReservationDTO reservationDTO);

    ReservationDTO updateReservation(Long reservationId, ReservationDTO reservationDTO);

    ReservationDTO getReservationById(Long reservationId);
}
