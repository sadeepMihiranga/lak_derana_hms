package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.InvoiceDTO;
import lk.lakderana.hms.dto.InvoicePrintDTO;
import net.sf.jasperreports.engine.JasperPrint;

import java.io.IOException;

public interface InvoiceService {

    InvoiceDTO createInvoice(Long reservationId, InvoiceDTO invoiceDTO);

    InvoicePrintDTO getInvoiceDataByReservation(Long reservationId);

    JasperPrint generateInvoicePrint(Long reservationId) throws IOException;
}
