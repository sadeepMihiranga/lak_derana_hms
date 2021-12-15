package lk.lakderana.hms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="T_RF_BRANCH")
public class TRfBranch {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "BRNH_ID")
    private Long brnhId;

    @Column(name = "BRNH_NAME")
    private String brnhName;

    @Column(name = "BRNH_LOCATION")
    private String brnhDescription;

    @Column(name = "BRNH_STATUS")
    private Short brnhStatus;
}