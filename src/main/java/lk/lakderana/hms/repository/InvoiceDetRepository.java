package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TTrInvoiceDet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceDetRepository extends JpaRepository<TTrInvoiceDet, Long> {

    List<TTrInvoiceDet> findAllByReservation_ResvIdAndAndBranch_BrnhIdIn(Long resvId, List<Long> brnhIdList);
}
