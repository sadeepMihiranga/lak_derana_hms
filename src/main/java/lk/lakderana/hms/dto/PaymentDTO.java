package lk.lakderana.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO implements Paginated {

    private Long paymentId;
    private Long reservationId;
    private Long invoiceId;
    private String invoiceNumber;
    private String description;
    @NotBlank(message = "Payment Type is required")
    private String paymentType;
    private BigDecimal amount;
    private Short status;
    private Long branchId;
    private String branchName;

    public PaymentDTO(String paymentType, BigDecimal amount) {
        this.paymentType = paymentType;
        this.amount = amount;
    }
}
