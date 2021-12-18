package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.dto.*;
import lk.lakderana.hms.entity.*;
import lk.lakderana.hms.exception.DataNotFoundException;
import lk.lakderana.hms.exception.DuplicateRecordException;
import lk.lakderana.hms.exception.InvalidDataException;
import lk.lakderana.hms.exception.NoRequiredInfoException;
import lk.lakderana.hms.mapper.UserMapper;
import lk.lakderana.hms.repository.*;
import lk.lakderana.hms.security.User;
import lk.lakderana.hms.service.UserService;
import lk.lakderana.hms.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final RoleFunctionRepository roleFunctionRepository;
    private final PartyRepository partyRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           UserRoleRepository userRoleRepository,
                           RoleFunctionRepository roleFunctionRepository,
                           PartyRepository partyRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleFunctionRepository = roleFunctionRepository;
        this.partyRepository = partyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public UserDTO createUser(UserDTO userDTO) {

        final TMsParty tMsParty = partyRepository
                .findByPrtyCodeAndPrtyStatus(userDTO.getPartyCode(), Constants.STATUS_ACTIVE.getShortValue());

        if(tMsParty == null)
            throw new DataNotFoundException("Invalid Party Code " + userDTO.getPartyCode());

        final TMsUser userExists = userRepository
                .findByUserUsernameAndUserStatus(userDTO.getUsername(), Constants.STATUS_ACTIVE.getShortValue());

        if(userExists != null)
            throw new InvalidDataException("An User already exists with " + userDTO.getUsername());

        final TMsUser tMsUser = UserMapper.INSTANCE.dtoToEntity(userDTO);
        tMsUser.setUserPassword(passwordEncoder.encode(tMsUser.getUserPassword()));
        tMsUser.setParty(tMsParty);

        final TMsUser createdUser = userRepository.save(tMsUser);

        if(userDTO.getRoles() != null) {
            userDTO.getRoles().forEach(roleDTO -> {
                final TMsRole tMsRole = roleRepository
                        .findByRoleNameAndRoleStatus(roleDTO.getName(), Constants.STATUS_ACTIVE.getShortValue());

                if(tMsRole == null)
                    throw new InvalidDataException("Received Role " + roleDTO.getName() + " is invalid");

                TMsUserRole tMsUserRole = userRoleRepository
                        .findByUser_UserIdAndRole_RoleIdAndUsrlStatus(createdUser.getUserId(), tMsRole.getRoleId(), Constants.STATUS_ACTIVE.getShortValue());

                if(tMsUserRole != null)
                    throw new DuplicateRecordException("Requested User and Role combination is already there");

                tMsUserRole = new TMsUserRole();
                tMsUserRole.setUser(createdUser);
                tMsUserRole.setRole(tMsRole);
                tMsUserRole.setUsrlStatus(Constants.STATUS_ACTIVE.getShortValue());

                userRoleRepository.save(tMsUserRole);
            });
        }

        return getAUserById(createdUser.getUserId());
    }

    @Transactional
    @Override
    public TMsRole createRole(TMsRole tMsRole) {
        return roleRepository.save(tMsRole);
    }

    @Transactional
    @Override
    public Boolean assignRoleToUser(Long userId, List<String> roles) {

        if(userId == null)
            throw new NoRequiredInfoException("User Id is required");

        if(roles == null || roles.isEmpty())
            throw new NoRequiredInfoException("Roles required");

        final TMsUser tMsUser = userRepository.findByUserIdAndUserStatus(userId, Constants.STATUS_ACTIVE.getShortValue());

        if(tMsUser == null)
            throw new DataNotFoundException("User not found for Id " + userId);

        List<TMsUserRole> tMsUserRoleList = new ArrayList<>();

        roles.forEach(roleName -> {

            final TMsRole tMsRole = roleRepository.findByRoleNameAndRoleStatus(roleName, Constants.STATUS_ACTIVE.getShortValue());

            if(tMsRole == null)
                throw new DataNotFoundException("Role " + roleName + " not found");

            final TMsUserRole existingUserRole = userRoleRepository
                    .findByUser_UserIdAndRole_RoleId(tMsUser.getUserId(), tMsRole.getRoleId());

            if(existingUserRole != null) {

                existingUserRole.setUsrlStatus(Constants.STATUS_ACTIVE.getShortValue());
                tMsUserRoleList.add(existingUserRole);

            } else {

                TMsUserRole tRfUserRole = new TMsUserRole();
                tRfUserRole.setRole(tMsRole);
                tRfUserRole.setUser(tMsUser);
                tRfUserRole.setUsrlStatus(Constants.STATUS_ACTIVE.getShortValue());

                tMsUserRoleList.add(tRfUserRole);
            }
        });

        userRoleRepository.saveAll(tMsUserRoleList);

        return true;
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        final TMsUser tMsUser = userRepository.findByUserUsernameAndUserStatus(username, Constants.STATUS_ACTIVE.getShortValue());

        if(tMsUser == null)
            throw new DataNotFoundException("User " + username + " not found");

        final List<TMsUserRole> tRfUserRoleList = userRoleRepository
                .findAllByUser_UserIdAndUsrlStatus(tMsUser.getUserId(), Constants.STATUS_ACTIVE.getShortValue());

        final UserDTO userDTO = UserMapper.INSTANCE.entityToDTO(tMsUser);
        userDTO.setRoles(tRfUserRoleList.stream().map(tRfUserRole -> mapRoleToRoleDTO(tRfUserRole.getRole())).collect(Collectors.toList()));

        return userDTO;
    }

    @Override
    public UserDTO getUserByPartyCode(String partyCode) {
        final TMsUser tMsUser = userRepository.findByParty_PrtyCodeAndUserStatus(partyCode, Constants.STATUS_ACTIVE.getShortValue());

        if(tMsUser == null)
            throw new DataNotFoundException("User " + partyCode + " not found");

        final List<TMsUserRole> tRfUserRoleList = userRoleRepository
                .findAllByUser_UserIdAndUsrlStatus(tMsUser.getUserId(), Constants.STATUS_ACTIVE.getShortValue());

        final UserDTO userDTO = UserMapper.INSTANCE.entityToDTO(tMsUser);
        userDTO.setRoles(tRfUserRoleList.stream().map(tRfUserRole -> mapRoleToRoleDTO(tRfUserRole.getRole())).collect(Collectors.toList()));

        return userDTO;
    }

    @Override
    public UserDTO getAUserById(Long userId) {

        if(userId == null)
            throw new NoRequiredInfoException("User Id is required");

        final TMsUser tMsUser = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("User not found"));

        final List<TMsUserRole> tRfUserRoleList = userRoleRepository
                .findAllByUser_UserIdAndUsrlStatus(tMsUser.getUserId(), Constants.STATUS_ACTIVE.getShortValue());

        final UserDTO userDTO = UserMapper.INSTANCE.entityToDTO(tMsUser);

        userDTO.setPassword(null);
        userDTO.setRoles(tRfUserRoleList.stream().map(tRfUserRole -> mapRoleToRoleDTO(tRfUserRole.getRole())).collect(Collectors.toList()));

        return userDTO;
    }

    @Override
    public List<UserDTO> getAllUsers() {

        List<UserDTO> userDTOList = new ArrayList<>();

        final List<TMsUser> userList = userRepository.findAllByUserStatus(Constants.STATUS_ACTIVE.getShortValue());

        userList.forEach(user -> {

            UserDTO userDTO = new UserDTO();

            final List<TMsUserRole> tRfUserRoleList = userRoleRepository
                    .findAllByUser_UserIdAndUsrlStatus(user.getUserId(), Constants.STATUS_ACTIVE.getShortValue());

            userDTO.setId(user.getUserId());
            userDTO.setPartyCode(user.getParty().getPrtyCode());
            userDTO.setDisplayName(user.getParty().getPrtyFirstName());
            userDTO.setUsername(user.getUserUsername());
            userDTO.setRoles(tRfUserRoleList.stream().map(tRfUserRole -> mapRoleToRoleDTO(tRfUserRole.getRole())).collect(Collectors.toList()));

            userDTOList.add(userDTO);
        });

        return userDTOList;
    }

    @Override
    public PaginatedEntity userPaginatedSearch(String username, String partyCode, Integer page, Integer size) {

        PaginatedEntity paginatedUserList = null;
        List<UserDTO> userList = null;

        if (page < 1)
            throw new InvalidDataException("Page should be a value greater than 0");

        if (size < 1)
            throw new InvalidDataException("Limit should be a value greater than 0");

        partyCode = partyCode.isEmpty() ? null : partyCode;

        final Page<TMsUser> tMsUserPage = userRepository
                .getActiveUsers(username, partyCode, Constants.STATUS_ACTIVE.getShortValue(), PageRequest.of(page - 1, size));

        if (tMsUserPage.getSize() == 0)
            return null;

        paginatedUserList = new PaginatedEntity();
        userList = new ArrayList<>();

        for (TMsUser tMsUser : tMsUserPage) {
            tMsUser.setUserPassword(null);
            userList.add(UserMapper.INSTANCE.entityToDTO(tMsUser));
        }

        for(UserDTO userDTO : userList) {

            final List<TMsUserRole> tRfUserRoleList = userRoleRepository
                    .findAllByUser_UserIdAndUsrlStatus(userDTO.getId(), Constants.STATUS_ACTIVE.getShortValue());

            userDTO.setRoles(tRfUserRoleList.stream().map(tRfUserRole -> mapRoleToRoleDTO(tRfUserRole.getRole())).collect(Collectors.toList()));
        }

        paginatedUserList.setTotalNoOfPages(tMsUserPage.getTotalPages());
        paginatedUserList.setTotalNoOfRecords(tMsUserPage.getTotalElements());
        paginatedUserList.setEntities(userList);

        return paginatedUserList;
    }

    @Override
    public List<TMsRoleFunction> getPermissionsByRole(Long roleId) {
        final List<TMsRoleFunction> tMsRoleFunctionList = roleFunctionRepository.findAllByRoleRoleId(roleId);
        return tMsRoleFunctionList;
    }

    @Override
    public Long removeUser(Long userId) {

        if(userId == null)
            throw new NoRequiredInfoException("User Id is required");

        final TMsUser tMsUser = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("User not found"));

        tMsUser.setUserStatus(Constants.STATUS_INACTIVE.getShortValue());

        final List<TMsUserRole> tMsUserRoleList = userRoleRepository
                .findAllByUser_UserIdAndUsrlStatus(tMsUser.getUserId(), Constants.STATUS_ACTIVE.getShortValue());

        tMsUserRoleList.forEach(tMsUserRole -> {
            tMsUserRole.setUsrlStatus(Constants.STATUS_INACTIVE.getShortValue());

            userRoleRepository.save(tMsUserRole);
        });

        return userRepository.save(tMsUser).getUserId();
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

        final List<TMsUserRole> tRfUserRoleList = userRoleRepository
                .findAllByUser_UserIdAndUsrlStatus(userInDb.getId(), Constants.STATUS_ACTIVE.getShortValue());

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
        user.setName(userInDb.getDisplayName());
        user.setPartyCode(userInDb.getPartyCode());
        user.setBranchCode("");
        user.setUsername(userInDb.getUsername());
        user.setPassword(userInDb.getPassword());
        user.setAuthorities(authorities);
        user.setPermittedFunctions(permittedFunctions);

        return user;
    }
}
