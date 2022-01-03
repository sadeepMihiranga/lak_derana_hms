package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TTrReportHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportHistoryRepository extends JpaRepository<TTrReportHistory, Long> {

    @Query("SELECT t FROM TTrReportHistory t " +
            "WHERE (:reportTypeCode IS NULL OR (:reportTypeCode IS NOT NULL AND t.reportType.rptpCode = :reportTypeCode)) " +
            "AND t.branch.brnhId IN :branchIdList " +
            "AND t.rphtStatus = :status " +
            "ORDER BY t.lastModDate DESC")
    Page<TTrReportHistory> getReportHistory(@Param("reportTypeCode") String reportTypeCode,
                                            @Param("status") Short status,
                                            @Param("branchIdList") List<Long> branchIdList,
                                            Pageable pageable);

    TTrReportHistory findByRphtIdAndRphtStatusAndBranch_BrnhIdIn(Long rphtId, Short rphtStatus, List<Long> brnhIdList);
}
