package lk.lakderana.hms.mapper;

import lk.lakderana.hms.dto.PartyDTO;
import lk.lakderana.hms.entity.TMsParty;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PartyMapper {

    PartyMapper INSTANCE = Mappers.getMapper(PartyMapper.class);

    @Mappings({
            @Mapping(source = "prtyId", target = "partyId"),
            @Mapping(source = "prtyName", target = "name"),
            //@Mapping(source = "prtyFirstName", target = "firstName"),
            //@Mapping(source = "prtyLastName", target = "lastName"),
            @Mapping(source = "prtyDob", target = "dob"),
            @Mapping(source = "prtyAddress1", target = "address1"),
            @Mapping(source = "prtyAddress2", target = "address2"),
            @Mapping(source = "prtyAddress3", target = "address3"),
            @Mapping(source = "prtyGender", target = "gender"),
            @Mapping(source = "prtyNic", target = "nic"),
            @Mapping(source = "prtyPassport", target = "passport"),
            @Mapping(source = "prtyType", target = "type"),
            @Mapping(source = "department.dpmtCode", target = "departmentCode"),
            @Mapping(source = "branch.brnhId", target = "branchId"),
            @Mapping(source = "prtyManagedBy", target = "managedBy"),
            @Mapping(source = "prtyStatus", target = "status")
    })
    PartyDTO entityToDTO(TMsParty entity);

    @InheritInverseConfiguration
    TMsParty dtoToEntity(PartyDTO dto);
}
