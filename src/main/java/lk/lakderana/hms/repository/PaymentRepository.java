package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TTrPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<TTrPayment, Long> {

    List<TTrPayment> findAllByReservation_ResvIdAndPaytStatusAndBranch_BrnhIdIn(
            Long resvId, Short paytStatus, List<Long> branchIdList);

    @Query("SELECT t FROM TTrPayment t " +
            "WHERE UPPER(t.paytType) LIKE CONCAT('%', UPPER(:paymentMethod), '%') " +
            "AND (:reservationId IS NULL OR (:reservationId IS NOT NULL AND t.reservation.resvId = :reservationId)) " +
            "AND (:status IS NULL OR (:status IS NOT NULL AND t.paytStatus = :status)) " +
            "AND t.branch.brnhId IN :branchIdList " +
            "ORDER BY t.lastModDate DESC")
    Page<TTrPayment> searchPayments(@Param("paymentMethod") String paymentMethod,
                                    @Param("reservationId") Long reservationId,
                                    @Param("branchIdList") List<Long> branchIdList,
                                    @Param("status") Short status,
                                    Pageable pageable);

    TTrPayment findByPaytIdAndPaytStatusAndBranch_BrnhIdIn(Long paytId, Short paytStatus, List<Long> brnhIdList);
}
