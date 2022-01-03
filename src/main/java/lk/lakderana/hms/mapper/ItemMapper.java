package lk.lakderana.hms.mapper;

import lk.lakderana.hms.dto.ItemDTO;
import lk.lakderana.hms.entity.TMsItem;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItemMapper {

    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mappings({
            @Mapping(source = "itemId", target = "itemId"),
            @Mapping(source = "itemName", target = "name"),
            @Mapping(source = "itemTypeCode", target = "itemTypeCode"),
            @Mapping(source = "itemPrice", target = "price"),
            @Mapping(source = "itemUom", target = "uom"),
            @Mapping(source = "itemStatus", target = "status"),
            @Mapping(source = "branch.brnhId", target = "branchId"),
            @Mapping(source = "branch.brnhName", target = "branchName")
    })
    ItemDTO entityToDTO(TMsItem entity);

    @InheritInverseConfiguration
    TMsItem dtoToEntity(ItemDTO dto);
}
