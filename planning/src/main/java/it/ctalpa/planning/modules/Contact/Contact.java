package it.ctalpa.planning.modules.Contact;

import it.ctalpa.planning.modules.Stakeholder.Stakeholder;
import it.ctalpa.planning.util.AuditedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by c.talpa on 05/05/2017.
 */
@Entity
@Table(name="contact")
public class Contact extends AuditedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private ContactType contactType;

    private String contact;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="STAKEHOLDER_ID")
    private Stakeholder stakeholder;

}
