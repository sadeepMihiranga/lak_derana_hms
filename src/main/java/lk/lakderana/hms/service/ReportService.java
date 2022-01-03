package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.ReportWrapperDTO;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.text.ParseException;

public interface ReportService {

    ReportWrapperDTO generateDetailedCsvReport(String startDate, String endDate, String reportType, String reportCode)
            throws IOException, JRException, ParseException;
}