package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.PaginatedEntity;
import lk.lakderana.hms.dto.UserDTO;
import lk.lakderana.hms.entity.TMsRole;
import lk.lakderana.hms.entity.TMsRoleFunction;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserById(Long userId);

    List<UserDTO> getAllUsers();

    PaginatedEntity userPaginatedSearch(String username, String partyCode, Integer page, Integer size);

    UserDTO getUserByUsername(String username);

    UserDTO getUserByPartyCode(String partyCode);

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    List<TMsRoleFunction> getPermissionsByRole(Long roleId);

    Long removeUserById(Long userId);

    Boolean removeUserByPartyCode(String partyCode);

    Boolean assignRoleToUser(Long userId, List<String> roles);

    TMsRole createRole(TMsRole role);

    UserDTO updateUser(Long userId, UserDTO userDTO);
}
