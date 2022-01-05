package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TTrPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<TTrPayment, Long> {

    List<TTrPayment> findAllByReservation_ResvIdAndPaytStatusAndBranch_BrnhIdIn(
            Long resvId, Short paytStatus, List<Long> branchIdList);
}
