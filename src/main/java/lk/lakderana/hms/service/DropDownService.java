package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.DropDownDTO;

import java.util.List;

public interface DropDownService {

    List<DropDownDTO> getDropDownByCode(String code);
}
