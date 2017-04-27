package ct.app.apms.modules.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import ct.app.apms.modules.roles.RoleViewModel;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

public class UserViewModel {

    private Integer id;

    @Size(min = 4, max = 50)
    private String login;

    @Email
    @Size(max = 255)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Size(max = 100)
    private String name;

    @Size(max = 255)
    private String contactInformation;

    @Size(max = 100)
    private String smsNumber;

    private Collection<RoleViewModel> roles;

    private Collection<String> permissions;


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

    public Collection<String> getPermissions() {
        return permissions;
    }

    public Collection<RoleViewModel> getRoles() {
        return roles;
    }

    public String getSmsNumber() {
        return smsNumber;
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
        this.login = login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPermissions(Collection<String> permissions) {
        this.permissions = new TreeSet<>(permissions);
    }

    public void setRoles(Collection<RoleViewModel> roles) {
        this.roles = new ArrayList<>(roles);
    }

    public void setSmsNumber(String aSmsNumber) {
        smsNumber = aSmsNumber;
    }

    @Override
    public String toString() {
        return "UserViewModel{" + "id=" + id + ", login='" + login + '\'' + ", name='" + name + '\'' + ", email='"
                + email + '\'' + '}';
    }
}
