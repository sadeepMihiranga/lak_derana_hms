package lk.lakderana.hms.controller;

import lk.lakderana.hms.dto.ReportWrapperDTO;
import lk.lakderana.hms.response.SuccessResponse;
import lk.lakderana.hms.response.SuccessResponseHandler;
import lk.lakderana.hms.service.ReportHistoryService;
import lk.lakderana.hms.service.ReportService;
import lk.lakderana.hms.service.ReportTypeService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.export.SimpleCsvReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarException;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;
    private final ReportTypeService reportTypeService;
    private final ReportHistoryService reportHistoryService;

    public ReportController(ReportService reportService,
                            ReportTypeService reportTypeService,
                            ReportHistoryService reportHistoryService) {
        this.reportService = reportService;
        this.reportTypeService = reportTypeService;
        this.reportHistoryService = reportHistoryService;
    }

    @GetMapping("/types")
    public ResponseEntity<SuccessResponse> getReportTypes() {
        return SuccessResponseHandler.generateResponse(reportTypeService.getReportTypes());
    }

    @GetMapping(path = "/types/{reportTypeId}")
    public ResponseEntity<SuccessResponse> getReportTypeById(@PathVariable("reportTypeId") Long reportTypeId) {
        return SuccessResponseHandler.generateResponse(reportTypeService.getByReportId(reportTypeId));
    }

    @GetMapping(path = "/history/search")
    public ResponseEntity<SuccessResponse> reportHistoryPaginatedSearch(
            @RequestParam(name = "reportTypeCode", required = false) String reportTypeCode,
            @RequestParam(name = "page", required = true) Integer page,
            @RequestParam(name = "size", required = true) Integer size) {
        return SuccessResponseHandler.generateResponse(reportHistoryService
                .reportHistoryPaginatedSearch(reportTypeCode, page, size));
    }

    @DeleteMapping(path = "/history/{reportHistoryId}")
    public ResponseEntity<SuccessResponse> removeReportHistory(@PathVariable("reportHistoryId") Long reportHistoryId) {
        return SuccessResponseHandler.generateResponse(reportHistoryService.removeReportHistory(reportHistoryId));
    }

    @GetMapping(path = "/history/{reportHistoryId}/regenerate")
    public void regenerateDetailedReportHistory(HttpServletResponse response,
                                                @PathVariable("reportHistoryId") Long reportHistoryId) throws IOException, JRException, ParseException {

        final ReportWrapperDTO detailedCsvReport = reportHistoryService.regenerateDetailedCsvReport(reportHistoryId);

        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition",
                String.format("attachment; filename=\""+detailedCsvReport.getReportFileNameWithExtension()+"\""));

        generateToExcel(response, detailedCsvReport.getJasperPrint(), response.getOutputStream());
    }

    @GetMapping("/detailed")
    public void generateDetailedXlsReport(HttpServletResponse response,
                                          @RequestParam(name = "startDate") String startDate ,
                                          @RequestParam(name = "endDate") String endDate,
                                          @RequestParam(name = "reportType") String reportType,
                                          @RequestParam(name = "reportCode") String reportCode,
                                          @RequestParam(name = "filterBy") String filterBy) throws IOException, JarException, ParseException, JRException {

        ReportWrapperDTO reportWrapperDTO = reportService.generateDetailedCsvReport(startDate, endDate, reportType, reportCode);

        if (reportType.equals("EXCEL")) {

            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition",
                    String.format("attachment; filename=%s", "\"" + reportWrapperDTO.getReportFileNameWithExtension() + "\""));

            OutputStream out = response.getOutputStream();
            generateToExcel(response, reportWrapperDTO.getJasperPrint(), out);
        } else {
            //generatePDF(jasperPrint,response,String.format("attachment; filename=\"Branch Major Error Report.pdf\""));
        }
    }

    private void generateToExcel(HttpServletResponse response, JasperPrint jasperPrint, OutputStream out) throws JRException, IOException {
        JRCsvExporter exporter = new JRCsvExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

        exporter.setExporterOutput(new SimpleWriterExporterOutput(response.getOutputStream()));

        SimpleCsvReportConfiguration configuration = new SimpleCsvReportConfiguration();
        exporter.setConfiguration(configuration);
        exporter.exportReport();
    }

    private void generatePDF(JasperPrint jasperPrint,HttpServletResponse response,String reportName) {
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", reportName);

        try {
            OutputStream out = response.getOutputStream();
            jasperPrint.setPageWidth(842);
            jasperPrint.setPageHeight(595);
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);
        } catch (JRException e) {
            log.error("Error Filling Jasper Report : {} ", e.getMessage());
        } catch (IOException e) {
            log.error("Error Filling Jasper Report : {} ", e.getMessage());
        }
        removeBlankPage(jasperPrint.getPages());
    }

    private void removeBlankPage(List<JRPrintPage> pages) {
        for (Iterator<JRPrintPage> i = pages.iterator(); i.hasNext(); ) {
            JRPrintPage page = i.next();
            if (page.getElements().isEmpty())
                i.remove();
        }
    }
}
