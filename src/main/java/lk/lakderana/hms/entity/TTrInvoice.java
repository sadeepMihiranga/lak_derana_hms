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
@Table(name="T_TR_INVOICE")
public class TTrInvoice extends AuditModel {

    @javax.persistence.Id
    @GeneratedValue(generator = "InvoiceSequence")
    @SequenceGenerator(name = "InvoiceSequence", schema = "LAKDERANA_BASE", sequenceName = "\"T_TR_INVOICE_INVC_ID_seq\"", allocationSize = 1)
    @Column(name = "INVC_ID")
    private Long invcId;

    @Column(name = "INVC_NUMBER")
    private String invcNumber;

    @Column(name = "INVC_DESCRIPTION")
    private String invcDescription;

    @Column(name = "INVC_GROSS_AMOUNT")
    private BigDecimal invcGrossAmount;

    @Column(name = "INVC_NET_AMOUNT")
    private BigDecimal invcNetAmount;

    @Column(name = "INVC_DISCOUNT_AMOUNT")
    private BigDecimal invcDiscountAmount;

    @Column(name = "INVC_TAX_AMOUNT")
    private BigDecimal invcTaxAmount;

    @Column(name = "INVC_STATUS")
    private Short invcStatus;

    @JoinColumn(name = "INVC_BRANCH_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TRfBranch branch;
}
