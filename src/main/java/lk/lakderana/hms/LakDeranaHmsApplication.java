package lk.lakderana.hms;

import lk.lakderana.hms.dto.UserDTO;
import lk.lakderana.hms.entity.TMsRole;
import lk.lakderana.hms.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class LakDeranaHmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(LakDeranaHmsApplication.class, args);
    }

    /*@Bean
    CommandLineRunner run(UserService userService) {
        return args -> {

            userService.createRole(new TMsRole(null, "ROLE_USER"));
            userService.createRole(new TMsRole(null, "ROLE_MANAGER"));
            userService.createRole(new Role(null, "ROLE_ADMIN"));
            userService.createRole(new Role(null, "ROLE_SUPER_ADMIN"));

            userService.createUser(new UserDTO(5l, "Sadeep Mihiranga", "sadeep", "1234", new ArrayList<>()));
            userService.createUser(new UserDTO(6l, "Suranja Liyanage", "sura", "1234", new ArrayList<>()));
            userService.createUser(new UserDTO(7l, "Chamari Irosha", "chami", "1234", new ArrayList<>()));
            userService.createUser(new UserDTO(8l, "Rasini Kumara", "rasini", "1234", new ArrayList<>()));

            userService.addRoleToUser("sadeep", "ROLE_USER");
            userService.addRoleToUser("sadeep", "ROLE_MANAGER");
            userService.addRoleToUser("sura", "ROLE_MANAGER");
            userService.addRoleToUser("chami", "ROLE_ADMIN");
            userService.addRoleToUser("rasini", "ROLE_SUPER_ADMIN");
            userService.addRoleToUser("rasini", "ROLE_ADMIN");
            userService.addRoleToUser("rasini", "ROLE_USER");
        };
    }*/
}
