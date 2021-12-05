package lk.lak_derana_hms.security;

import lk.lak_derana_hms.service.UserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ApplicationUserService implements UserDetailsService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public ApplicationUserService(UserService userService,
                                  PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /*@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService
                .selectApplicationUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("username %s not found", username)));
    }*/

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User(
                "sadeep",
                passwordEncoder.encode("password"),
                new ArrayList<>()
        );
    }
}
