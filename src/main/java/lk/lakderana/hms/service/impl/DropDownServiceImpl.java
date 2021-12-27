package lk.lakderana.hms.service.impl;

import com.google.common.base.Strings;
import lk.lakderana.hms.dto.DropDownDTO;
import lk.lakderana.hms.exception.InvalidDataException;
import lk.lakderana.hms.exception.NoRequiredInfoException;
import lk.lakderana.hms.repository.FunctionRepository;
import lk.lakderana.hms.repository.RoleRepository;
import lk.lakderana.hms.service.*;
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
    private static final String CUSTOMERS = "CUSTM";
    private static final String EMPLOYEES = "EMPLY";
    private static final String ROOM_TYPES = "ROMTP";
    private static final String ROOM_CATEGORIES = "RMCAT";

    private final BranchService branchService;
    private final DepartmentService departmentService;
    private final PartyService partyService;
    private final CommonReferenceService commonReferenceService;

    private final FunctionRepository functionRepository;
    private final RoleRepository roleRepository;

    public DropDownServiceImpl(BranchService branchService,
                               DepartmentService departmentService,
                               PartyService partyService,
                               CommonReferenceService commonReferenceService,
                               FunctionRepository functionRepository,
                               RoleRepository roleRepository) {
        this.branchService = branchService;
        this.departmentService = departmentService;
        this.partyService = partyService;
        this.commonReferenceService = commonReferenceService;
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
                    downDTOList.add(new DropDownDTO(
                            branchDTO.getBranchId().toString(),
                            branchDTO.getMame(),
                            null,
                            branchDTO.getStatus()));
                });
                break;
            case DEPARTMENTS :
                departmentService.getAllDepartments().forEach(departmentDTO -> {
                    downDTOList.add(new DropDownDTO(
                            departmentDTO.getDepartmentCode(),
                            departmentDTO.getMame(),
                            null,
                            departmentDTO.getStatus()));
                });
                break;
            case PERMISSIONS :
                functionRepository.findAll().forEach(tMsFunction -> {
                    downDTOList.add(new DropDownDTO(
                            tMsFunction.getFuncId(),
                            tMsFunction.getDunsDescription(),
                            null,
                            tMsFunction.getFuncStatus()));
                });
                break;
            case ROLES :
                roleRepository.findAll().forEach(tMsRole -> {
                    downDTOList.add(new DropDownDTO(
                            tMsRole.getRoleId().toString(),
                            tMsRole.getRoleName(),
                            tMsRole.getRoleDescription(),
                            tMsRole.getRoleStatus()));
                });
            case CUSTOMERS :
                partyService.getPartyListByType(CUSTOMERS).forEach(partyDTO -> {
                    downDTOList.add(new DropDownDTO(
                            partyDTO.getPartyCode(),
                            partyDTO.getName(),
                            null,
                            null
                    ));
                });
                break;
            case EMPLOYEES :
                partyService.getPartyListByType(EMPLOYEES).forEach(partyDTO -> {
                    downDTOList.add(new DropDownDTO(
                            partyDTO.getPartyCode(),
                            partyDTO.getName(),
                            null,
                            null
                    ));
                });
                break;
            case ROOM_TYPES :
                commonReferenceService.getAllByCmrtCode(ROOM_TYPES).forEach(commonReferenceDTO -> {
                    downDTOList.add(new DropDownDTO(
                            commonReferenceDTO.getCmrfCode(),
                            commonReferenceDTO.getDescription(),
                            null,
                            null
                    ));
                });
                break;
            case ROOM_CATEGORIES :
                commonReferenceService.getAllByCmrtCode(ROOM_CATEGORIES).forEach(commonReferenceDTO -> {
                    downDTOList.add(new DropDownDTO(
                            commonReferenceDTO.getCmrfCode(),
                            commonReferenceDTO.getDescription(),
                            null,
                            null
                    ));
                });
                break;
            default:
                throw new InvalidDataException("Requested Dropdown Code is invalid");
        }

        return downDTOList;
    }
}
