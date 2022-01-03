package lk.lakderana.hms.mapper;

import lk.lakderana.hms.dto.ReportHistoryDTO;
import lk.lakderana.hms.entity.TTrReportHistory;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReportHistoryMapper {

    ReportHistoryMapper INSTANCE = Mappers.getMapper(ReportHistoryMapper.class);

    @Mappings({
            @Mapping(source = "rphtId", target = "reportHistoryId"),
            @Mapping(source = "reportType.rptpCode", target = "reportType"),
            @Mapping(source = "reportType.rptpDisplayName", target = "reportDisplayName"),
            @Mapping(source = "rphtStatus", target = "status"),
            @Mapping(source = "rphtFromDate", target = "fromDate"),
            @Mapping(source = "rphtToDate", target = "toDate"),
            @Mapping(source = "createdDate", target = "createdDate"),
            @Mapping(source = "lastModDate", target = "lastUpdatedDate"),
            @Mapping(source = "createdUserCode", target = "createdUserCode"),
            @Mapping(source = "lastModUserCode", target = "lastUpdatedUserCode"),
            @Mapping(source = "branch.brnhId", target = "branchId")
    })
    ReportHistoryDTO entityToDTO(TTrReportHistory entity);

    @InheritInverseConfiguration
    TTrReportHistory dtoToEntity(ReportHistoryDTO dto);
}
