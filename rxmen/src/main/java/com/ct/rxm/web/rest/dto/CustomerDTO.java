package com.ct.rxm.web.rest.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Customer entity.
 */
public class CustomerDTO implements Serializable {

    private Long id;

    private Long custumerId;


    private String name;


    private String description;


    private Long jobHistoryId;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getCustumerId() {
        return custumerId;
    }

    public void setCustumerId(Long custumerId) {
        this.custumerId = custumerId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getJobHistoryId() {
        return jobHistoryId;
    }

    public void setJobHistoryId(Long jobHistoryId) {
        this.jobHistoryId = jobHistoryId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomerDTO customerDTO = (CustomerDTO) o;

        if ( ! Objects.equals(id, customerDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CustomerDTO{" +
            "id=" + id +
            ", custumerId='" + custumerId + "'" +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
