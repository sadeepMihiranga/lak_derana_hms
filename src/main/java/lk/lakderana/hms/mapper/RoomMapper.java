package lk.lakderana.hms.mapper;

import lk.lakderana.hms.dto.RoomDTO;
import lk.lakderana.hms.entity.TMsRoom;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoomMapper {

    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

    @Mappings({
            @Mapping(source = "roomId", target = "roomId"),
            @Mapping(source = "roomType", target = "roomType"),
            @Mapping(source = "roomNo", target = "roomNo"),
            @Mapping(source = "roomDescription", target = "roomDescription"),
            @Mapping(source = "roomCategory", target = "roomCategory"),
            @Mapping(source = "roomPrice", target = "roomPrice"),
            @Mapping(source = "roomStatus", target = "status"),
            @Mapping(source = "branch.brnhId", target = "branchId")
    })
    RoomDTO entityToDTO(TMsRoom entity);

    @InheritInverseConfiguration
    TMsRoom dtoToEntity(RoomDTO dto);
}
