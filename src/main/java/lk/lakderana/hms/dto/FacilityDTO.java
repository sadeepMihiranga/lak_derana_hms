package lk.lakderana.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDTO implements Paginated {

    private Long facilityId;
    @NotBlank(message = "Facility Name is required")
    private String facilityName;
    private String description;
    @NotBlank(message = "Facility Type is required")
    private String facilityType;
    private String facilityTypeName;
    private BigDecimal price;
    private String uom;
    private String uomName;
    private Short status;
    private Long branchId;
    private String branchName;
    private BigDecimal quantity;
    private Integer maxCapacity;
}
