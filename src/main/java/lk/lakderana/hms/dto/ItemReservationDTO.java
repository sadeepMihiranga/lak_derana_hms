package lk.lakderana.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemReservationDTO {

    private Long itemReservationId;
    private ItemDTO item;
    private Short status;
    private BigDecimal quantity;
}
