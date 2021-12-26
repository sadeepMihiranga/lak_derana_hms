package lk.lakderana.hms.service.impl;

import com.google.common.base.Strings;
import lk.lakderana.hms.config.EntityValidator;
import lk.lakderana.hms.dto.CommonReferenceDTO;
import lk.lakderana.hms.dto.PaginatedEntity;
import lk.lakderana.hms.dto.RoomDTO;
import lk.lakderana.hms.entity.TMsRoom;
import lk.lakderana.hms.exception.DataNotFoundException;
import lk.lakderana.hms.exception.NoRequiredInfoException;
import lk.lakderana.hms.exception.OperationException;
import lk.lakderana.hms.exception.TransactionConflictException;
import lk.lakderana.hms.mapper.RoomMapper;
import lk.lakderana.hms.repository.RoomRepository;
import lk.lakderana.hms.service.CommonReferenceService;
import lk.lakderana.hms.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static lk.lakderana.hms.util.CommonReferenceTypeCodes.*;
import static lk.lakderana.hms.util.Constants.*;

@Slf4j
@Service
public class RoomServiceImpl extends EntityValidator implements RoomService {

    private final RoomRepository roomRepository;

    private final CommonReferenceService commonReferenceService;

    public RoomServiceImpl(RoomRepository roomRepository,
                           CommonReferenceService commonReferenceService) {
        this.roomRepository = roomRepository;
        this.commonReferenceService = commonReferenceService;
    }

    @Override
    public PaginatedEntity roomPaginatedSearch(String roomType, String roomCategory, Integer page, Integer size) {

        PaginatedEntity paginatedRoomList = null;
        List<RoomDTO> roomList = null;

        validatePaginateIndexes(page, size);

        Page<TMsRoom> tMsRoomPage = roomRepository
                .getActiveRooms(roomType, roomCategory, STATUS_ACTIVE.getShortValue(), captureBranchIds(),
                        PageRequest.of(page - 1, size));

        if (tMsRoomPage.getSize() == 0)
            return null;

        paginatedRoomList = new PaginatedEntity();
        roomList = new ArrayList<>();

        for (TMsRoom tMsRoom : tMsRoomPage) {

            RoomDTO roomDTO = RoomMapper.INSTANCE.entityToDTO(tMsRoom);
            setReferenceData(tMsRoom, roomDTO);

            roomList.add(roomDTO);
        }

        paginatedRoomList.setTotalNoOfPages(tMsRoomPage.getTotalPages());
        paginatedRoomList.setTotalNoOfRecords(tMsRoomPage.getTotalElements());
        paginatedRoomList.setEntities(roomList);

        return paginatedRoomList;
    }

    @Override
    public RoomDTO createRoom(RoomDTO roomDTO) {

        validateEntity(roomDTO);
        validateReferenceData(roomDTO);

        roomDTO.setBranchId(captureBranchIds().get(0));
        roomDTO.setStatus(STATUS_ACTIVE.getShortValue());

        final TMsRoom tMsRoom = RoomMapper.INSTANCE.dtoToEntity(roomDTO);

        return RoomMapper.INSTANCE.entityToDTO(persistEntity(tMsRoom));
    }

    @Override
    public RoomDTO updateRoom(Long roomId, RoomDTO roomDTO) {

        validateEntity(roomDTO);
        validateReferenceData(roomDTO);

        roomDTO.setBranchId(captureBranchIds().get(0));
        TMsRoom tMsRoom = validateRoomId(roomId);

        tMsRoom.setRoomCategory(roomDTO.getRoomCategory());
        tMsRoom.setRoomType(roomDTO.getRoomType());
        tMsRoom.setRoomPrice(roomDTO.getRoomPrice());

        return RoomMapper.INSTANCE.entityToDTO(persistEntity(tMsRoom));
    }

    @Override
    public Boolean removeRoom(Long roomId) {

        TMsRoom tMsRoom = validateRoomId(roomId);

        tMsRoom.setRoomStatus(STATUS_INACTIVE.getShortValue());
        persistEntity(tMsRoom);

        return true;
    }

    private TMsRoom validateRoomId(Long roomId) {

        if(roomId == null)
            throw new NoRequiredInfoException("Room Id is required");

        final TMsRoom tMsRoom = roomRepository
                .getByRoomIdAndBranch_BrnhIdInAndRoomStatus(roomId, captureBranchIds(), STATUS_ACTIVE.getShortValue());

        if(tMsRoom == null)
            throw new DataNotFoundException("Room not found for Id " + roomId);

        return tMsRoom;
    }

    private void validateReferenceData(RoomDTO roomDTO) {

        commonReferenceService
                .getByCmrfCodeAndCmrtCode(ROOM_TYPES.getValue(), roomDTO.getRoomType());

        commonReferenceService
                .getByCmrfCodeAndCmrtCode(ROOM_CATEGORY_TYPES.getValue(), roomDTO.getRoomCategory());
    }

    private void setReferenceData(TMsRoom tMsRoom, RoomDTO roomDTO) {

        if(tMsRoom.getBranch() != null)
            roomDTO.setBranchName(tMsRoom.getBranch().getBrnhName());

        if(!Strings.isNullOrEmpty(roomDTO.getRoomType())) {
            final CommonReferenceDTO commonReferenceDTO = commonReferenceService
                    .getByCmrfCodeAndCmrtCode(ROOM_TYPES.getValue(), roomDTO.getRoomType());

            roomDTO.setRoomTypeName(commonReferenceDTO.getDescription());
        }

        if(!Strings.isNullOrEmpty(roomDTO.getRoomCategory())) {
            final CommonReferenceDTO commonReferenceDTO = commonReferenceService
                    .getByCmrfCodeAndCmrtCode(ROOM_CATEGORY_TYPES.getValue(), roomDTO.getRoomCategory());

            roomDTO.setRoomCategoryName(commonReferenceDTO.getDescription());
        }
    }

    private TMsRoom persistEntity(TMsRoom tMsRoom) {
        try {
            validateEntity(tMsRoom);
            return roomRepository.save(tMsRoom);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new TransactionConflictException("Transaction Updated by Another User.");
        } catch (Exception e) {
            log.error("Error while persisting : " + e.getMessage());
            throw new OperationException(e.getMessage());
        }
    }
}
