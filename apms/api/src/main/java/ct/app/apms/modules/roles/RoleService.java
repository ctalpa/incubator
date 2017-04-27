package ct.app.apms.modules.roles;


import ct.app.apms.modules.permissions.Permission;
import ct.app.apms.modules.permissions.PermissionsRepository;
import ct.app.apms.modules.users.UserRole;
import ct.app.apms.modules.users.UserRoleRepository;
import ct.app.apms.util.ModelUtils;
import ct.app.apms.util.exception.OperationNotAllowedException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionsRepository permissionsRepository;
    private final RoleOwnershipRepository roleOwnershipRepository;
    private final UserRoleRepository userRoleRepository;

    private static final String ADMIN_ROLE_NAME = "Administrator";

    public RoleService(RoleRepository roleRepository, RolePermissionRepository rolePermissionRepository,
            PermissionsRepository permissionsRepository, RoleOwnershipRepository roleOwnershipRepository,
                       UserRoleRepository userRoleRepository) {
        this.roleRepository = roleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.permissionsRepository = permissionsRepository;
        this.roleOwnershipRepository = roleOwnershipRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @PreAuthorize("hasAuthority('user_profile_view')")
    @Transactional(readOnly = true)
    public Page<Role> getAllRoles(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    @PreAuthorize("hasAuthority('user_profile_view')")
    @Transactional(readOnly = true)
    public Role getRoleById(int id) {
        return roleRepository.getOne(id);
    }

    @PreAuthorize("hasAuthority('user_profile_modify')")
    public Role updateUserRolePermissions(Integer roleId, Collection<Integer> permissions) {
        Role roleToUpdate = getRoleById(roleId);
        if (roleToUpdate != null) {
            if (ADMIN_ROLE_NAME.equals(roleToUpdate.getName()))
                throw new OperationNotAllowedException("Cannot modify the Administrator role");
            roleToUpdate = this.updateRolePermissions(roleToUpdate, permissions);
        }
        return roleToUpdate;
    }

    @PreAuthorize("hasAuthority('user_profile_modify')")
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    @PreAuthorize("hasAuthority('user_profile_modify')")
    public Role updateRole(Integer id, Role role) {
        Role existingRole = getRoleById(id);
        if (ADMIN_ROLE_NAME.equals(existingRole.getName()) || ADMIN_ROLE_NAME.equals(role.getName())) {
            throw new OperationNotAllowedException("Cannot modify the Administrator role");
        }
        ModelUtils.merge(role, existingRole, "createdAt", "createdBy", "id");
        return roleRepository.save(existingRole);
    }

    @PreAuthorize("hasAuthority('user_profile_modify')")
    public void deleteRole(Integer id) throws OperationNotAllowedException {
        Role role = roleRepository.getOne(id);
        if (role != null) {
            if (ADMIN_ROLE_NAME.equals(role.getName()))
                throw new OperationNotAllowedException("Cannot modify the Administrator role");

            final Set<RoleOwnership> roleOwnerships = role.getRoleOwnership();
            if (CollectionUtils.isNotEmpty(roleOwnerships)) {
                roleOwnershipRepository.delete(roleOwnerships);
            }
            final Set<RoleOwnership> parentRelationship = roleOwnershipRepository.getAllRoleOwnershipByOwnedRoleId(role);
            if (CollectionUtils.isNotEmpty(parentRelationship)) {
                roleOwnershipRepository.delete(parentRelationship);
            }
            final Set<RolePermission> rolePermissions = role.getRolePermissions();
            if (CollectionUtils.isNotEmpty(rolePermissions)) {
                rolePermissionRepository.delete(rolePermissions);
            }
            final Set<UserRole> userRoles = role.getUserRoles();
            if (CollectionUtils.isNotEmpty(userRoles)) {
                userRoleRepository.delete(userRoles);
            }
            roleOwnershipRepository.flush();
            rolePermissionRepository.flush();
            userRoleRepository.flush();
            roleRepository.delete(id);
            roleRepository.flush();
        }
    }

    @PreAuthorize("hasAuthority('user_profile_modify')")
    public Role updateRoleOwnership(Integer id, Collection<Integer> requiredRolesId) {
        final Role currentRole = getRoleById(id);
        final Map<Integer, RoleOwnership> currentOwnedRoles = currentRole.getRoleOwnership().stream()
            .collect(Collectors.toMap(ro -> ro.getOwnedRoleId().getId(), ro -> ro));

        final Map<Integer, Role> roles = roleRepository.findAllByIdIn(requiredRolesId).stream()
            .collect(Collectors.toMap(Role::getId, p -> p));

        final List<RoleOwnership> rolesOwnershipToAdd = requiredRolesId.stream().filter(i -> !currentOwnedRoles.containsKey(i))
            .map(i -> mapRoleOwnership(currentRole, roles.get(i))).collect(Collectors.toList());

        final List<RoleOwnership> rolesOwnershipToDelete = currentOwnedRoles.entrySet().stream()
            .filter(e -> !requiredRolesId.contains(e.getKey())).map(Map.Entry::getValue)
            .collect(Collectors.toList());

        roleOwnershipRepository.save(rolesOwnershipToAdd);
        roleOwnershipRepository.delete(rolesOwnershipToDelete);
        roleOwnershipRepository.flush();
        roleRepository.refresh(currentRole);
        return roleRepository.findOne(id);
    }

    @PreAuthorize("hasAuthority('user_profile_modify')")
    public Role updateRolePermissions(Role role, Collection<Integer> permissionIds) {
        Map<Integer, RolePermission> currentPermissions = role.getRolePermissions().stream()
                .collect(Collectors.toMap(gp -> gp.getPermission().getId(), gp -> gp));

        Map<Integer, Permission> permissions = permissionsRepository.findAllByIdIn(permissionIds).stream()
                .collect(Collectors.toMap(Permission::getId, p -> p));

        List<RolePermission> permissionsToAdd = permissionIds.stream().filter(i -> !currentPermissions.containsKey(i))
                .map(i -> mapRolePermission(role, permissions.get(i))).collect(Collectors.toList());

        List<RolePermission> permissionsToDelete = currentPermissions.entrySet().stream()
                .filter(e -> !permissionIds.contains(e.getKey())).map(Map.Entry::getValue).collect(Collectors.toList());

        rolePermissionRepository.save(permissionsToAdd);
        rolePermissionRepository.delete(permissionsToDelete);
        rolePermissionRepository.flush();
        roleRepository.refresh(role);
        return roleRepository.findOne(role.getId());
    }

    protected Role retrieveRole(final Collection<Role> roles, final Integer roleId, boolean recursively) {
        Role foundRole = null;
        if (roles != null) {
            for (final Role role : roles) {
                if (role.getId() == roleId) {
                    foundRole = role;
                    break;
                }
                if (recursively) {
                    foundRole = retrieveRole(role.getOwnedRoles(), roleId, false);
                }
                if (foundRole != null) {
                    break;
                }
            }
        }
        return foundRole;
    }

    private RolePermission mapRolePermission(Role role, Permission permission) {
        final RolePermission rolePermission = new RolePermission();
        rolePermission.setRole(role);
        rolePermission.setPermission(permission);

        return rolePermission;
    }

    private RoleOwnership mapRoleOwnership(Role role, Role ownedRole) {
        final RoleOwnership roleOwnership = new RoleOwnership();
        roleOwnership.setParentRoleId(role);
        roleOwnership.setOwnedRoleId(ownedRole);
        return roleOwnership;
    }
}
