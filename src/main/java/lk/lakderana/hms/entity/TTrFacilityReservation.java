package lk.lakderana.hms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="T_TR_FACILITY_RESERVATION")
public class TTrFacilityReservation extends AuditModel {

    @javax.persistence.Id
    @GeneratedValue(generator = "FacilityReservationSequence")
    @SequenceGenerator(name = "FacilityReservationSequence", schema = "LAKDERANA_BASE", sequenceName = "\"T_TR_FACILITY_RESERVATION_FARE_ID_seq\"", allocationSize = 1)
    @Column(name = "FARE_ID")
    private Long fareId;

    @JoinColumn(name = "FARE_FACILITY_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TMsFacility facility;

    @JoinColumn(name = "FARE_RESERVATION_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TMsReservation reservation;

    @Column(name = "FARE_QUANTITY")
    private Double fareQuantity;

    @Column(name = "FARE_STATUS")
    private Short fareStatus;

    @JoinColumn(name = "FARE_BRANCH_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TRfBranch branch;
}
