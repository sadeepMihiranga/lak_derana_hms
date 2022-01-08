package lk.lakderana.hms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="T_MS_ATTENDANCE")
public class TMsAttendance extends AuditModel {

    @javax.persistence.Id
    @GeneratedValue(generator = "AttendanceSequence")
    @SequenceGenerator(name = "AttendanceSequence", schema = "LAKDERANA_BASE", sequenceName = "\"T_MS_ATTENDANCE_ATTN_ID_seq\"", allocationSize = 1)
    @Column(name = "ATTN_ID")
    private Long attnId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="ATTN_EMPLOYEE_CODE", nullable = false)
    private TMsParty party;

    @Column(name = "ATTN_DATE")
    private LocalDate attnDate;

    @Column(name = "ATTN_IN_TIME")
    private LocalTime attnInTime;

    @Column(name = "ATTN_OUT_TIME")
    private LocalTime attnOutTime;

    @Column(name = "ATTN_STATUS")
    private Short attnStatus;

    @JoinColumn(name = "ATTN_BRANCH_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TRfBranch branch;

}
