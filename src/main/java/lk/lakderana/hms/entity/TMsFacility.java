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
@Table(name="T_MS_FACILITY")
public class TMsFacility extends AuditModel {

    @javax.persistence.Id
    @GeneratedValue(generator = "FacilitySequence")
    @SequenceGenerator(name = "FacilitySequence", schema = "LAKDERANA_BASE", sequenceName = "\"T_MS_FACILITY_FCLT_ID_seq\"", allocationSize = 1)
    @Column(name = "FCLT_ID")
    private Long fcltId;

    @Column(name = "FCLT_NAME")
    private String fcltName;

    @Column(name = "FCLT_DESCRIPTION")
    private String fcltDescription;

    @Column(name = "FCLT_TYPE")
    private String fcltType;

    @Column(name = "FCLT_PRICE")
    private BigDecimal fcltPrice;

    @Column(name = "FLCT_UOM")
    private Integer facltUom;

    @Column(name = "FCLT_STATUS")
    private Short facltStatus;
}
