package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TRfInquiry;
import lk.lakderana.hms.entity.TTrInvoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<TTrInvoice, Long> {

    TTrInvoice findByReservation_ResvIdAndAndBranch_BrnhIdInAndInvcStatus(Long resvId, List<Long> brnhIdList, Short invcStatus);

    @Query("SELECT t FROM TTrInvoice t " +
            "WHERE UPPER(t.invcNumber) LIKE CONCAT('%', UPPER(:invoiceNumber), '%') " +
            "AND (:status IS NULL OR (:status IS NOT NULL AND t.invcStatus = :status)) " +
            "AND (:reservationId IS NULL OR (:reservationId IS NOT NULL AND t.invcId = :reservationId)) " +
            "AND t.branch.brnhId IN :branchIdList " +
            "ORDER BY t.lastModDate DESC")
    Page<TTrInvoice> getActiveInvoice(@Param("reservationId") Long reservationId,
                                      @Param("invoiceNumber") String invoiceNumber,
                                      @Param("status") Short status,
                                      @Param("branchIdList") List<Long> branchIdList,
                                      Pageable pageable);
}
