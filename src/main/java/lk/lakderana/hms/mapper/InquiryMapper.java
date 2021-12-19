package lk.lakderana.hms.mapper;

import lk.lakderana.hms.dto.InquiryDTO;
import lk.lakderana.hms.entity.TRfInquiry;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InquiryMapper {

    InquiryMapper INSTANCE = Mappers.getMapper(InquiryMapper.class);

    @Mappings({
            @Mapping(source = "inqrId", target = "inquiryId"),
            @Mapping(source = "inqrCustomerCode", target = "partyCode"),
            @Mapping(source = "inqrDateTime", target = "inquiryDateTime"),
            @Mapping(source = "inqrRemarks", target = "remarks"),
            @Mapping(source = "inqrStatus", target = "inquiryStatus"),
            @Mapping(source = "branch.brnhId", target = "branchId"),
            @Mapping(source = "inqrCustomerName", target = "customerName"),
            @Mapping(source = "inqrCustomerContactNo", target = "customerContactNo")
    })
    InquiryDTO entityToDTO(TRfInquiry entity);

    @InheritInverseConfiguration
    TRfInquiry dtoToEntity(InquiryDTO dto);
}
