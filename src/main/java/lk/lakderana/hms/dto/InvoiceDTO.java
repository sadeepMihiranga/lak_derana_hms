package lk.lakderana.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {

    private Long invoiceId;
    private String invoiceNumber;
    private String description;
    private BigDecimal grossAmount;
    private BigDecimal netAmount;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private Short status;
    private String branchId;
    private String branchName;
}
