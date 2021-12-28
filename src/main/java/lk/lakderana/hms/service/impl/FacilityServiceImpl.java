package lk.lakderana.hms.service.impl;

import com.google.common.base.Strings;
import lk.lakderana.hms.config.EntityValidator;
import lk.lakderana.hms.dto.CommonReferenceDTO;
import lk.lakderana.hms.dto.FacilityDTO;
import lk.lakderana.hms.dto.PaginatedEntity;
import lk.lakderana.hms.dto.RoomDTO;
import lk.lakderana.hms.entity.TMsFacility;
import lk.lakderana.hms.entity.TMsRoom;
import lk.lakderana.hms.exception.OperationException;
import lk.lakderana.hms.exception.TransactionConflictException;
import lk.lakderana.hms.mapper.FacilityMapper;
import lk.lakderana.hms.mapper.RoomMapper;
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

import static lk.lakderana.hms.util.CommonReferenceTypeCodes.*;
import static lk.lakderana.hms.util.Constants.*;

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
