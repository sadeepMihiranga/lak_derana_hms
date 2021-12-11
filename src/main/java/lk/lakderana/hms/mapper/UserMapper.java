package lk.lakderana.hms.mapper;

import lk.lakderana.hms.dto.UserDTO;
import lk.lakderana.hms.entity.TMsUser;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings({
            @Mapping(source = "userId", target = "id"),
            @Mapping(source = "userFullName", target = "name"),
            @Mapping(source = "userUsername", target = "username"),
            @Mapping(source = "userPassword", target = "password")
    })
    UserDTO entityToDTO(TMsUser entity);

    @InheritInverseConfiguration
    TMsUser dtoToEntity(UserDTO dto);
}
