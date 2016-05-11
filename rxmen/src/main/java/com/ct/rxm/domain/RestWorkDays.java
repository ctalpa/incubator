package com.ct.rxm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
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

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private DayOff dayOff;

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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public DayOff getDayOff() {
        return dayOff;
    }

    public void setDayOff(DayOff dayOff) {
        this.dayOff = dayOff;
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
