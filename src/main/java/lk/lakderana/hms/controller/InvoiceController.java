package lk.lakderana.hms.controller;

import lk.lakderana.hms.dto.InvoiceDTO;
import lk.lakderana.hms.response.SuccessResponse;
import lk.lakderana.hms.response.SuccessResponseHandler;
import lk.lakderana.hms.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
