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
@Table(name="T_TR_REPORT_HISTORY")
public class TTrReportHistory extends AuditModel {

    @javax.persistence.Id
    @GeneratedValue(generator = "ReportHistorySequence")
    @SequenceGenerator(name = "ReportHistorySequence", schema = "LAKDERANA_BASE", sequenceName = "\"T_TR_REPORT_HISTORY_RPHT_ID_seq\"", allocationSize = 1)
    @Column(name = "RPHT_ID")
    private Long rphtId;

    @JoinColumn(name = "RPHT_REPORT_TYPE")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TRfReportType reportType;

    @Column(name = "RPHT_STATUS")
    private Short rphtStatus;

    @Column(name = "RPHT_FROM_DATE")
    private LocalDateTime rphtFromDate;

    @Column(name = "RPHT_TO_DATE")
    private LocalDateTime rphtToDate;

    @JoinColumn(name = "RPHT_BRANH_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TRfBranch branch;
}
