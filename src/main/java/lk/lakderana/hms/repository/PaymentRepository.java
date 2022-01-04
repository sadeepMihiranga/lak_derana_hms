package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TTrPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<TTrPayment, Long> {
}
