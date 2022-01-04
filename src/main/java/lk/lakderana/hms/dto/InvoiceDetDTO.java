package lk.lakderana.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetDTO {

    private Long invoiceDetId;
    private Long invoiceId;
    private Long reservationId;
    private Long roomReservationId;
    private Long facilityReservationId;
    private Long itemReservationId;
    private Integer reservedQuantity;
    private BigDecimal unitPrice;
    private Short status;
    private String branchId;
    private String branchName;
}
