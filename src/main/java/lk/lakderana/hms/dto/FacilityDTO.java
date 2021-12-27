package lk.lakderana.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityDTO {

    private Long facilityId;
    private String facilityName;
    private String description;
    private String facilityType;
    private BigDecimal price;
    private Integer uom;
    private Short status;
}
