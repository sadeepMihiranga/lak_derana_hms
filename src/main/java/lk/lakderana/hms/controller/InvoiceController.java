package lk.lakderana.hms.controller;

import lk.lakderana.hms.dto.InvoiceDTO;
import lk.lakderana.hms.response.SuccessResponse;
import lk.lakderana.hms.response.SuccessResponseHandler;
import lk.lakderana.hms.service.InvoiceService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@CrossOrigin
@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/reservation/{reservationId}")
    public ResponseEntity<SuccessResponse> createInvoice(@PathVariable("reservationId") Long reservationId,
            @RequestBody InvoiceDTO invoiceDTO) {
        return SuccessResponseHandler.generateResponse(invoiceService.createInvoice(reservationId, invoiceDTO));
    }

    @GetMapping(value = "/reservation/{reservationId}/print")
    public void generateInvoice(HttpServletResponse response, @PathVariable("reservationId") Long reservationId) throws IOException, JRException {
        JasperPrint jasperPrint = null;

        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", String.format("attachment; filename=%s", "\"invoice_" + reservationId + ".pdf\""));

        OutputStream out = response.getOutputStream();

        jasperPrint = invoiceService.generateInvoicePrint(reservationId);

        JasperExportManager.exportReportToPdfStream(jasperPrint, out);
    }

    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<SuccessResponse> getInvoiceDataByReservation(@PathVariable("reservationId") Long reservationId) {
        return SuccessResponseHandler.generateResponse(invoiceService.getInvoiceDataByReservation(reservationId));
    }

    @GetMapping(path = "/search")
        public ResponseEntity<SuccessResponse> invoicePaginatedSearch(@RequestParam(name = "reservationId", required = false) Long reservationId,
                                                                  @RequestParam(name = "invoiceNumber", required = false) String invoiceNumber,
                                                                  @RequestParam(name = "status", required = false) Short status,
                                                                  @RequestParam(name = "page", required = true) Integer page,
                                                                  @RequestParam(name = "size", required = true) Integer size) {
        return SuccessResponseHandler.generateResponse(invoiceService
                .invoicePaginatedSearch(reservationId, invoiceNumber, status, page, size));
    }
}
