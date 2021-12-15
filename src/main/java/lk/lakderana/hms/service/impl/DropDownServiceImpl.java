package lk.lakderana.hms.service.impl;

import com.google.common.base.Strings;
import lk.lakderana.hms.dto.DropDownDTO;
import lk.lakderana.hms.exception.NoRequiredInfoException;
import lk.lakderana.hms.service.BranchService;
import lk.lakderana.hms.service.DepartmentService;
import lk.lakderana.hms.service.DropDownService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DropDownServiceImpl implements DropDownService {

    private static final String BRANCHES = "BRANCH";
    private static final String DEPARTMENTS = "DEPART";

    private final BranchService branchService;
    private final DepartmentService departmentService;

    public DropDownServiceImpl(BranchService branchService,
                               DepartmentService departmentService) {
        this.branchService = branchService;
        this.departmentService = departmentService;
    }

    @Override
    public List<DropDownDTO> getDropDownByCode(String code) {

        if(Strings.isNullOrEmpty(code))
            throw new NoRequiredInfoException("Code is required");

        List<DropDownDTO> downDTOList = new ArrayList<>();

        if(code.equals(BRANCHES)) {
            branchService.getAllBranches().forEach(branchDTO -> {
                downDTOList.add(new DropDownDTO(branchDTO.getBranchId().toString(), branchDTO.getMame(), branchDTO.getStatus()));
            });
        } else if(code.equals(DEPARTMENTS)) {
            departmentService.getAllDepartments().forEach(departmentDTO -> {
                downDTOList.add(new DropDownDTO(departmentDTO.getDepartmentCode(), departmentDTO.getMame(), departmentDTO.getStatus()));
            });
        }

        return downDTOList;
    }
}
