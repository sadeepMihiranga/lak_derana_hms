package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TTrInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<TTrInvoice, Long> {

    TTrInvoice findByReservation_ResvIdAndAndBranch_BrnhIdInAndInvcStatus(Long resvId, List<Long> brnhIdList, Short invcStatus);
}
