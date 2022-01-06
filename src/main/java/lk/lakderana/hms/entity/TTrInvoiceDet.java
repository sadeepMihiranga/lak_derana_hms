package lk.lakderana.hms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="T_TR_INVOICE_DET")
public class TTrInvoiceDet extends AuditModel {

    @javax.persistence.Id
    @GeneratedValue(generator = "InvoiceDetSequence")
    @SequenceGenerator(name = "InvoiceDetSequence", schema = "LAKDERANA_BASE", sequenceName = "\"T_TR_INVOICE_DET_INDT_ID_seq\"", allocationSize = 1)
    @Column(name = "INDT_ID")
    private Long indtId;

    @JoinColumn(name = "INDT_INVOICE_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TTrInvoice invoice;

    @JoinColumn(name = "INDT_RESERVATION_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TMsReservation reservation;

    @JoinColumn(name = "INDT_ROOM_RESERVATION_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TTrRoomReservation roomReservation;

    @JoinColumn(name = "INDT_FACILITY_RESERVATION_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TTrFacilityReservation facilityReservation;

    @JoinColumn(name = "INDT_ITEM_RESERVATION_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TTrItemReservation itemReservation;

    @Column(name = "INDT_RESERVED_QUANTITY")
    private Integer indtReservedQuantity;

    @Column(name = "INDT_UNIT_PRICE")
    private BigDecimal indtUnitPrice;

    @Column(name = "INDT_AMOUNT")
    private BigDecimal indtAmount;

    @Column(name = "INDT_STATUS")
    private Short indtStatus;

    @JoinColumn(name = "INDT_BRANCH_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TRfBranch branch;
}
