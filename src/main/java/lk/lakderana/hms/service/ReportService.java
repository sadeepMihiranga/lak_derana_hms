package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.ReportWrapperDTO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

import java.io.IOException;
import java.text.ParseException;

public interface ReportService {

    JasperPrint generateDetailedCsvReport(String startDate, String endDate, String reportType, String reportCode) throws IOException, JRException, ParseException;
    ReportWrapperDTO getInquiryReport(String startDate, String endDate) throws ParseException;
}
