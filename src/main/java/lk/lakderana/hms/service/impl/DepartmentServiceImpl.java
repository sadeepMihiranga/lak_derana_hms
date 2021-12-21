package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.dto.DepartmentDTO;
import lk.lakderana.hms.entity.TMsDepartment;
import lk.lakderana.hms.mapper.DepartmentMapper;
import lk.lakderana.hms.repository.DepartmentRepository;
import lk.lakderana.hms.service.DepartmentService;
import lk.lakderana.hms.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<DepartmentDTO> getAllDepartments() {

        final List<TMsDepartment> tMsDepartmentList = departmentRepository
                .findAllByDpmtStatus(Constants.STATUS_ACTIVE.getShortValue());

        if(tMsDepartmentList.isEmpty() || tMsDepartmentList == null)
            return Collections.emptyList();

        List<DepartmentDTO> departmentDTOList = new ArrayList<>();

        tMsDepartmentList.forEach(tMsDepartment -> {
            departmentDTOList.add(DepartmentMapper.INSTANCE.entityToDTO(tMsDepartment));
        });

        return departmentDTOList;
    }
}
