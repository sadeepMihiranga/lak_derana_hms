package lk.lakderana.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityReservationDTO {

    private Long facilityReservationId;
    private FacilityDTO facility;
    private Short status;
    private BigDecimal quantity;
}
