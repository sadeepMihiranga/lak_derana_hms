package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.CommonReferenceDTO;
import lk.lakderana.hms.dto.DropDownDTO;

import java.util.List;

public interface CommonReferenceService {

    List<CommonReferenceDTO> getAllByCmrtCode(String cmrtCode);

    CommonReferenceDTO getByCmrfCodeAndCmrtCode(String cmrtCode, String cmrfCode);
}
