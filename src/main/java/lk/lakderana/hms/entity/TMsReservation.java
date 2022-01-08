package lk.lakderana.hms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="T_MS_RESERVATION")
public class TMsReservation extends AuditModel {

    @javax.persistence.Id
    @GeneratedValue(generator = "ReservationSequence")
    @SequenceGenerator(name = "ReservationSequence", schema = "LAKDERANA_BASE", sequenceName = "\"T_MS_RESERVATION_RESV_ID_seq\"", allocationSize = 1)
    @Column(name = "RESV_ID")
    private Long resvId;

    @JoinColumn(name = "RESV_INQUIRY_ID")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private TRfInquiry inquiry;

    @Column(name = "RESV_CHECK_IN_DATE_TIME")
    private LocalDateTime resvCheckInDateTime;

    @Column(name = "RESV_CHECK_OUT_DATE_TIME")
    private LocalDateTime resvCheckOutDateTime;

    @Column(name = "RESV_REMARKS")
    private String resvRemarks;

    @Column(name = "RESV_NO_OF_ADULTS")
    private Integer resvNoOfAdults;

    @Column(name = "RESV_NO_OF_CHILDREN")
    private Integer resvNoOfChildren;

    @Column(name = "RESV_STATUS")
    private Short resvStatus;

    @JoinColumn(name = "RESV_BRANCH_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TRfBranch branch;

    @Column(name = "RESV_CANCELLATION_REASON")
    private String resvCancellationReasons;
}
