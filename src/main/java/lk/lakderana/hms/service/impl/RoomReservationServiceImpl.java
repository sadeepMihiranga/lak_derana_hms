package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.config.EntityValidator;
import lk.lakderana.hms.dto.RoomDTO;
import lk.lakderana.hms.dto.RoomReservationDTO;
import lk.lakderana.hms.entity.TMsRoom;
import lk.lakderana.hms.entity.TTrRoomReservation;
import lk.lakderana.hms.exception.DataNotFoundException;
import lk.lakderana.hms.exception.NoRequiredInfoException;
import lk.lakderana.hms.exception.OperationException;
import lk.lakderana.hms.exception.TransactionConflictException;
import lk.lakderana.hms.mapper.RoomMapper;
import lk.lakderana.hms.repository.BranchRepository;
import lk.lakderana.hms.repository.ReservationRepository;
import lk.lakderana.hms.repository.RoomRepository;
import lk.lakderana.hms.repository.RoomReservationRepository;
import lk.lakderana.hms.service.RoomReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import static lk.lakderana.hms.util.constant.Constants.STATUS_ACTIVE;

@Slf4j
@Service
public class RoomReservationServiceImpl extends EntityValidator implements RoomReservationService {

    private final RoomRepository roomRepository;
    private final RoomReservationRepository roomReservationRepository;
    private final ReservationRepository reservationRepository;
    private final BranchRepository branchRepository;

    public RoomReservationServiceImpl(RoomRepository roomRepository,
                                      RoomReservationRepository roomReservationRepository,
                                      ReservationRepository reservationRepository,
                                      BranchRepository branchRepository) {
        this.roomRepository = roomRepository;
        this.roomReservationRepository = roomReservationRepository;
        this.reservationRepository = reservationRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    public RoomReservationDTO reserveRoom(Long reservationId, RoomDTO roomDTO) {

        if(roomDTO.getRoomId() == null)
            throw new NoRequiredInfoException("Room Id is required");

        final TMsRoom tMsRoom = roomRepository.getByRoomIdAndBranch_BrnhIdIn(roomDTO.getRoomId(), captureBranchIds());

        if(tMsRoom == null)
            throw new DataNotFoundException("Room not found for the Id " + roomDTO.getRoomId());

        TTrRoomReservation tTrRoomReservation = new TTrRoomReservation();

        tTrRoomReservation.setRoom(tMsRoom);
        tTrRoomReservation.setReservation(reservationRepository.findByResvIdAndBranch_BrnhIdIn(reservationId, captureBranchIds()));
        tTrRoomReservation.setRoreStatus(STATUS_ACTIVE.getShortValue());
        tTrRoomReservation.setBranch(branchRepository.getById(captureBranchIds().get(0)));

        final TTrRoomReservation createdRoomReservation = persistEntity(tTrRoomReservation);

        return new RoomReservationDTO(
                createdRoomReservation.getRoreId(),
                RoomMapper.INSTANCE.entityToDTO(tMsRoom),
                createdRoomReservation.getRoreStatus());
    }

    private TTrRoomReservation persistEntity(TTrRoomReservation tTrRoomReservation) {
        try {
            validateEntity(tTrRoomReservation);
            return roomReservationRepository.save(tTrRoomReservation);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new TransactionConflictException("Transaction Updated by Another User.");
        } catch (Exception e) {
            log.error("Error while persisting : " + e.getMessage());
            throw new OperationException(e.getMessage());
        }
    }
}
