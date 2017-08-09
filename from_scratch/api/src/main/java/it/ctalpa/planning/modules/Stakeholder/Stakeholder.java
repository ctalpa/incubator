package it.ctalpa.planning.modules.Stakeholder;

import it.ctalpa.planning.modules.Contact.Address;
import it.ctalpa.planning.modules.Contact.Contact;
import it.ctalpa.planning.util.AuditedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by c.talpa on 05/05/2017.
 */
@Entity
@Table(name="stakeholder")
public class Stakeholder extends AuditedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private StakeholderType stakeholderType;

    private String name;

    private String vatNumber;

    private String taxCode;

    @OneToOne
    @JoinColumn(name = "ADDRESS_ID")
    private Address address;

    @OneToMany(mappedBy = "stakeholder")
    private List<Contact> contacts;

    private String description;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StakeholderType getStakeholderType() {
        return stakeholderType;
    }

    public void setStakeholderType(StakeholderType stakeholderType) {
        this.stakeholderType = stakeholderType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Stakeholder{" +
                "id=" + id +
                ", stakeholderType=" + stakeholderType +
                ", name='" + name + '\'' +
                ", vatNumber='" + vatNumber + '\'' +
                ", taxCode='" + taxCode + '\'' +
                '}';
    }
}
