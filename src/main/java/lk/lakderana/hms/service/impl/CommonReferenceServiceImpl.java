package lk.lakderana.hms.service.impl;

import com.google.common.base.Strings;
import lk.lakderana.hms.dto.CommonReferenceDTO;
import lk.lakderana.hms.dto.DropDownDTO;
import lk.lakderana.hms.entity.TRfCommonReference;
import lk.lakderana.hms.exception.DataNotFoundException;
import lk.lakderana.hms.exception.NoRequiredInfoException;
import lk.lakderana.hms.mapper.CommonReferenceMapper;
import lk.lakderana.hms.repository.CommonReferenceRepository;
import lk.lakderana.hms.service.CommonReferenceService;
import lk.lakderana.hms.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class CommonReferenceServiceImpl implements CommonReferenceService {

    private final CommonReferenceRepository commonReferenceRepository;

    public CommonReferenceServiceImpl(CommonReferenceRepository commonReferenceRepository) {
        this.commonReferenceRepository = commonReferenceRepository;
    }

    @Override
    public List<CommonReferenceDTO> getAllByCmrtCode(String cmrtCode) {

        if(Strings.isNullOrEmpty(cmrtCode))
            throw new NoRequiredInfoException("Cmrt Code is required");

        final List<TRfCommonReference> tRfCommonReferenceList = commonReferenceRepository
                .findAllByReferenceTypeCmrtCodeAndCmrfStatus(cmrtCode, Constants.STATUS_ACTIVE.getShortValue());

        if(tRfCommonReferenceList.isEmpty() || tRfCommonReferenceList == null)
            return Collections.emptyList();

        List<CommonReferenceDTO> commonReferenceDTOList = new ArrayList<>();

        tRfCommonReferenceList.forEach(tRfCommonReference -> {
            commonReferenceDTOList.add(CommonReferenceMapper.INSTANCE.entityToDTO(tRfCommonReference));
        });

        return commonReferenceDTOList;
    }

    @Override
    public CommonReferenceDTO getByCmrfCodeAndCmrtCode(String cmrtCode, String cmrfCode) {

        if(Strings.isNullOrEmpty(cmrfCode))
            throw new NoRequiredInfoException("Cmrf Code is required");

        if(Strings.isNullOrEmpty(cmrtCode))
            throw new NoRequiredInfoException("Cmrt Code is required");

        final TRfCommonReference tRfCommonReference = commonReferenceRepository
                .findByCmrtCodeAndCmrfCode(cmrtCode, cmrfCode, Constants.STATUS_ACTIVE.getShortValue());

        if(tRfCommonReference == null)
            throw new DataNotFoundException("Reference not found for Cmrf Code : " + cmrfCode + " and Cmrt Code : " + cmrtCode);

        return CommonReferenceMapper.INSTANCE.entityToDTO(tRfCommonReference);
    }
}
