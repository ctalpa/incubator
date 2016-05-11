package com.ct.rxm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.ct.rxm.domain.enumeration.RestWorkDaysStatus;

/**
 * A RestWorkDays.
 */
@Entity
@Table(name = "rest_work_days")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "restworkdays")
public class RestWorkDays implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "rest_work_days_status")
    private RestWorkDaysStatus restWorkDaysStatus;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "restWorkDays")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Employee> restEmployees = new HashSet<>();

    @OneToMany(mappedBy = "restWorkDays")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DayOff> workDayOffs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RestWorkDaysStatus getRestWorkDaysStatus() {
        return restWorkDaysStatus;
    }

    public void setRestWorkDaysStatus(RestWorkDaysStatus restWorkDaysStatus) {
        this.restWorkDaysStatus = restWorkDaysStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Employee> getRestEmployees() {
        return restEmployees;
    }

    public void setRestEmployees(Set<Employee> employees) {
        this.restEmployees = employees;
    }

    public Set<DayOff> getWorkDayOffs() {
        return workDayOffs;
    }

    public void setWorkDayOffs(Set<DayOff> dayOffs) {
        this.workDayOffs = dayOffs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RestWorkDays restWorkDays = (RestWorkDays) o;
        if(restWorkDays.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, restWorkDays.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RestWorkDays{" +
            "id=" + id +
            ", restWorkDaysStatus='" + restWorkDaysStatus + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
