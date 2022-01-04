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
@Table(name="T_TR_PAYMENT")
public class TTrPayment extends AuditModel {

    @javax.persistence.Id
    @GeneratedValue(generator = "PaymentSequence")
    @SequenceGenerator(name = "PaymentSequence", schema = "LAKDERANA_BASE", sequenceName = "\"T_TR_PAYMENT_PAYT_ID_seq\"", allocationSize = 1)
    @Column(name = "PAYT_ID")
    private Long paytId;

    @JoinColumn(name = "PAYT_RESERVATION_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TMsReservation reservation;

    @JoinColumn(name = "PAYT_INVOICAE_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TTrInvoice invoice;

    @Column(name = "PAYT_DESCRIPTION")
    private String paytDescription;

    @Column(name = "PAYT_TYPE")
    private String paytType;

    @Column(name = "PAYT_AMOUNT")
    private BigDecimal paytAmount;

    @Column(name = "PAYT_STATUS")
    private Short paytStatus;

    @JoinColumn(name = "FARE_BRANCH_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TRfBranch branch;
}
