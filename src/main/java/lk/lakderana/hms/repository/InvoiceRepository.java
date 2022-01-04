package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TTrInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<TTrInvoice, Long> {
}
