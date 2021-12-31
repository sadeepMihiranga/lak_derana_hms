package lk.lakderana.hms.service.impl;

import com.google.common.base.Strings;
import lk.lakderana.hms.config.EntityValidator;
import lk.lakderana.hms.dto.CommonReferenceDTO;
import lk.lakderana.hms.dto.FacilityDTO;
import lk.lakderana.hms.dto.PaginatedEntity;
import lk.lakderana.hms.entity.TMsFacility;
import lk.lakderana.hms.exception.DataNotFoundException;
import lk.lakderana.hms.exception.NoRequiredInfoException;
import lk.lakderana.hms.exception.OperationException;
import lk.lakderana.hms.exception.TransactionConflictException;
import lk.lakderana.hms.mapper.FacilityMapper;
import lk.lakderana.hms.repository.FacilityRepository;
import lk.lakderana.hms.service.CommonReferenceService;
import lk.lakderana.hms.service.FacilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static lk.lakderana.hms.util.constant.CommonReferenceTypeCodes.*;
import static lk.lakderana.hms.util.constant.Constants.*;

@Slf4j
@Service
public class FacilityServiceImpl extends EntityValidator implements FacilityService {

    private final CommonReferenceService commonReferenceService;

    private final FacilityRepository facilityRepository;

    public FacilityServiceImpl(CommonReferenceService commonReferenceService,
                               FacilityRepository facilityRepository) {
        this.commonReferenceService = commonReferenceService;
        this.facilityRepository = facilityRepository;
    }

    @Override
    public FacilityDTO createFacility(FacilityDTO facilityDTO) {

        validateEntity(facilityDTO);
        validateReferenceData(facilityDTO);

        facilityDTO.setStatus(STATUS_ACTIVE.getShortValue());
        facilityDTO.setBranchId(captureBranchIds().get(0));
        final TMsFacility tMsFacility = FacilityMapper.INSTANCE.dtoToEntity(facilityDTO);

        return FacilityMapper.INSTANCE.entityToDTO(persistEntity(tMsFacility));
    }

    @Override
    public PaginatedEntity facilityPaginatedSearch(String facilityName, String facilityType, Integer page, Integer size) {

        PaginatedEntity paginatedFacilityList = null;
        List<FacilityDTO> facilityList = null;

        validatePaginateIndexes(page, size);

        Page<TMsFacility> tMsFacilityPage = facilityRepository
                .searchFacility(facilityName, facilityType, captureBranchIds(), PageRequest.of(page - 1, size));

        if (tMsFacilityPage.getSize() == 0)
            return null;

        paginatedFacilityList = new PaginatedEntity();
        facilityList = new ArrayList<>();

        for (TMsFacility tMsFacility : tMsFacilityPage) {

            final FacilityDTO facilityDTO = FacilityMapper.INSTANCE.entityToDTO(tMsFacility);
            setReferenceData(tMsFacility, facilityDTO);

            facilityList.add(facilityDTO);
        }

        paginatedFacilityList.setTotalNoOfPages(tMsFacilityPage.getTotalPages());
        paginatedFacilityList.setTotalNoOfRecords(tMsFacilityPage.getTotalElements());
        paginatedFacilityList.setEntities(facilityList);

        return paginatedFacilityList;
    }

    @Override
    public FacilityDTO updateFacility(Long facilityId, FacilityDTO facilityDTO) {

        validateEntity(facilityDTO);
        validateReferenceData(facilityDTO);

        final TMsFacility tMsFacility = validateByFacilityId(facilityId);

        tMsFacility.setFacltUom(facilityDTO.getUom());
        tMsFacility.setFcltDescription(facilityDTO.getDescription());
        tMsFacility.setFcltName(facilityDTO.getFacilityName());
        tMsFacility.setFcltPrice(facilityDTO.getPrice());
        tMsFacility.setFcltType(facilityDTO.getFacilityType());

        return FacilityMapper.INSTANCE.entityToDTO(persistEntity(tMsFacility));
    }

    @Override
    public Boolean removeFacility(Long facilityId) {

        final TMsFacility tMsFacility = validateByFacilityId(facilityId);

        tMsFacility.setFacltStatus(STATUS_INACTIVE.getShortValue());

        persistEntity(tMsFacility);

        return true;
    }

    private TMsFacility validateByFacilityId(Long facilityId) {

        if(facilityId == null)
            throw new NoRequiredInfoException("Facility Id is required");

        final TMsFacility tMsFacility = facilityRepository.findByFcltIdAndBranch_BrnhIdIn(facilityId, captureBranchIds());

        if(tMsFacility == null)
            throw new DataNotFoundException("Facility not found for the Id " + facilityId);

        return tMsFacility;
    }

    private void setReferenceData(TMsFacility tMsFacility, FacilityDTO facilityDTO) {

        if(tMsFacility.getBranch() != null)
            facilityDTO.setBranchName(tMsFacility.getBranch().getBrnhName());

        if(!Strings.isNullOrEmpty(facilityDTO.getFacilityType())) {
            final CommonReferenceDTO commonReferenceDTO = commonReferenceService
                    .getByCmrfCodeAndCmrtCode(FACILITY_TYPES.getValue(), facilityDTO.getFacilityType());

            facilityDTO.setFacilityTypeName(commonReferenceDTO.getDescription());
        }
    }

    private void validateReferenceData(FacilityDTO facilityDTO) {

        commonReferenceService
                .getByCmrfCodeAndCmrtCode(FACILITY_TYPES.getValue(), facilityDTO.getFacilityType());

        commonReferenceService
                .getByCmrfCodeAndCmrtCode(MEASUREMENT_TYPES.getValue(), facilityDTO.getUom());
    }

    private TMsFacility persistEntity(TMsFacility tMsFacility) {
        try {
            validateEntity(tMsFacility);
            return facilityRepository.save(tMsFacility);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new TransactionConflictException("Transaction Updated by Another User.");
        } catch (Exception e) {
            log.error("Error while persisting : " + e.getMessage());
            throw new OperationException(e.getMessage());
        }
    }
}
