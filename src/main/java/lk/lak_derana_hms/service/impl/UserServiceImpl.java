package lk.lak_derana_hms.service.impl;

import com.google.common.collect.Lists;
import lk.lak_derana_hms.dto.UserDTO;
import lk.lak_derana_hms.exception.DataNotFoundException;
import lk.lak_derana_hms.security.ApplicationUser;
import lk.lak_derana_hms.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return Lists.newArrayList(
                new UserDTO(1l, "sadeep", "password"),
                new UserDTO(2l, "randima", "password"),
                new UserDTO(3l, "chamari", "password")
        );
    }

    @Override
    public ApplicationUser loadUserUserByUsername(String username) {

        UserDTO userDTO = null;

        try {
            userDTO = getAllUsers()
                    .stream()
                    .filter(applicationUser -> username.equals(applicationUser.getUsername()))
                    .findFirst()
                    .get();
        } catch (NoSuchElementException e) {
            logger.error("Auth -> UserServiceImpl -> selectApplicationUserByUsername : " + e.getMessage());
            throw new DataNotFoundException("User not found");
        }

        return convertUserToApplicationUsers(userDTO);
    }

    private ApplicationUser convertUserToApplicationUsers(UserDTO userDTO) {
        return new ApplicationUser(
                userDTO.getId(),
                userDTO.getUsername(),
                passwordEncoder.encode(userDTO.getPassword()),
                Collections.emptyList(),
                Collections.emptyList()
        );
    }
}
