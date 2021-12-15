package lk.lakderana.hms.mapper;

import lk.lakderana.hms.dto.DepartmentDTO;
import lk.lakderana.hms.dto.PartyContactDTO;
import lk.lakderana.hms.entity.TMsDepartment;
import lk.lakderana.hms.entity.TMsPartyContact;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DepartmentMapper {

    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

    @Mappings({
            @Mapping(source = "dpmtCode", target = "departmentCode"),
            @Mapping(source = "dpmtName", target = "mame"),
            @Mapping(source = "dpmtDescription", target = "description"),
            @Mapping(source = "dpmtStatus", target = "status")
    })
    DepartmentDTO entityToDTO(TMsDepartment entity);

    @InheritInverseConfiguration
    TMsDepartment dtoToEntity(DepartmentDTO dto);
}
