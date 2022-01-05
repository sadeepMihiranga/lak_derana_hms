package lk.lakderana.hms.mapper;

import lk.lakderana.hms.dto.InvoiceDTO;
import lk.lakderana.hms.entity.TTrInvoice;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InvoiceMapper {

    InvoiceMapper INSTANCE = Mappers.getMapper(InvoiceMapper.class);

    @Mappings({
            @Mapping(source = "invcId", target = "invoiceId"),
            @Mapping(source = "invcNumber", target = "invoiceNumber"),
            @Mapping(source = "invcDescription", target = "description"),
            @Mapping(source = "invcGrossAmount", target = "grossAmount"),
            @Mapping(source = "invcNetAmount", target = "netAmount"),
            @Mapping(source = "invcDiscountAmount", target = "discountAmount"),
            @Mapping(source = "invcTaxAmount", target = "taxAmount"),
            @Mapping(source = "invcStatus", target = "status"),
            @Mapping(source = "branch.brnhId", target = "branchId"),
            @Mapping(source = "branch.brnhName", target = "branchName")
    })
    InvoiceDTO entityToDTO(TTrInvoice entity);

    @InheritInverseConfiguration
    TTrInvoice dtoToEntity(InvoiceDTO dto);
}
