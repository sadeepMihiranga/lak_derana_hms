package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TTrRoomReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomReservationRepository extends JpaRepository<TTrRoomReservation, Long> {

    List<TTrRoomReservation> findAllByReservation_ResvIdAndRoreStatusAndBranch_BrnhIdIn(Long resvId, Short roreStatus,
                                                                                        List<Long> brnhIdList);
}
