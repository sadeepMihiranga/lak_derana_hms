package lk.lakderana.hms.mapper;

import lk.lakderana.hms.dto.ReservationDTO;
import lk.lakderana.hms.entity.TMsReservation;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReservationMapper {

    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

    @Mappings({
            @Mapping(source = "resvId", target = "reservationId"),
            @Mapping(source = "inquiry.inqrId", target = "inquiryId"),
            @Mapping(source = "resvCheckInDateTime", target = "checkInDateTime"),
            @Mapping(source = "resvCheckOutDateTime", target = "checkOutDateTime"),
            @Mapping(source = "resvRemarks", target = "remarks"),
            @Mapping(source = "branch.brnhId", target = "branchId"),
            @Mapping(source = "branch.brnhName", target = "branchName"),
            @Mapping(source = "resvNoOfPersons", target = "noOfPersons"),
            @Mapping(source = "resvStatus", target = "status"),
            @Mapping(source = "lastModDate", target = "lastUpdatedDate"),
            @Mapping(source = "lastModUserCode", target = "lastUpdatedUserCode")
    })
    ReservationDTO entityToDTO(TMsReservation entity);

    @InheritInverseConfiguration
    TMsReservation dtoToEntity(ReservationDTO dto);
}
