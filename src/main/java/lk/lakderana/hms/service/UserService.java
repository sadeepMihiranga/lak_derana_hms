package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.UserDTO;
import lk.lakderana.hms.entity.TMsRole;
import lk.lakderana.hms.entity.TMsRoleFunction;

import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    TMsRole createRole(TMsRole role);

    void addRoleToUser(String username, String roleName);

    UserDTO getAUser(String username);

    UserDTO getAUserById(Long userId);

    List<UserDTO> getAllUsers();

    List<TMsRoleFunction> getPermissionsByRole(Long roleId);
}
