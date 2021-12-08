package lk.lakderana.hms.mapper;

import lk.lakderana.hms.dto.UserDTO;
import lk.lakderana.hms.entity.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "username", target = "username"),
            @Mapping(source = "password", target = "password")
    })
    UserDTO entityToDTO(User entity);

    @InheritInverseConfiguration
    User dtoToEntity(UserDTO dto);
}
