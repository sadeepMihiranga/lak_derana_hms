package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.config.EntityValidator;
import lk.lakderana.hms.dto.InvoiceDTO;
import lk.lakderana.hms.service.InvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InvoiceServiceImpl extends EntityValidator implements InvoiceService {

    @Override
    public InvoiceDTO createInvoice(InvoiceDTO invoiceDTO) {
        return null;
    }
}
