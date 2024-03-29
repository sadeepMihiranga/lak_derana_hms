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
import lk.lakderana.hms.repository.NumberGeneratorRepository;
import lk.lakderana.hms.repository.RoomRepository;
import lk.lakderana.hms.service.CommonReferenceService;
import lk.lakderana.hms.service.RoomService;
import lk.lakderana.hms.util.constant.status.RoomStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static lk.lakderana.hms.util.constant.CommonReferenceTypeCodes.*;
import static lk.lakderana.hms.util.constant.Constants.*;
import static lk.lakderana.hms.util.constant.status.RoomStatus.READY_FOR_BOOKING;

@Slf4j
@Service
public class RoomServiceImpl extends EntityValidator implements RoomService {

    private final RoomRepository roomRepository;
    private final NumberGeneratorRepository numberGeneratorRepository;

    private final CommonReferenceService commonReferenceService;

    public RoomServiceImpl(RoomRepository roomRepository,
                           NumberGeneratorRepository numberGeneratorRepository,
                           CommonReferenceService commonReferenceService) {
        this.roomRepository = roomRepository;
        this.numberGeneratorRepository = numberGeneratorRepository;
        this.commonReferenceService = commonReferenceService;
    }

    @Override
    public PaginatedEntity roomPaginatedSearch(String roomType, String roomNo,
                                               Short status, Integer page, Integer size) {

        PaginatedEntity paginatedRoomList = null;
        List<RoomDTO> roomList = null;

        validatePaginateIndexes(page, size);

        roomNo = roomNo.isEmpty() ? null : roomNo;

        Page<TMsRoom> tMsRoomPage = roomRepository
                .searchRooms(roomType, roomNo, captureBranchIds(), status, PageRequest.of(page - 1, size));

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
        String roomNo = null;

        validateEntity(roomDTO);
        validateReferenceData(roomDTO);

        roomDTO.setBranchId(captureBranchIds().get(0));
        roomDTO.setStatus(READY_FOR_BOOKING.getShortValue());
        roomDTO.setRoomNo(roomDTO.getRoomNo());

        final TMsRoom tMsRoom = RoomMapper.INSTANCE.dtoToEntity(roomDTO);

        return RoomMapper.INSTANCE.entityToDTO(persistEntity(tMsRoom));
    }

    @Override
    public RoomDTO updateRoom(Long roomId, RoomDTO roomDTO) {

        validateEntity(roomDTO);
        validateReferenceData(roomDTO);

        roomDTO.setBranchId(captureBranchIds().get(0));
        TMsRoom tMsRoom = validateRoomId(roomId);

        tMsRoom.setRoomType(roomDTO.getRoomType());
        tMsRoom.setRoomPrice(roomDTO.getRoomPrice());
        tMsRoom.setRoomNo(roomDTO.getRoomNo());
        tMsRoom.setRoomStatus(roomDTO.getStatus());

        return RoomMapper.INSTANCE.entityToDTO(persistEntity(tMsRoom));
    }

    @Override
    public Boolean inactiveRoom(Long roomId) {

        TMsRoom tMsRoom = validateRoomId(roomId);

        if(tMsRoom.getRoomStatus() == RoomStatus.RESERVED.getShortValue()
                || tMsRoom.getRoomStatus() == RoomStatus.IN_USE.getShortValue()) {
            throw new OperationException("Cannot deactivate the Room "+ tMsRoom.getRoomNo() +". Room is in Use");
        }

        tMsRoom.setRoomStatus(STATUS_INACTIVE.getShortValue());
        persistEntity(tMsRoom);

        return true;
    }

    @Override
    public Boolean removeRoom(Long roomId) {

        TMsRoom tMsRoom = validateRoomId(roomId);

        if(tMsRoom.getRoomStatus() == RoomStatus.RESERVED.getShortValue()
                || tMsRoom.getRoomStatus() == RoomStatus.IN_USE.getShortValue()) {
            throw new OperationException("Cannot remove the Room "+ tMsRoom.getRoomNo() +". Room is in Use");
        }

        tMsRoom.setRoomStatus(RoomStatus.REMOVED.getShortValue());
        persistEntity(tMsRoom);

        return true;
    }

    @Override
    public RoomDTO getRoomById(Long roomId) {

        final TMsRoom tMsRoom = validateRoomId(roomId);

        RoomDTO roomDTO = RoomMapper.INSTANCE.entityToDTO(validateRoomId(roomId));
        setReferenceData(tMsRoom, roomDTO);

        return roomDTO;
    }

    private TMsRoom validateRoomId(Long roomId) {

        if(roomId == null)
            throw new NoRequiredInfoException("Room Id is required");

        final TMsRoom tMsRoom = roomRepository
                .getByRoomIdAndBranch_BrnhIdInAndRoomStatusNot(roomId, captureBranchIds(), RoomStatus.REMOVED.getShortValue());

        if(tMsRoom == null)
            throw new DataNotFoundException("Room not found for Id " + roomId);

        return tMsRoom;
    }

    private void validateReferenceData(RoomDTO roomDTO) {

        commonReferenceService
                .getByCmrfCodeAndCmrtCode(ROOM_TYPES.getValue(), roomDTO.getRoomType());

        commonReferenceService
                .getByCmrfCodeAndCmrtCode(ROOM_BED_TYPES.getValue(), roomDTO.getRoomBedType());
    }

    private void setReferenceData(TMsRoom tMsRoom, RoomDTO roomDTO) {

        if(tMsRoom.getBranch() != null)
            roomDTO.setBranchName(tMsRoom.getBranch().getBrnhName());

        if(!Strings.isNullOrEmpty(roomDTO.getRoomType())) {
            final CommonReferenceDTO commonReferenceDTO = commonReferenceService
                    .getByCmrfCodeAndCmrtCode(ROOM_TYPES.getValue(), roomDTO.getRoomType());

            roomDTO.setRoomTypeName(commonReferenceDTO.getDescription());
        }

        if(!Strings.isNullOrEmpty(roomDTO.getRoomBedType())) {
            final CommonReferenceDTO commonReferenceDTO = commonReferenceService
                    .getByCmrfCodeAndCmrtCode(ROOM_BED_TYPES.getValue(), roomDTO.getRoomBedType());

            roomDTO.setRoomBedTypeName(commonReferenceDTO.getDescription());
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
