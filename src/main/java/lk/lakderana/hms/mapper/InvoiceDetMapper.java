package lk.lakderana.hms.mapper;

import lk.lakderana.hms.dto.InvoiceDetDTO;
import lk.lakderana.hms.entity.TTrInvoiceDet;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InvoiceDetMapper {

    InvoiceDetMapper INSTANCE = Mappers.getMapper(InvoiceDetMapper.class);

    @Mappings({
            @Mapping(source = "indtId", target = "invoiceDetId"),
            @Mapping(source = "invoice.invcId", target = "invoiceId"),
            @Mapping(source = "reservation.resvId", target = "reservationId"),
            @Mapping(source = "roomReservation.roreId", target = "roomReservationId"),
            @Mapping(source = "facilityReservation.fareId", target = "facilityReservationId"),
            @Mapping(source = "itemReservation.itrsId", target = "itemReservationId"),
            @Mapping(source = "indtReservedQuantity", target = "reservedQuantity"),
            @Mapping(source = "indtUnitPrice", target = "unitPrice"),
            @Mapping(source = "indtStatus", target = "status"),
            @Mapping(source = "branch.brnhId", target = "branchId"),
            @Mapping(source = "branch.brnhName", target = "branchName")
    })
    InvoiceDetDTO entityToDTO(TTrInvoiceDet entity);

    @InheritInverseConfiguration
    TTrInvoiceDet dtoToEntity(InvoiceDetDTO dto);
}
