package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.PaginatedEntity;
import lk.lakderana.hms.dto.ReportWrapperDTO;
import lk.lakderana.hms.dto.ReportHistoryDTO;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.text.ParseException;

public interface ReportHistoryService {

    ReportHistoryDTO createReportHistory(ReportHistoryDTO reportHistoryDTO);

    PaginatedEntity reportHistoryPaginatedSearch(String reportTypeCode, Integer page, Integer size);

    Boolean removeReportHistory(Long reportHistoryId);

    ReportWrapperDTO regenerateDetailedCsvReport(Long reportHistoryId) throws IOException, JRException, ParseException;
}
