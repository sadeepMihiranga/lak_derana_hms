package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TRfInquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InquiryRepository extends JpaRepository<TRfInquiry, String> {

    TRfInquiry findByInqrIdAndInqrStatus(Long inqrId, Short inqrStatus);

    @Query("SELECT t FROM TRfInquiry t " +
            "WHERE t.inqrStatus = :status " +
            "AND (:prtyCode IS NULL OR (:prtyCode IS NOT NULL AND t.inqrCustomerCode = :prtyCode)) " +
            "AND UPPER(t.inqrCustomerName) LIKE CONCAT('%', UPPER(:customerName), '%') " +
            "AND UPPER(t.inqrCustomerContactNo) LIKE CONCAT('%', UPPER(:customerContactNo), '%') " +
            "ORDER BY t.lastModDate DESC")
    Page<TRfInquiry> getActiveInquiries(@Param("customerName") String customerName,
                                        @Param("customerContactNo") String customerContactNo,
                                        @Param("prtyCode") String prtyCode,
                                        @Param("status") Short status,
                                        Pageable pageable);
}
