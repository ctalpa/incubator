package ct.app.apms.modules.users;


import ct.app.apms.modules.roles.Role;
import ct.app.apms.modules.roles.RoleRepository;
import ct.app.apms.modules.roles.RoleViewModel;
import ct.app.apms.util.ModelUtils;
import ct.app.apms.util.exception.ExceptionFactory;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository,
            UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @PreAuthorize("hasAuthority('user_view')")
    @Transactional(readOnly = true)
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @PreAuthorize("hasAuthority('user_view')")
    @Transactional(readOnly = true)
    public User getUserById(Integer id) {
        return userRepository.getOne(id);
    }

    @PreAuthorize("hasAuthority('user_view')")
    @Transactional(readOnly = true)
    public User getUserByLogin(String login) {
        return userRepository.getOneByLogin(login);
    }

    @Transactional(readOnly = true)
    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails details = (UserDetails) auth.getPrincipal();
        return userRepository.getOneByLogin(details.getUsername());
    }

    @PreAuthorize("hasAuthority('user_modify')")
    public User createUser(User user) {
        String password;
        if (user.getPassword() != null) {
            password = user.getPassword();
        } else {
            throw ExceptionFactory.getNullablePasswordException(User.class);
        }
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @PreAuthorize("hasAuthority('user_modify')")
    public User updateUser(Integer id, User user) {
        User existingUser = getUserById(id);

        ModelUtils.mergeOnly(user, existingUser, "name", "contactInformation", "smsNumber");

        if (StringUtils.isNotEmpty(user.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    @PreAuthorize("hasAuthority('user_modify')")
    public User updateUserRoles(Integer id, Collection<RoleViewModel> requiredRoles) {
        User existingUser = getUserById(id);

        final Map<Integer, UserRole> currentUserRoles = existingUser.getUserRoles().stream()
                .collect(Collectors.toMap(ro -> ro.getRole().getId(), ro -> ro));

        final Collection<Integer> rolesId = requiredRoles.stream().map(RoleViewModel::getId)
                .collect(Collectors.toList());

        final Map<Integer, Role> roles = roleRepository.findAllByIdIn(rolesId).stream()
                .collect(Collectors.toMap(Role::getId, p -> p));

        final List<UserRole> userRolesToAdd = rolesId.stream().filter(i -> !currentUserRoles.containsKey(i))
                .map(i -> mapUserRole(existingUser, roles.get(i))).collect(Collectors.toList());

        final List<UserRole> userRolesToDelete = currentUserRoles.entrySet().stream()
                .filter(e -> !rolesId.contains(e.getKey())).map(Map.Entry::getValue).collect(Collectors.toList());

        if (log.isDebugEnabled()) {
            log.debug("@User: {} - Roles to add: {} - Roles to remove: {}", id, userRolesToAdd, userRolesToDelete);
        }

        userRoleRepository.save(userRolesToAdd);
        userRoleRepository.delete(userRolesToDelete);
        userRoleRepository.flush();
        userRepository.refresh(existingUser);
        return userRepository.findOne(id);
    }

    @PreAuthorize("hasAuthority('user_modify')")
    public void deleteUser(Integer id) {
        User existingUser = getUserById(id);
        if (existingUser != null) {
            final Set<UserRole> userRoles = existingUser.getUserRoles();
            if (CollectionUtils.isNotEmpty(userRoles)) {
                userRoleRepository.delete(userRoles);
            }
            userRoleRepository.flush();
        }
        userRepository.delete(id);
        userRepository.flush();
    }

    private UserRole mapUserRole(User user, Role role) {
        final UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        return userRole;
    }
}
