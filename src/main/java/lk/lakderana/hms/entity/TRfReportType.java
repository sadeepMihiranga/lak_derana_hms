package lk.lakderana.hms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="T_RF_REPORT_TYPE")
public class TRfReportType extends AuditModel {

    @javax.persistence.Id
    @GeneratedValue(generator = "ReportTypeSequence")
    @SequenceGenerator(name = "ReportTypeSequence", schema = "LAKDERANA_BASE", sequenceName = "\"T_RF_REPORT_TYPE_RPTP_ID_seq\"", allocationSize = 1)
    @Column(name = "RPTP_ID")
    private Long rptpId;

    @JoinColumn(name="RPTP_CODE")
    private String rptpCode;

    @JoinColumn(name="RPTP_DISPLAY_NAME")
    private String rptpDisplayName;

    @JoinColumn(name="RPTP_ICON")
    private String rptpIcon;

    @JoinColumn(name="RPTP_COLOR")
    private String rptpColor;

    @Column(name = "RPTP_STATUS")
    private Short rptpStatus;
}
