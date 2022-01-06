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
    private String invoiceNumber;
    private String description;
    private String cancelReason;
    @NotBlank(message = "Payment Method is required")
    private String paymentMethod;
    private String paymentMethodName;
    private BigDecimal amount;
    private Short status;
    private Long branchId;
    private String branchName;

    public PaymentDTO(String paymentType, BigDecimal amount) {
        this.paymentMethod = paymentType;
        this.amount = amount;
    }
}
