package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.UserDTO;
import lk.lakderana.hms.entity.Role;
import lk.lakderana.hms.entity.User;

import java.util.List;

public interface UserService {

    User createUser(User user);

    Role createRole(Role role);

    void addRoleToUser(String username, String roleName);

    UserDTO getAUser(String username);

    UserDTO getAUserById(Long userId);

    List<UserDTO> getAllUsers();
}
