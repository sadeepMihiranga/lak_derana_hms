package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.RoomDTO;
import lk.lakderana.hms.dto.RoomReservationDTO;

public interface RoomReservationService {

    RoomReservationDTO reserveRoom(Long reservationId, RoomDTO roomDTO);
}
