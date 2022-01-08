package lk.lakderana.hms.mapper;

import lk.lakderana.hms.dto.AttendanceDTO;
import lk.lakderana.hms.entity.TMsAttendance;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AttendanceMapper {

    AttendanceMapper INSTANCE = Mappers.getMapper(AttendanceMapper.class);

    @Mappings({
            @Mapping(source = "attnId", target = "attendanceId"),
            @Mapping(source = "party.prtyCode", target = "employeeCode"),
            @Mapping(source = "party.prtyName", target = "employeeName"),
            @Mapping(source = "attnDate", target = "attendanceDate"),
            @Mapping(source = "attnInTime", target = "inTime"),
            @Mapping(source = "attnOutTime", target = "outTime"),
            @Mapping(source = "attnStatus", target = "status"),
            @Mapping(source = "branch.brnhId", target = "branchId"),
            @Mapping(source = "branch.brnhName", target = "branchName")
    })
    AttendanceDTO entityToDTO(TMsAttendance entity);

    @InheritInverseConfiguration
    TMsAttendance dtoToEntity(AttendanceDTO dto);
}
