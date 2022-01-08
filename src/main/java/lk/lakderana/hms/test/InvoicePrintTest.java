package lk.lakderana.hms.test;

import lk.lakderana.hms.LakDeranaHmsApplication;
import lk.lakderana.hms.entity.TMsRoleFunction;
import lk.lakderana.hms.service.InvoiceService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LakDeranaHmsApplication.class)
public class InvoicePrintTest {

    @Autowired
    private InvoiceService invoiceService;

    @Ignore
    @Test
    public void getPermissionsByRoleTest() {
        invoiceService.getInvoiceDataByReservation(27l);
    }
}
