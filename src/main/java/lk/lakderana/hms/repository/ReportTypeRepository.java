package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TRfReportType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportTypeRepository extends JpaRepository<TRfReportType, Long> {

    List<TRfReportType> findAllByRptpStatus(Short rptpStatus);

    TRfReportType findByRptpCode(String rptpCode);
}
