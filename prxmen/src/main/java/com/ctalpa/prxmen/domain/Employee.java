package com.ctalpa.prxmen.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * The Employee entity.                                                        
 * 
 */
@ApiModel(description = ""
    + "The Employee entity.                                                   "
    + "")
@Entity
@Table(name = "employee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "employee_id")
    private Long employeeId;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "business_phone")
    private String businessPhone;

    @Column(name = "phiscalcode")
    private String phiscalcode;

    @Column(name = "birth_day")
    private ZonedDateTime birthDay;

    @Column(name = "hire_date")
    private ZonedDateTime hireDate;

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Job> employeesToJobs = new HashSet<>();

    @OneToMany(mappedBy = "employee")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<JobHistory> employeesToJobHistories = new HashSet<>();

    @ManyToOne
    private RestWorkDays restWorkDays;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBusinessPhone() {
        return businessPhone;
    }

    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }

    public String getPhiscalcode() {
        return phiscalcode;
    }

    public void setPhiscalcode(String phiscalcode) {
        this.phiscalcode = phiscalcode;
    }

    public ZonedDateTime getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(ZonedDateTime birthDay) {
        this.birthDay = birthDay;
    }

    public ZonedDateTime getHireDate() {
        return hireDate;
    }

    public void setHireDate(ZonedDateTime hireDate) {
        this.hireDate = hireDate;
    }

    public Set<Job> getEmployeesToJobs() {
        return employeesToJobs;
    }

    public void setEmployeesToJobs(Set<Job> jobs) {
        this.employeesToJobs = jobs;
    }

    public Set<JobHistory> getEmployeesToJobHistories() {
        return employeesToJobHistories;
    }

    public void setEmployeesToJobHistories(Set<JobHistory> jobHistories) {
        this.employeesToJobHistories = jobHistories;
    }

    public RestWorkDays getRestWorkDays() {
        return restWorkDays;
    }

    public void setRestWorkDays(RestWorkDays restWorkDays) {
        this.restWorkDays = restWorkDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee employee = (Employee) o;
        if(employee.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, employee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Employee{" +
            "id=" + id +
            ", employeeId='" + employeeId + "'" +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", email='" + email + "'" +
            ", phone='" + phone + "'" +
            ", businessPhone='" + businessPhone + "'" +
            ", phiscalcode='" + phiscalcode + "'" +
            ", birthDay='" + birthDay + "'" +
            ", hireDate='" + hireDate + "'" +
            '}';
    }
}