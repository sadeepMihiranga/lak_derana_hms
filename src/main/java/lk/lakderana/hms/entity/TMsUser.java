package lk.lakderana.hms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="\"T_MS_USER\"")
public class TMsUser {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "USER_FULL_NAME")
    private String userFullName;

    @Column(name = "USER_PASSWORD")
    private String userPassword;

    @Column(name = "USER_USERNAME")
    private String userUsername;
}
