package ct.app.apms.modules.users;


import ct.app.apms.modules.permissions.Permission;
import ct.app.apms.modules.roles.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface UserMapper {

    List<UserViewModel> toViewModel(Iterable<User> users);

    UserViewModel toViewModel(User user);

    default Collection<String> mapPermissions(Collection<Permission> permissions) {
        return permissions.stream().map(Permission::getName).collect(Collectors.toList());
    }

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "userRoles", ignore = true)


    User toModel(UserViewModel dto);

    Collection<RoleViewModel> toViewModel(Collection<Role> roles);

    @Mapping(target = "permissions", source = "rolePermissions")
    @Mapping(target = "ownedRoles", source = "roleOwnership")
    RoleViewModel toViewModel(Role role);

    @Mapping(target = "id", source = "permission.id")
    @Mapping(target = "name", source = "permission.name")
    RolePermissionViewModel toViewModel(RolePermission rolePermission);

    @Mapping(target = "id", source = "ownedRoleId.id")
    @Mapping(target = "name", source = "ownedRoleId.name")
    RoleOwnershipViewModel toViewModel(RoleOwnership roleOwnership);

    default Collection<Integer> getPermissionIds(Collection<RolePermissionViewModel> rolePermissionViewModels) {
        return rolePermissionViewModels.stream().map(RolePermissionViewModel::getId).collect(Collectors.toList());
    }

    default Collection<Integer> getOwnedRolesIds(Collection<RoleOwnershipViewModel> roleOwnershipViewModel) {
        return roleOwnershipViewModel.stream().map(RoleOwnershipViewModel::getId).collect(Collectors.toList());
    }


}
