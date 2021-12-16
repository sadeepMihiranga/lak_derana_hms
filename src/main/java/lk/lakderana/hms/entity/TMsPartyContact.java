package lk.lakderana.hms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="T_MS_PARTY_CONTACT")
public class TMsPartyContact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PTCN_ID")
    private Long ptcnId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="PTCN_PRTY_CODE", nullable = false)
    private TMsParty party;

    @Column(name = "PTCN_CONTACT_TYPE")
    private String ptcnContactType;

    @Column(name = "PTCN_CONTACT_NUMBER")
    private String ptcnContactNumber;

    @Column(name = "PTCN_STATUS")
    private Short ptcnStatus;
}
