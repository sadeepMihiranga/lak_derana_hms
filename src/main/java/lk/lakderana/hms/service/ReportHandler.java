package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.InquiryDTO;
import lk.lakderana.hms.dto.ReportDTO;

import java.util.Date;
import java.util.List;

public interface ReportHandler {

    List<ReportDTO> getInquiryReportContent(Date fromDate, Date toDate);
}
