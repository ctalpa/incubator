package it.ctalpa.planning.modules.Contact;

import it.ctalpa.planning.modules.Stakeholder.Stakeholder;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by c.talpa on 05/05/2017.
 */
@Entity
public class Contact implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private ContactType contactType;

    private String contact;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="STAKEHOLDER_ID")
    private Stakeholder stakeholder;

}
