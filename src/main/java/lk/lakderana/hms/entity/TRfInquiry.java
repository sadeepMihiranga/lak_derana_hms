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
@Table(name="T_MS_INQUIRY")
public class TRfInquiry extends AuditModel {

    @javax.persistence.Id
    @GeneratedValue(generator = "InquirySequence")
    @SequenceGenerator(name = "InquirySequence", schema = "LAKDERANA_BASE", sequenceName = "\"T_MS_INQUIRY_INQR_ID_seq\"", allocationSize = 1)
    @Column(name = "INQR_ID")
    private Long inqrId;

    @JoinColumn(name="INQR_CUSTOMER_CODE")
    private String inqrCustomerCode;

    @Column(name = "INQR_DATE_TIME")
    private LocalDateTime inqrDateTime;

    @Column(name = "INQR_REMARKS")
    private String inqrRemarks;

    @Column(name = "INQR_STATUS")
    private Short inqrStatus;

    @JoinColumn(name = "INQR_BRANCH_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TRfBranch branch;

    @Column(name = "INQR_CUSTOMER_NAME")
    private String inqrCustomerName;

    @Column(name = "INQR_CONTACT_NO")
    private String inqrCustomerContactNo;

    @Column(name = "INQR_TRANSFERRED_FROM")
    private Long inqrTransferredFrom;

    @Column(name = "INQR_TRANSFERRED_TO")
    private Long inqrTransferredTo;
}
