package it.ctalpa.planning.modules.Contact;

import javax.persistence.Id;

/**
 * Created by c.talpa on 24/05/2017.
 */
public class ContactDTO {

    @Id
    private Long id;

    private ContactType contactType;

    private String contact;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContactType getContactType() {
        return contactType;
    }

    public void setContactType(ContactType contactType) {
        this.contactType = contactType;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
