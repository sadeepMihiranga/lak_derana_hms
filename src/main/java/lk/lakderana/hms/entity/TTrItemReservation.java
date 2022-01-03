package lk.lakderana.hms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="T_TR_ITEM_RESERVATION")
public class TTrItemReservation extends AuditModel {

    @javax.persistence.Id
    @GeneratedValue(generator = "ItemReservationSequence")
    @SequenceGenerator(name = "ItemReservationSequence", schema = "LAKDERANA_BASE", sequenceName = "\"T_TR_ITEM_RESERVATION_ITRS_ID_seq\"", allocationSize = 1)
    @Column(name = "ITRS_ID")
    private Long itrsId;

    @JoinColumn(name = "ITRS_ITEM_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TMsItem item;

    @JoinColumn(name = "ITRS_RESERVATION_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TMsReservation reservation;

    @Column(name = "ITRS_QUANTITY")
    private Double itrsQuantity;

    @Column(name = "ITRS_STATUS")
    private Short itrsStatus;

    @JoinColumn(name = "ITRS_BRANCH_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TRfBranch branch;
}
