package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TTrInvoiceDet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceDetRepository extends JpaRepository<TTrInvoiceDet, Long> {
}
