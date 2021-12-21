package lk.lakderana.hms.service.impl;

import com.google.common.base.Strings;
import lk.lakderana.hms.dto.DropDownDTO;
import lk.lakderana.hms.exception.InvalidDataException;
import lk.lakderana.hms.exception.NoRequiredInfoException;
import lk.lakderana.hms.repository.FunctionRepository;
import lk.lakderana.hms.repository.RoleRepository;
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
    private static final String PERMISSIONS = "PERMIS";
    private static final String ROLES = "ROLES";

    private final BranchService branchService;
    private final DepartmentService departmentService;

    private final FunctionRepository functionRepository;
    private final RoleRepository roleRepository;

    public DropDownServiceImpl(BranchService branchService,
                               DepartmentService departmentService,
                               FunctionRepository functionRepository,
                               RoleRepository roleRepository) {
        this.branchService = branchService;
        this.departmentService = departmentService;
        this.functionRepository = functionRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<DropDownDTO> getDropDownByCode(String code) {

        if(Strings.isNullOrEmpty(code))
            throw new NoRequiredInfoException("Code is required");

        List<DropDownDTO> downDTOList = new ArrayList<>();

        switch (code) {
            case BRANCHES :
                branchService.getAllBranches().forEach(branchDTO -> {
                    downDTOList.add(new DropDownDTO(branchDTO.getBranchId().toString(), branchDTO.getMame(), branchDTO.getStatus()));
                });
                break;
            case DEPARTMENTS :
                departmentService.getAllDepartments().forEach(departmentDTO -> {
                    downDTOList.add(new DropDownDTO(departmentDTO.getDepartmentCode(), departmentDTO.getMame(), departmentDTO.getStatus()));
                });
                break;
            case PERMISSIONS :
                functionRepository.findAll().forEach(tMsFunction -> {
                    downDTOList.add(new DropDownDTO(tMsFunction.getFuncId(), tMsFunction.getDunsDescription(), tMsFunction.getFuncStatus()));
                });
                break;
            case ROLES :
                roleRepository.findAll().forEach(tMsRole -> {
                    downDTOList.add(new DropDownDTO(tMsRole.getRoleId().toString(), tMsRole.getRoleName(), tMsRole.getRoleStatus()));
                });
                break;
            default:
                throw new InvalidDataException("Requested Dropdown Code is invalid");
        }

        return downDTOList;
    }
}
