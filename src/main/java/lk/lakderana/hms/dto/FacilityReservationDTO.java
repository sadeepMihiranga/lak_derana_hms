package lk.lakderana.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityReservationDTO {

    private Long facilityReservationId;
    private FacilityDTO facility;
    private Short status;
}
