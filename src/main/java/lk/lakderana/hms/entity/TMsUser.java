package lk.lakderana.hms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="T_MS_USER")
public class TMsUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_ID")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_PARTY_CODE", nullable = false)
    private TMsParty party;

    @Column(name = "USER_PASSWORD")
    private String userPassword;

    @Column(name = "USER_USERNAME")
    private String userUsername;

    @Column(name = "USER_STATUS")
    private Short userStatus;
}
