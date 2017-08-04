package it.ctalpa.planning.modules.Permission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.ctalpa.planning.modules.Role.RolePermission;
import it.ctalpa.planning.util.AuditedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by c.talpa on 22/05/2017.
 */
@Entity
@Table(name = "permission")
public class Permission extends AuditedEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @Size(max = 100)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "permission")
    private Set<RolePermission> rolePermissions=new HashSet<>();


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<RolePermission> getRolePermissions() {
        return rolePermissions;
    }

    public void setRolePermissions(Set<RolePermission> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Permission that = (Permission) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return rolePermissions != null ? rolePermissions.equals(that.rolePermissions) : that.rolePermissions == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (rolePermissions != null ? rolePermissions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rolePermissions=" + rolePermissions +
                '}';
    }
}
