package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.FacilityDTO;
import lk.lakderana.hms.dto.FacilityReservationDTO;

public interface FacilityReservationService {

    FacilityReservationDTO reserveFacility(Long reservationId, FacilityDTO facilityDTO);
}
