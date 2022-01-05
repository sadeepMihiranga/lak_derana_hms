package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.RoomDTO;
import lk.lakderana.hms.dto.RoomReservationDTO;

import java.math.BigDecimal;
import java.util.List;

public interface RoomReservationService {

    RoomReservationDTO reserveRoom(Long reservationId, RoomDTO roomDTO);

    Boolean cancelRoomReservationByReservation(Long reservationId);

    List<RoomReservationDTO> getRoomReservationsByReservation(Long reservationId);

    BigDecimal calculateRoomReservationAmount(Long reservationId);
}
