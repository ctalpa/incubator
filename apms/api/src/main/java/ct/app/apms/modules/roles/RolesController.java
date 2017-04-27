package ct.app.apms.modules.roles;


import ct.app.apms.util.exception.OperationNotAllowedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RolesController {

    private final Logger log = LoggerFactory.getLogger(RolesController.class);

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    public RolesController(RoleService roleService, RoleMapper roleMapper) {
        this.roleService = roleService;
        this.roleMapper = roleMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<RoleViewModel>> getAllRoles(
            @SortDefault(sort = {"name"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Role> roles = roleService.getAllRoles(pageable);
        final Page<RoleViewModel> resultPage = new PageImpl<>(roleMapper.toViewModel(roles),
                pageable, roles.getTotalElements());

        return ResponseEntity.ok().body(resultPage);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<RoleViewModel> getRole(@PathVariable Integer id) {
        Role role = roleService.getRoleById(id);
        RoleViewModel roleDto = roleMapper.toViewModel(role);
        return Optional.ofNullable(roleDto)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<RoleViewModel> createRole(@Valid @RequestBody RoleViewModel roleDto) throws URISyntaxException {
        log.debug("REST request to save Role : {}", roleDto);
        if (roleDto.getId() != null) {
            return ResponseEntity.badRequest().body(null);
        }
        Role roleToCreate = roleMapper.toModel(roleDto);
        Role createdRole = roleService.createRole(roleToCreate);
        RoleViewModel result = roleMapper.toViewModel(createdRole);

        return ResponseEntity.created(new URI("/api/roles/" + result.getId()))
            .body(result);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteRole(@PathVariable Integer id) {
        log.debug("REST request to delete Role with id : {}", id);
        try {
            roleService.deleteRole(id);
        } catch (OperationNotAllowedException onae) {
            log.warn("Delete of a role not allowed: {}", onae.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}/permissions", method = RequestMethod.PUT)
    public ResponseEntity<RoleViewModel> updateRolePermissions(
        @PathVariable Integer id,
        @RequestBody Collection<RolePermissionViewModel> rolePermissions
    ) {
        Collection<Integer> permissionIds = roleMapper.getPermissionIds(rolePermissions);
        RoleViewModel updatedRoleDto;
        try {
        Role role = roleService.updateUserRolePermissions(id, permissionIds);
            updatedRoleDto = roleMapper.toViewModel(role);
        } catch (OperationNotAllowedException onae) {
            log.warn("Update of a role not allowed: {}", onae.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        return Optional.ofNullable(updatedRoleDto)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/{id}/owned-roles", method = RequestMethod.PUT)
    public ResponseEntity<RoleViewModel> updateOwnedRoles(
        @PathVariable Integer id,
        @RequestBody Collection<RoleOwnershipViewModel> roleOwnedCollection
    ) {
        Collection<Integer> ownedRoleIds = roleMapper.getOwnedRolesIds(roleOwnedCollection);
        RoleViewModel updatedRoleDto;
        try {
            Role role = roleService.updateRoleOwnership(id, ownedRoleIds);
            updatedRoleDto = roleMapper.toViewModel(role);
        } catch (OperationNotAllowedException onae) {
            log.warn("Update of a role not allowed: {}", onae.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        return Optional.ofNullable(updatedRoleDto)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<RoleViewModel> updateRole(
        @PathVariable Integer id,
        @RequestBody RoleViewModel roleDto
    ) {
        Role role = roleMapper.toModel(roleDto);
        RoleViewModel updatedRoleDto;
        try {
            Role updatedRole = roleService.updateRole(id, role);
            updatedRoleDto = roleMapper.toViewModel(updatedRole);
        } catch (OperationNotAllowedException onae) {
            log.warn("Update of a role not allowed: {}", onae.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        return Optional.ofNullable(updatedRoleDto)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
