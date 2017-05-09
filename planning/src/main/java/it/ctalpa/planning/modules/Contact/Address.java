package it.ctalpa.planning.modules.Contact;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by c.talpa on 05/05/2017.
 */
@Entity
public class Address implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String street;

    private String streetNumber;

    private String city;

    private String country;

    private String region;

    private String province;

    private String locality;

    private String postalCode;

    private String note;

}
