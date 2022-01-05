package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.FacilityDTO;
import lk.lakderana.hms.dto.PaginatedEntity;

import java.util.List;

public interface FacilityService {

    FacilityDTO createFacility(FacilityDTO facilityDTO);

    PaginatedEntity facilityPaginatedSearch(String facilityName, String facilityType, Short status, Integer page, Integer size);

    FacilityDTO updateFacility(Long facilityId, FacilityDTO facilityDTO);

    Boolean removeFacility(Long facilityId);

    List<FacilityDTO> getAllFacilities();

    FacilityDTO getFacilityById(Long facilityId);
}
