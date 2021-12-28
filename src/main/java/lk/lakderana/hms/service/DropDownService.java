package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.DropDownDTO;

import java.util.List;
import java.util.Map;

public interface DropDownService {

    List<DropDownDTO> getDropDownByCode(String code);

    Map<String, String> getDropDownCodes();
}
