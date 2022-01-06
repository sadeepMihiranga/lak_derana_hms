package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.InvoiceDTO;

public interface InvoiceService {

    InvoiceDTO createInvoice(Long reservationId, InvoiceDTO invoiceDTO);

    InvoiceDTO getInvoiceDataByReservation(Long reservationId);
}
