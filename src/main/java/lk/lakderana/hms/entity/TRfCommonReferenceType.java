package lk.lakderana.hms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="T_RF_COMMON_REFERENCE_TYPE")
public class TRfCommonReferenceType {

    @Id
    @Column(name = "CMRT_CODE")
    private String cmrtCode;

    @Column(name = "CMRT_DESCRIPTION")
    private String cmrtDescription;

    @Column(name = "CMRT_STATUS")
    private Short cmrtStatus;
}
