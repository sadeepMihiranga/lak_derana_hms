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
@Table(name="T_MS_ROOM")
public class TMsRoom extends AuditModel {

    @javax.persistence.Id
    @GeneratedValue(generator = "RoomSequence")
    @SequenceGenerator(name = "RoomSequence", schema = "LAKDERANA_BASE", sequenceName = "\"T_MS_ROOM_ID_seq\"", allocationSize = 1)
    @Column(name = "ROOM_ID")
    private Long roomId;

    @Column(name = "ROOM_TYPE")
    private String roomType;

    @Column(name = "ROOM_NO")
    private String roomNo;

    @Column(name = "ROOM_CATEGORY")
    private String roomCategory;

    @Column(name = "ROOM_DESCRIPTION")
    private String roomDescription;

    @Column(name = "ROOM_PRICE")
    private BigDecimal roomPrice;

    @Column(name = "ROOM_STATUS")
    private Short roomStatus;

    @JoinColumn(name = "ROOM_BRANCH_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TRfBranch branch;
}
