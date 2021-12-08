package lk.lakderana.hms.entity;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name="\"T_RF_USER_ROLES\"")
public class RoleToUser {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="\"ID\"")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "\"USER_ID\"", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "\"ROLES_ID\"", nullable = false)
    private Role role;

    @Column(name="\"STATUS\"")
    private Short status;

    /*private String username;
    private String roleName;*/
}
