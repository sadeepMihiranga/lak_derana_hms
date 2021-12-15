package lk.lakderana.hms.mapper;

import lk.lakderana.hms.dto.BranchDTO;
import lk.lakderana.hms.dto.DepartmentDTO;
import lk.lakderana.hms.entity.TMsDepartment;
import lk.lakderana.hms.entity.TRfBranch;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BranchMapper {

    BranchMapper INSTANCE = Mappers.getMapper(BranchMapper.class);

    @Mappings({
            @Mapping(source = "brnhId", target = "branchId"),
            @Mapping(source = "brnhName", target = "mame"),
            @Mapping(source = "brnhDescription", target = "description"),
            @Mapping(source = "brnhStatus", target = "status")
    })
    BranchDTO entityToDTO(TRfBranch entity);

    @InheritInverseConfiguration
    TRfBranch dtoToEntity(BranchDTO dto);
}
