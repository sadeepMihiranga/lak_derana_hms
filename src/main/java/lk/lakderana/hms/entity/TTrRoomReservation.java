package lk.lakderana.hms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="T_TR_ROOM_RESERVATION")
public class TTrRoomReservation extends AuditModel {

    @javax.persistence.Id
    @GeneratedValue(generator = "RoomReservationSequence")
    @SequenceGenerator(name = "RoomReservationSequence", schema = "LAKDERANA_BASE", sequenceName = "\"T_TR_ROOM_RESERVATION_RORE_ID_seq\"", allocationSize = 1)
    @Column(name = "RORE_ID")
    private Long roreId;

    @JoinColumn(name = "RORE_ROOM_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TMsRoom room;

    @JoinColumn(name = "RORE_RESERVATION_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TMsReservation reservation;

    @Column(name = "RORE_STATUS")
    private Short roreStatus;

    @JoinColumn(name = "RORE_BRANCH_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TRfBranch branch;
}
