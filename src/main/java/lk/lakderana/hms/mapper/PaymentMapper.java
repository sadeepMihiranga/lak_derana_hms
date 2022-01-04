package lk.lakderana.hms.mapper;

import lk.lakderana.hms.dto.PaymentDTO;
import lk.lakderana.hms.entity.TTrPayment;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentMapper {

    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    @Mappings({
            @Mapping(source = "paytId", target = "paymentId"),
            @Mapping(source = "reservation.resvId", target = "reservationId"),
            @Mapping(source = "invoice.invcId", target = "invoiceId"),
            @Mapping(source = "invoice.invcNumber", target = "invoiceNumber"),
            @Mapping(source = "paytDescription", target = "description"),
            @Mapping(source = "paytType", target = "paymentType"),
            @Mapping(source = "paytAmount", target = "amount"),
            @Mapping(source = "paytStatus", target = "status"),
            @Mapping(source = "branch.brnhId", target = "branchId"),
            @Mapping(source = "branch.brnhName", target = "branchName")
    })
    PaymentDTO entityToDTO(TTrPayment entity);

    @InheritInverseConfiguration
    TTrPayment dtoToEntity(PaymentDTO dto);
}
