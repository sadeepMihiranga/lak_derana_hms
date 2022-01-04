package lk.lakderana.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String paymentType;
    private BigDecimal amount;
    private Short status;
    private String branchId;
    private String branchName;
}
