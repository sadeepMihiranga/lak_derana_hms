package lk.lakderana.hms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="\"T_MS_USER\"")
public class User {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String username;
    private String password;
    /*private String email;
    private Short status;*/
    /*@ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();*/

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Collection<RoleToUser> roleToUsers = new ArrayList<>();
}
