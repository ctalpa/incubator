package it.ctalpa.planning.modules.Role;

import it.ctalpa.planning.modules.Permission.Permission;
import it.ctalpa.planning.util.AuditedEntity;

import javax.persistence.*;

/**
 * Created by c.talpa on 22/05/2017.
 */
@Entity
@Table(name="role_permission")
public class RolePermission extends AuditedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RolePermission that = (RolePermission) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (permission != null ? !permission.equals(that.permission) : that.permission != null) return false;
        return role != null ? role.equals(that.role) : that.role == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "RolePermission{" +
                "id=" + id +
                ", permission=" + permission +
                ", role=" + role +
                '}';
    }
}
