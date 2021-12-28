package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.FacilityDTO;
import lk.lakderana.hms.dto.PaginatedEntity;

public interface FacilityService {

    FacilityDTO createFacility(FacilityDTO facilityDTO);

    PaginatedEntity facilityPaginatedSearch(String facilityName, String facilityType, Integer page, Integer size);
}
