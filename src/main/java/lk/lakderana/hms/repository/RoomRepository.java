package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TMsRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<TMsRoom, Long> {

    @Query("SELECT t FROM TMsRoom t " +
            "WHERE t.roomStatus = :status " +
            "AND UPPER(t.roomType) LIKE CONCAT('%', UPPER(:roomType), '%') " +
            "AND UPPER(t.roomCategory) LIKE CONCAT('%', UPPER(:roomCategory), '%') " +
            "AND t.branch.brnhId = :branchId " +
            "ORDER BY t.lastModUserCode")
    Page<TMsRoom> getActiveRooms(@Param("roomType") String roomType,
                                 @Param("roomCategory") String roomCategory,
                                 @Param("status") Short status,
                                 @Param("branchId") Long branchId,
                                 Pageable pageable);

    TMsRoom getByRoomIdAndBranch_BrnhIdAndRoomStatus(Long roomId, Long brnhId, Short roomStatus);
}
