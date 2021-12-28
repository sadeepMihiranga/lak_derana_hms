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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static final String FACILITIES = "FCLTP";
    private static final String MEASUREMENTS_UNITS = "UOFMS";

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
    public Map<String, String> getDropDownCodes() {

        Map<String, String> dropDownCodes = new HashMap<>();

        dropDownCodes.put("BRANCHES", BRANCHES);
        dropDownCodes.put("DEPARTMENTS", DEPARTMENTS);
        dropDownCodes.put("PERMISSIONS", PERMISSIONS);
        dropDownCodes.put("ROLES", ROLES);
        dropDownCodes.put("CUSTOMERS", CUSTOMERS);
        dropDownCodes.put("EMPLOYEES", EMPLOYEES);
        dropDownCodes.put("ROOM_TYPES", ROOM_TYPES);
        dropDownCodes.put("ROOM_CATEGORIES", ROOM_CATEGORIES);
        dropDownCodes.put("FACILITIES", FACILITIES);
        dropDownCodes.put("MEASUREMENTS_UNITS", MEASUREMENTS_UNITS);

        return dropDownCodes;
    }

    @Override
    public List<DropDownDTO> getDropDownByCode(String code) {

        if(Strings.isNullOrEmpty(code))
            throw new NoRequiredInfoException("Code is required");

        List<DropDownDTO> downDTOList = new ArrayList<>();

        switch (code) {
            case BRANCHES :
                List<DropDownDTO> branchList = downDTOList;
                branchService.getAllBranches().forEach(branchDTO -> {
                    branchList.add(new DropDownDTO(
                            branchDTO.getBranchId().toString(),
                            branchDTO.getMame(),
                            null,
                            branchDTO.getStatus()));
                });
                break;
            case DEPARTMENTS :
                List<DropDownDTO> departmentList = downDTOList;
                departmentService.getAllDepartments().forEach(departmentDTO -> {
                    departmentList.add(new DropDownDTO(
                            departmentDTO.getDepartmentCode(),
                            departmentDTO.getMame(),
                            null,
                            departmentDTO.getStatus()));
                });
                break;
            case PERMISSIONS :
                List<DropDownDTO> permissionList = downDTOList;
                functionRepository.findAll().forEach(tMsFunction -> {
                    permissionList.add(new DropDownDTO(
                            tMsFunction.getFuncId(),
                            tMsFunction.getDunsDescription(),
                            null,
                            tMsFunction.getFuncStatus()));
                });
                break;
            case ROLES :
                List<DropDownDTO> roleList = downDTOList;
                roleRepository.findAll().forEach(tMsRole -> {
                    roleList.add(new DropDownDTO(
                            tMsRole.getRoleId().toString(),
                            tMsRole.getRoleName(),
                            tMsRole.getRoleDescription(),
                            tMsRole.getRoleStatus()));
                });
            case CUSTOMERS :
                List<DropDownDTO> customerList = downDTOList;
                partyService.getPartyListByType(CUSTOMERS).forEach(partyDTO -> {
                    customerList.add(new DropDownDTO(
                            partyDTO.getPartyCode(),
                            partyDTO.getName(),
                            null,
                            null
                    ));
                });
                break;
            case EMPLOYEES :
                List<DropDownDTO> roomList = downDTOList;
                partyService.getPartyListByType(EMPLOYEES).forEach(partyDTO -> {
                    roomList.add(new DropDownDTO(
                            partyDTO.getPartyCode(),
                            partyDTO.getName(),
                            null,
                            null
                    ));
                });
                break;
            case ROOM_TYPES :
                downDTOList = populateFromCommonReference(ROOM_TYPES);
                break;
            case ROOM_CATEGORIES :
                downDTOList = populateFromCommonReference(ROOM_CATEGORIES);
                break;
            case FACILITIES :
                downDTOList = populateFromCommonReference(FACILITIES);
                break;
            case MEASUREMENTS_UNITS :
                downDTOList = populateFromCommonReference(MEASUREMENTS_UNITS);
                break;
            default:
                throw new InvalidDataException("Requested Dropdown Code is invalid");
        }

        return downDTOList;
    }

    private List<DropDownDTO> populateFromCommonReference(String code) {

        List<DropDownDTO> downDTOList = new ArrayList<>();

        commonReferenceService.getAllByCmrtCode(code).forEach(commonReferenceDTO -> {
            downDTOList.add(new DropDownDTO(
                    commonReferenceDTO.getCmrfCode(),
                    commonReferenceDTO.getDescription(),
                    null,
                    null
            ));
        });

        return downDTOList;
    }
}
