package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.dto.*;
import lk.lakderana.hms.exception.DataNotFoundException;
import lk.lakderana.hms.exception.OperationException;
import lk.lakderana.hms.service.ReportHandler;
import lk.lakderana.hms.service.ReportHistoryService;
import lk.lakderana.hms.service.ReportService;
import lk.lakderana.hms.service.ReportTypeService;
import lk.lakderana.hms.util.DateConversion;
import lk.lakderana.hms.util.constant.ReportCodes;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.assertj.core.util.Strings;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static lk.lakderana.hms.util.constant.ReportCodes.*;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    private final ResourceLoader resourceLoader;

    private final ReportHandler reportHandler;
    private final ReportTypeService reportTypeService;
    private final ReportHistoryService reportHistoryService;

    public ReportServiceImpl(ResourceLoader resourceLoader,
                             ReportHandler reportHandler,
                             @Lazy ReportHistoryService reportHistoryService,
                             ReportTypeService reportTypeService) {
        this.resourceLoader = resourceLoader;
        this.reportHandler = reportHandler;
        this.reportHistoryService = reportHistoryService;
        this.reportTypeService = reportTypeService;
    }

    @Override
    public ReportWrapperDTO generateDetailedCsvReport(String startDate, String endDate, String reportType, String reportCode)
            throws IOException, JRException, ParseException {

        ReportContentWrapperDTO reportWrapperDTO = null;

        final ReportTypeDTO reportTypeDTO = reportTypeService.getByCode(reportCode);

        reportWrapperDTO = getDetailedReport(startDate, endDate, reportTypeDTO);

        InputStream jasperReport = getReportTemplate(reportType);
        HashMap<String, Object> parameters = new HashMap<>();

        JRBeanArrayDataSource beanColDataSource = new JRBeanArrayDataSource(new ReportContentWrapperDTO[]{reportWrapperDTO});
        parameters.put("REPORT_TYPE", reportType);
        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);

        removeBlankPage(print.getPages());

        log.info("generate Detailed Report, startDate:{}, endDate:{}", startDate, endDate);

        ReportHistoryDTO reportHistoryDTO = new ReportHistoryDTO();
        reportHistoryDTO.setReportType(reportCode);
        reportHistoryDTO.setFromDate(DateConversion.convertStringToLocalDateTime(startDate + " 00:00:00"));
        reportHistoryDTO.setToDate(DateConversion.convertStringToLocalDateTime(endDate+ " 23:59:59"));

        reportHistoryService.createReportHistory(reportHistoryDTO);

        final String reportFileNameWithExtension = reportTypeDTO.getDisplayName() + "_" + startDate + "_" + endDate + ".csv";

        return new ReportWrapperDTO(print, reportFileNameWithExtension);
    }

    private ReportContentWrapperDTO getDetailedReport(String startDate, String endDate, ReportTypeDTO reportTypeDTO) throws ParseException {

        if (Strings.isNullOrEmpty(startDate)) {
            log.error("missing start date, startDate:{}", startDate);
            throw new DataNotFoundException("Start Date is Required.");
        }

        if (Strings.isNullOrEmpty(endDate)) {
            log.error("Missing End Date, endDate:{}", endDate);
            throw new DataNotFoundException("End Date is Required.");
        }

        ReportContentWrapperDTO reportWrapper = new ReportContentWrapperDTO();

        if(reportTypeDTO.getReportTypeCode().equals(INQUIRY_DETAILED.getValue())) {
            List<ReportDTO> inquiryReportContent = reportHandler.getInquiryReportContent(
                    DateConversion.convertStringToDate(startDate, "yyyy-MM-dd"),
                    DateConversion.convertStringToDate(endDate, "yyyy-MM-dd"));

            reportWrapper.setReportDatasource(new JRBeanCollectionDataSource(inquiryReportContent, false));
        }

        if(reportTypeDTO.getReportTypeCode().equals(RESERVATION_DETAILED.getValue())) {
            List<ReportDTO> reservationReportContent = reportHandler.getReservationReportContent(
                    DateConversion.convertStringToDate(startDate, "yyyy-MM-dd"),
                    DateConversion.convertStringToDate(endDate, "yyyy-MM-dd"));

            reportWrapper.setReportDatasource(new JRBeanCollectionDataSource(reservationReportContent, false));
        }

        if(reportTypeDTO.getReportTypeCode().equals(INCOME_DETAILED.getValue())) {
            List<ReportDTO> incomeDetailedReportContent = reportHandler.getIncomeDetailedReportContent(
                    DateConversion.convertStringToDate(startDate, "yyyy-MM-dd"),
                    DateConversion.convertStringToDate(endDate, "yyyy-MM-dd"));

            reportWrapper.setReportDatasource(new JRBeanCollectionDataSource(incomeDetailedReportContent, false));
        }

        if(reportTypeDTO.getReportTypeCode().equals(INVOICE_WISE_INCOME_DETAILED.getValue())) {
            List<ReportDTO> invoideWiseIncomeDetailedReportContent = reportHandler.getInvoiceWiseIncomeDetailedReportContent(
                    DateConversion.convertStringToDate(startDate, "yyyy-MM-dd"),
                    DateConversion.convertStringToDate(endDate, "yyyy-MM-dd"));

            reportWrapper.setReportDatasource(new JRBeanCollectionDataSource(invoideWiseIncomeDetailedReportContent, false));
        }

        reportWrapper.setStartDate(setReportHeadingDate(startDate));
        reportWrapper.setEndDate(setReportHeadingDate(endDate));
        reportWrapper.setReportName(reportTypeDTO.getDisplayName());

        log.info("get Inquiry Report, startDate:{}, endDate:{}", startDate, endDate);
        return reportWrapper;
    }

    private String setReportHeadingDate(String convertDate) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        log.info("set report heading date, covertDate:{}", convertDate);
        return simpleDateFormat.format(DateConversion.convertStringToDate(convertDate + " " + "00:00:00"));
    }

    private void removeBlankPage(List<JRPrintPage> pages) {
        for (Iterator<JRPrintPage> i = pages.iterator(); i.hasNext(); ) {
            JRPrintPage page = i.next();
            if (page.getElements().size() == 0) i.remove();
        }
    }

    private InputStream getReportTemplate(String reportType) throws IOException {
        if (reportType.equals("EXCEL"))
            return resourceLoader.getResource("classpath:/reports/inquiry/common_report_excel.jasper").getInputStream();
        else
            return resourceLoader.getResource("classpath:common_report_excel.jasper").getInputStream();
    }

}
