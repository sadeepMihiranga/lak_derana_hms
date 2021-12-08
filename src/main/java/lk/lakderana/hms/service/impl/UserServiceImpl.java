package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.dto.RoleDTO;
import lk.lakderana.hms.dto.UserDTO;
import lk.lakderana.hms.entity.Role;
import lk.lakderana.hms.entity.RoleToUser;
import lk.lakderana.hms.entity.User;
import lk.lakderana.hms.exception.DataNotFoundException;
import lk.lakderana.hms.mapper.UserMapper;
import lk.lakderana.hms.repository.RoleRepository;
import lk.lakderana.hms.repository.UserRepository;
import lk.lakderana.hms.repository.UserRoleRepository;
import lk.lakderana.hms.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           UserRoleRepository userRoleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @Transactional
    @Override
    public void addRoleToUser(String username, String roleName) {
        final User user = userRepository.findByUsername(username);
        final Role role = roleRepository.findByName(roleName);

        RoleToUser roleToUser = new RoleToUser();
        roleToUser.setRole(role);
        roleToUser.setUser(user);
        roleToUser.setStatus(Short.valueOf("1"));

        userRoleRepository.save(roleToUser);
    }

    @Override
    public UserDTO getAUser(String username) {
        final User user = userRepository.findByUsername(username);

        if(user == null)
            throw new DataNotFoundException("User " + username + " not found");

        final List<RoleToUser> roleToUsers = userRoleRepository.findAllById(user.getId());

        final UserDTO userDTO = UserMapper.INSTANCE.entityToDTO(user);
        userDTO.setRoles(roleToUsers.stream().map(roleToUser -> mapRoleToRoleDTO(roleToUser.getRole())).collect(Collectors.toList()));

        return userDTO;
    }

    @Override
    public UserDTO getAUserById(Long userId) {
        final User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("User not found"));

        final List<RoleToUser> roleToUsers = userRoleRepository.findAllById(user.getId());

        final UserDTO userDTO = UserMapper.INSTANCE.entityToDTO(user);
        userDTO.setRoles(roleToUsers.stream().map(roleToUser -> mapRoleToRoleDTO(roleToUser.getRole())).collect(Collectors.toList()));

        return userDTO;
    }

    @Override
    public List<UserDTO> getAllUsers() {

        List<UserDTO> userDTOList = new ArrayList<>();

        final List<User> userList = userRepository.findAll();

        userList.forEach(user -> {

            UserDTO userDTO = new UserDTO();

            final List<RoleToUser> roleToUsers = userRoleRepository.findAllById(user.getId());

            userDTO.setId(user.getId());
            userDTO.setName(user.getName());
            userDTO.setUsername(user.getUsername());
            userDTO.setRoles(roleToUsers.stream().map(roleToUser -> mapRoleToRoleDTO(roleToUser.getRole())).collect(Collectors.toList()));

            userDTOList.add(userDTO);
        });

        return userDTOList;
    }

    private RoleDTO mapRoleToRoleDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());

        return roleDTO;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final UserDTO user = getAUser(username);

        if(user == null)
            throw new UsernameNotFoundException("User not found");
        else
            log.info("User {} found in the Database", username);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        final List<RoleToUser> roleToUsers = userRoleRepository.findAllById(user.getId());

        roleToUsers.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRole().getName())));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
