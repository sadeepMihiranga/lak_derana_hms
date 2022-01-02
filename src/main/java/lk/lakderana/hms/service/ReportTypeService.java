package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.ReportTypeDTO;

import java.util.List;

public interface ReportTypeService {

    List<ReportTypeDTO> getReportTypes();

    ReportTypeDTO getByCode(String reportCode);
}
