package com.ctalpa.prxmen.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A CompanyVehicles.
 */
@Entity
@Table(name = "company_vehicles")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "companyvehicles")
public class CompanyVehicles implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "company_vehicles_id")
    private Long companyVehiclesId;

    @Column(name = "number_plate")
    private String numberPlate;

    @Column(name = "vendor_vehicle")
    private String vendorVehicle;

    @Column(name = "model_vehicles")
    private String modelVehicles;

    @Column(name = "insurance_expiration_date")
    private ZonedDateTime insuranceExpirationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyVehiclesId() {
        return companyVehiclesId;
    }

    public void setCompanyVehiclesId(Long companyVehiclesId) {
        this.companyVehiclesId = companyVehiclesId;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getVendorVehicle() {
        return vendorVehicle;
    }

    public void setVendorVehicle(String vendorVehicle) {
        this.vendorVehicle = vendorVehicle;
    }

    public String getModelVehicles() {
        return modelVehicles;
    }

    public void setModelVehicles(String modelVehicles) {
        this.modelVehicles = modelVehicles;
    }

    public ZonedDateTime getInsuranceExpirationDate() {
        return insuranceExpirationDate;
    }

    public void setInsuranceExpirationDate(ZonedDateTime insuranceExpirationDate) {
        this.insuranceExpirationDate = insuranceExpirationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CompanyVehicles companyVehicles = (CompanyVehicles) o;
        if(companyVehicles.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, companyVehicles.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CompanyVehicles{" +
            "id=" + id +
            ", companyVehiclesId='" + companyVehiclesId + "'" +
            ", numberPlate='" + numberPlate + "'" +
            ", vendorVehicle='" + vendorVehicle + "'" +
            ", modelVehicles='" + modelVehicles + "'" +
            ", insuranceExpirationDate='" + insuranceExpirationDate + "'" +
            '}';
    }
}
