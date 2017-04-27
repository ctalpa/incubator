package ct.app.apms.modules.roles;


import ct.app.apms.modules.AuditedEntity;

import javax.persistence.*;

@Entity
@Table(name = "role_ownership")
public class RoleOwnership extends AuditedEntity {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "parent_role_id")
    private Role parentRoleId;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owned_role_id")
    private Role ownedRoleId;
    
    public Role getParentRoleId() {
        return parentRoleId;
    }

    public void setParentRoleId(Role parentRoleId) {
        this.parentRoleId = parentRoleId;
    }

    public Role getOwnedRoleId() {
        return ownedRoleId;
    }

    public void setOwnedRoleId(Role ownedRoleId) {
        this.ownedRoleId = ownedRoleId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RoleOwnership other = (RoleOwnership) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }



    
}
