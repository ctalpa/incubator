package ct.app.apms.modules.users;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ct.app.apms.modules.AuditedEntity;
import ct.app.apms.modules.permissions.Permission;
import ct.app.apms.modules.roles.Role;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class User extends AuditedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(min = 4, max = 50)
    private String login;

    @NotNull
    @Size(max = 255)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotNull
    @Size(max = 100)
    private String name;
    
    @NotNull
    @Size(max = 255)
    private String contactInformation;
    
    @NotNull
    @Size(max = 100)
    private String smsNumber;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<UserRole> userRoles = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        User user = (User) o;

        return id != null ? id.equals(user.id) : user.id == null;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public String getEmail() {
        return email;
    }

    public Integer getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Transient
    public Collection<String> getPermissions() {
        return getRoles().stream().flatMap(role -> role.getPermissions().stream()).map(Permission::getName).distinct()
                .collect(Collectors.toSet());
    }

    @Transient
    public Collection<Role> getRoles() {
        return getUserRoles().stream().map(UserRole::getRole).collect(Collectors.toList());
    }
    
    public String getSmsNumber() {
        return smsNumber;
    }

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public void setContactInformation(String aContactInformation) {
        contactInformation = aContactInformation;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login.toLowerCase(); // normalize logins
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSmsNumber(String aSmsNumber) {
        smsNumber = aSmsNumber;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", login=" + login + ", email=" + email + ", name=" + name + ", contactInformation="
                + contactInformation + ", smsNumber=" + smsNumber + "]";
    }
}
