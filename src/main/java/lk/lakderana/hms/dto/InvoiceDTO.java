package lk.lakderana.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {

    private Long invoiceId;
    private String invoiceNumber;
    private Long reservationId;
    private String description;
    private BigDecimal grossAmount;
    private BigDecimal netAmount;
    @Max(value = 100, message = "Discount Percentage should not be greater than 100")
    private BigDecimal discountPercentage;
    private BigDecimal discountAmount;
    @Max(value = 100, message = "Tax Percentage should not be greater than 100")
    private BigDecimal taxPercentage;
    private BigDecimal taxAmount;
    private Short status;
    private Long branchId;
    private String branchName;
    private List<InvoiceDetDTO> invoiceDetDTOList;
}
