package lk.lakderana.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO implements Paginated {

    private Long itemId;
    @NotNull(message = "Item Name is required")
    private String name;
    @NotNull(message = "Item Type is required")
    private String itemTypeCode;
    private String itemTypeName;
    @NotNull(message = "Item Price is required")
    private BigDecimal price;
    @NotNull(message = "Item Units of Measurement is required")
    private String uom;
    private String uomName;
    private Short status;
    private Long branchId;
    private String branchName;
    private BigDecimal quantity;
}
