package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TMsRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<TMsRoom, Long> {

    @Query("SELECT t FROM TMsRoom t " +
            "WHERE UPPER(t.roomType) LIKE CONCAT('%', UPPER(:roomType), '%') " +
            "AND UPPER(t.roomCategory) LIKE CONCAT('%', UPPER(:roomCategory), '%') " +
            "AND (:status IS NULL OR (:status IS NOT NULL AND t.roomStatus = :status)) " +
            "AND (:roomNumber IS NULL OR (:roomNumber IS NOT NULL AND t.roomNo = :roomNumber)) " +
            "AND t.branch.brnhId IN :branchIdList " +
            "AND t.roomStatus <> 5 " +
            "ORDER BY t.lastModDate DESC")
    Page<TMsRoom> searchRooms(@Param("roomType") String roomType,
                              @Param("roomCategory") String roomCategory,
                              @Param("roomNumber") String roomNumber,
                              @Param("branchIdList") List<Long> branchIdList,
                              @Param("status") Short status,
                              Pageable pageable);

    TMsRoom getByRoomIdAndBranch_BrnhIdInAndRoomStatusNot(Long roomId, List<Long> brnhIdList, Short roomStatus);
}
