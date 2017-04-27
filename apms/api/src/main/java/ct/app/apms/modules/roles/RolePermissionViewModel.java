package ct.app.apms.modules.roles;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

// This class represents a "flattened" view of a Role's relationship to a Permission
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RolePermissionViewModel {

    private Integer id; // ID of the permission itself

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String name;

    
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

    
}
