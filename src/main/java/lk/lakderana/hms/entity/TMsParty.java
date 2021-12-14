package lk.lakderana.hms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="T_MS_PARTY")
public class TMsParty {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PRTY_ID")
    private Long prtyId;

    @Column(name = "PRTY_NAME")
    private String prtyName;

    /*@Column(name = "PRTY_FIRST_NAME")
    private String prtyFirstName;

    @Column(name = "PRTY_LAST_NAME")
    private String prtyLastName;*/

    @Column(name = "PRTY_DOB")
    private Date prtyDob;

    @Column(name = "PRTY_ADDRESS_1")
    private String prtyAddress1;

    @Column(name = "PRTY_ADDRESS_2")
    private String prtyAddress2;

    @Column(name = "PRTY_ADDRESS_3")
    private String prtyAddress3;

    @Column(name = "PRTY_GENDER")
    private String prtyGender;

    @Column(name = "PRTY_NIC")
    private String prtyNic;

    @Column(name = "PRTY_PASSPORT")
    private String prtyPassport;

    @Column(name = "PRTY_TYPE")
    private String prtyType;

    @JoinColumn(name = "PRTY_DEPARTMENT_CODE")
    @ManyToOne(cascade = CascadeType.ALL)
    private TMsDepartment department;

    @JoinColumn(name = "PRTY_BRANCH_ID")
    @ManyToOne(cascade = CascadeType.ALL)
    private TRfBranch branch;

    @Column(name = "PRTY_MANAGED_BY")
    private Long prtyManagedBy;

    @Column(name = "PRTY_STATUS")
    private Short prtyStatus;
}
