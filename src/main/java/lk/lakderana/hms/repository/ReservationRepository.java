package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TMsReservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<TMsReservation, Long> {

    @Query("SELECT t FROM TMsReservation t " +
            "WHERE (:noOfAdults IS NULL OR (:noOfAdults IS NOT NULL AND t.resvNoOfAdults = :noOfAdults)) " +
            "AND (:status IS NULL OR (:status IS NOT NULL AND t.resvStatus = :status)) " +
            "AND t.branch.brnhId IN :branchIdList " +
            "ORDER BY t.lastModDate DESC")
    Page<TMsReservation> searchReservations(@Param("noOfAdults") Integer noOfAdults,
                                            @Param("status") Short status,
                                            @Param("branchIdList") List<Long> branchIdList,
                                            Pageable pageable);

    TMsReservation findByResvIdAndBranch_BrnhIdIn(Long resvId, List<Long> branchIdList);
}
