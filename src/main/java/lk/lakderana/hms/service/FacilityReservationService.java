package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.FacilityDTO;
import lk.lakderana.hms.dto.FacilityReservationDTO;

import java.util.List;

public interface FacilityReservationService {

    FacilityReservationDTO reserveFacility(Long reservationId, FacilityDTO facilityDTO);

    Boolean cancelFacilityReservationByReservation(Long reservationId);

    List<FacilityReservationDTO> getFacilityReservationsByReservation(Long reservationId);
}
