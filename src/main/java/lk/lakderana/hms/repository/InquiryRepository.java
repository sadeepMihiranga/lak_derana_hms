package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TRfInquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InquiryRepository extends JpaRepository<TRfInquiry, String> {

    TRfInquiry findByInqrIdAndBranch_BrnhIdInAndInqrStatus(Long inqrId, List<Long> brnhIdList, Short inqrStatus);

    TRfInquiry findByInqrIdAndBranch_BrnhIdIn(Long inqrId, List<Long> brnhIdList);

    @Query("SELECT t FROM TRfInquiry t " +
            "WHERE (:prtyCode IS NULL OR (:prtyCode IS NOT NULL AND t.inqrCustomerCode = :prtyCode)) " +
            "AND UPPER(t.inqrCustomerName) LIKE CONCAT('%', UPPER(:customerName), '%') " +
            "AND UPPER(t.inqrCustomerContactNo) LIKE CONCAT('%', UPPER(:customerContactNo), '%') " +
            "AND (:status IS NULL OR (:status IS NOT NULL AND t.inqrStatus = :status)) " +
            "AND t.branch.brnhId IN :branchIdList " +
            "ORDER BY t.lastModDate DESC")
    Page<TRfInquiry> getActiveInquiries(@Param("customerName") String customerName,
                                        @Param("customerContactNo") String customerContactNo,
                                        @Param("prtyCode") String prtyCode,
                                        @Param("status") Short status,
                                        @Param("branchIdList") List<Long> branchIdList,
                                        Pageable pageable);
}
