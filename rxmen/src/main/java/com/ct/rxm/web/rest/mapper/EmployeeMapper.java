package com.ct.rxm.web.rest.mapper;

import com.ct.rxm.domain.*;
import com.ct.rxm.web.rest.dto.EmployeeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Employee and its DTO EmployeeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EmployeeMapper {

    @Mapping(source = "restWorkDays.id", target = "restWorkDaysId")
    @Mapping(source = "job.id", target = "jobId")
    @Mapping(source = "jobHistory.id", target = "jobHistoryId")
    EmployeeDTO employeeToEmployeeDTO(Employee employee);

    List<EmployeeDTO> employeesToEmployeeDTOs(List<Employee> employees);

    @Mapping(source = "restWorkDaysId", target = "restWorkDays")
    @Mapping(source = "jobId", target = "job")
    @Mapping(source = "jobHistoryId", target = "jobHistory")
    Employee employeeDTOToEmployee(EmployeeDTO employeeDTO);

    List<Employee> employeeDTOsToEmployees(List<EmployeeDTO> employeeDTOs);

    default RestWorkDays restWorkDaysFromId(Long id) {
        if (id == null) {
            return null;
        }
        RestWorkDays restWorkDays = new RestWorkDays();
        restWorkDays.setId(id);
        return restWorkDays;
    }

    default Job jobFromId(Long id) {
        if (id == null) {
            return null;
        }
        Job job = new Job();
        job.setId(id);
        return job;
    }

    default JobHistory jobHistoryFromId(Long id) {
        if (id == null) {
            return null;
        }
        JobHistory jobHistory = new JobHistory();
        jobHistory.setId(id);
        return jobHistory;
    }
}
