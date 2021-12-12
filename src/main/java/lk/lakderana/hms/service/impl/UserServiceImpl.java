package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.dto.FunctionDTO;
import lk.lakderana.hms.dto.RoleDTO;
import lk.lakderana.hms.dto.UserDTO;
import lk.lakderana.hms.entity.TMsRole;
import lk.lakderana.hms.entity.TMsRoleFunction;
import lk.lakderana.hms.entity.TMsUserRole;
import lk.lakderana.hms.entity.TMsUser;
import lk.lakderana.hms.exception.DataNotFoundException;
import lk.lakderana.hms.exception.InvalidDataException;
import lk.lakderana.hms.mapper.UserMapper;
import lk.lakderana.hms.repository.RoleFunctionRepository;
import lk.lakderana.hms.repository.RoleRepository;
import lk.lakderana.hms.repository.UserRepository;
import lk.lakderana.hms.repository.UserRoleRepository;
import lk.lakderana.hms.security.User;
import lk.lakderana.hms.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleFunctionRepository roleFunctionRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           UserRoleRepository userRoleRepository,
                           RoleFunctionRepository roleFunctionRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleFunctionRepository = roleFunctionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public UserDTO createUser(UserDTO userDTO) {

        final TMsUser userExists = userRepository.findByUserUsername(userDTO.getUsername());

        if(userExists != null)
            throw new InvalidDataException("An User already exists with " + userDTO.getUsername());

        final TMsUser tMsUser = UserMapper.INSTANCE.dtoToEntity(userDTO);
        tMsUser.setUserPassword(passwordEncoder.encode(tMsUser.getUserPassword()));

        final TMsUser createdUser = userRepository.save(tMsUser);

        final UserDTO insertedUserDTO = UserMapper.INSTANCE.entityToDTO(createdUser);

        return insertedUserDTO;
    }

    @Transactional
    @Override
    public TMsRole createRole(TMsRole tMsRole) {
        return roleRepository.save(tMsRole);
    }

    @Transactional
    @Override
    public void addRoleToUser(String username, String roleName) {
        final TMsUser tMsUser = userRepository.findByUserUsername(username);
        final TMsRole tMsRole = roleRepository.findByRoleName(roleName);

        TMsUserRole tRfUserRole = new TMsUserRole();
        tRfUserRole.setRole(tMsRole);
        tRfUserRole.setUser(tMsUser);
        tRfUserRole.setUsrlStatus(Short.valueOf("1"));

        userRoleRepository.save(tRfUserRole);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        final TMsUser tMsUser = userRepository.findByUserUsername(username);

        if(tMsUser == null)
            throw new DataNotFoundException("User " + username + " not found");

        final List<TMsUserRole> tRfUserRoleList = userRoleRepository.findAllById(tMsUser.getUserId());

        final UserDTO userDTO = UserMapper.INSTANCE.entityToDTO(tMsUser);
        userDTO.setRoles(tRfUserRoleList.stream().map(tRfUserRole -> mapRoleToRoleDTO(tRfUserRole.getRole())).collect(Collectors.toList()));

        return userDTO;
    }

    @Override
    public UserDTO getAUserById(Long userId) {
        final TMsUser tMsUser = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("User not found"));

        final List<TMsUserRole> tRfUserRoleList = userRoleRepository.findAllById(tMsUser.getUserId());

        final UserDTO userDTO = UserMapper.INSTANCE.entityToDTO(tMsUser);
        userDTO.setRoles(tRfUserRoleList.stream().map(tRfUserRole -> mapRoleToRoleDTO(tRfUserRole.getRole())).collect(Collectors.toList()));

        return userDTO;
    }

    @Override
    public List<UserDTO> getAllUsers() {

        List<UserDTO> userDTOList = new ArrayList<>();

        final List<TMsUser> userList = userRepository.findAll();

        userList.forEach(user -> {

            UserDTO userDTO = new UserDTO();

            final List<TMsUserRole> tRfUserRoleList = userRoleRepository.findAllById(user.getUserId());

            userDTO.setId(user.getUserId());
            userDTO.setName(user.getUserFullName());
            userDTO.setUsername(user.getUserUsername());
            userDTO.setRoles(tRfUserRoleList.stream().map(tRfUserRole -> mapRoleToRoleDTO(tRfUserRole.getRole())).collect(Collectors.toList()));

            userDTOList.add(userDTO);
        });

        return userDTOList;
    }

    @Override
    public List<TMsRoleFunction> getPermissionsByRole(Long roleId) {
        final List<TMsRoleFunction> tMsRoleFunctionList = roleFunctionRepository.findAllByRoleRoleId(roleId);
        return tMsRoleFunctionList;
    }

    @Override
    public User getUserDetailsByUsername(String username) {
        return (User) loadUserByUsername(username);
    }

    private RoleDTO mapRoleToRoleDTO(TMsRole role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getRoleId());
        roleDTO.setName(role.getRoleName());

        return roleDTO;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final UserDTO userInDb = getUserByUsername(username);

        if(userInDb == null)
            throw new UsernameNotFoundException("User not found");
        else
            log.info("User {} found in the Database", username);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        final List<TMsUserRole> tRfUserRoleList = userRoleRepository.findAllById(userInDb.getId());

        Collection<FunctionDTO> permittedFunctions = new ArrayList<>();

        tRfUserRoleList.forEach(tRfUserRole -> {
            authorities.add(new SimpleGrantedAuthority(tRfUserRole.getRole().getRoleName()));

            final List<TMsRoleFunction> functionList = getPermissionsByRole(tRfUserRole.getRole().getRoleId());

            functionList.forEach(tMsRoleFunction -> {
                permittedFunctions.add(
                        new FunctionDTO(
                                tMsRoleFunction.getFunction().getFuncId(),
                                tMsRoleFunction.getFunction().getDunsDescription(),
                                tMsRoleFunction.getRofuStatus()
                        )
                );
            });
        });

        User user = new User();
        user.setId(userInDb.getId());
        user.setName(userInDb.getName());
        user.setUsername(userInDb.getUsername());
        user.setPassword(userInDb.getPassword());
        user.setAuthorities(authorities);
        user.setPermittedFunctions(permittedFunctions);

        return user;
    }
}
