package it.ctalpa.planning.modules.Stakeholder;

import it.ctalpa.planning.modules.Contact.Address;
import it.ctalpa.planning.modules.Contact.Contact;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by c.talpa on 05/05/2017.
 */
@Entity
public class Stakeholder implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

}
