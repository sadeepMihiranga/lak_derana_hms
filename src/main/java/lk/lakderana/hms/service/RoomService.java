package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.PaginatedEntity;
import lk.lakderana.hms.dto.RoomDTO;

public interface RoomService {

    PaginatedEntity roomPaginatedSearch(String roomType, String roomCategory, Integer page, Integer size);

    RoomDTO createRoom(RoomDTO roomDTO);

    RoomDTO updateRoom(Long roomId, RoomDTO roomDTO);

    Boolean removeRoom(Long roomId);
}
