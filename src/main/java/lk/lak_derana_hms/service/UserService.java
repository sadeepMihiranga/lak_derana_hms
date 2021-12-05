package lk.lak_derana_hms.service;

import lk.lak_derana_hms.dto.UserDTO;
import lk.lak_derana_hms.security.ApplicationUser;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDTO> getAllUsers();

    ApplicationUser loadUserUserByUsername(String username);
}
