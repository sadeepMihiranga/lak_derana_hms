package lk.lakderana.hms.mapper;

import lk.lakderana.hms.dto.FacilityDTO;
import lk.lakderana.hms.entity.TMsFacility;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FacilityMapper {

    FacilityMapper INSTANCE = Mappers.getMapper(FacilityMapper.class);

    @Mappings({
            @Mapping(source = "fcltId", target = "facilityId"),
            @Mapping(source = "fcltName", target = "facilityName"),
            @Mapping(source = "fcltDescription", target = "description"),
            @Mapping(source = "fcltType", target = "facilityType"),
            @Mapping(source = "fcltPrice", target = "price"),
            @Mapping(source = "facltUom", target = "uom"),
            @Mapping(source = "branch.brnhId", target = "branchId"),
            @Mapping(source = "branch.brnhName", target = "branchName"),
            @Mapping(source = "facltStatus", target = "status")
    })
    FacilityDTO entityToDTO(TMsFacility entity);

    @InheritInverseConfiguration
    TMsFacility dtoToEntity(FacilityDTO dto);
}
