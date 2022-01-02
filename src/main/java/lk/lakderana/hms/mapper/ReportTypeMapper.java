package lk.lakderana.hms.mapper;

import lk.lakderana.hms.dto.ReportTypeDTO;
import lk.lakderana.hms.entity.TRfReportType;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReportTypeMapper {

    ReportTypeMapper INSTANCE = Mappers.getMapper(ReportTypeMapper.class);

    @Mappings({
            @Mapping(source = "rptpId", target = "reportTypeId"),
            @Mapping(source = "rptpCode", target = "reportTypeCode"),
            @Mapping(source = "rptpDisplayName", target = "displayName"),
            @Mapping(source = "rptpIcon", target = "icon"),
            @Mapping(source = "rptpColor", target = "color"),
            @Mapping(source = "rptpStatus", target = "status")
    })
    ReportTypeDTO entityToDTO(TRfReportType entity);

    @InheritInverseConfiguration
    TRfReportType dtoToEntity(ReportTypeDTO dto);
}
