package lk.lakderana.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

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
    private BigDecimal amount;
    private Short status;
    private Long branchId;
    private String branchName;
    private ReservationDTO reservationDTO;
    private List<RoomReservationDTO> roomReservationDTOList;
    private List<FacilityReservationDTO> facilityReservationDTOList;
    private List<ItemReservationDTO> itemReservationDTOList;
}
