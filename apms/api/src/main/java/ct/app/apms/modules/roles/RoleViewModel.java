package ct.app.apms.modules.roles;

import java.util.Collection;

public class RoleViewModel {

    private Integer id;
    private String name;
    private Collection<RolePermissionViewModel> permissions;
    private Collection<RoleOwnershipViewModel> ownedRoles;
    private Double maxCreditNoteAmountApprovalLimit;
    private Double maxDebitNoteAmountApprovalLimit;
    private String notificationMechanism;


    
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

    public Collection<RolePermissionViewModel> getPermissions() {
        return permissions;
    }

    public void setPermissions(Collection<RolePermissionViewModel> permissions) {
        this.permissions = permissions;
    }


    public Double getMaxCreditNoteAmountApprovalLimit() {
        return maxCreditNoteAmountApprovalLimit;
    }

    public void setMaxCreditNoteAmountApprovalLimit(Double maxCreditNoteAmountApprovalLimit) {
        this.maxCreditNoteAmountApprovalLimit = maxCreditNoteAmountApprovalLimit;
    }

    public Double getMaxDebitNoteAmountApprovalLimit() {
        return maxDebitNoteAmountApprovalLimit;
    }

    public void setMaxDebitNoteAmountApprovalLimit(Double maxDebitNoteAmountApprovalLimit) {
        this.maxDebitNoteAmountApprovalLimit = maxDebitNoteAmountApprovalLimit;
    }

    public String getNotificationMechanism() {
        return notificationMechanism;
    }

    public void setNotificationMechanism(String notificationMechanism) {
        this.notificationMechanism = notificationMechanism;
    }

    public Collection<RoleOwnershipViewModel> getOwnedRoles() {
        return ownedRoles;
    }

    public void setOwnedRoles(Collection<RoleOwnershipViewModel> ownedRoles) {
        this.ownedRoles = ownedRoles;
    }

    @Override
    public String toString() {
        return "RoleViewModel{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}
