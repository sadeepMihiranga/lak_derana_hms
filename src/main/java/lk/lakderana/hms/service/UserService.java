package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.PaginatedEntity;
import lk.lakderana.hms.dto.UserDTO;
import lk.lakderana.hms.entity.TMsRole;
import lk.lakderana.hms.entity.TMsRoleFunction;
import lk.lakderana.hms.security.User;

import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    TMsRole createRole(TMsRole role);

    void addRoleToUser(String username, String roleName);

    UserDTO getUserByUsername(String username);

    UserDTO getAUserById(Long userId);

    List<UserDTO> getAllUsers();

    PaginatedEntity userPaginatedSearch(String username, String partyCode, Integer page, Integer size);

    List<TMsRoleFunction> getPermissionsByRole(Long roleId);

    User getUserDetailsByUsername(String username);
}
