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
    EmployeeDTO employeeToEmployeeDTO(Employee employee);

    List<EmployeeDTO> employeesToEmployeeDTOs(List<Employee> employees);

    @Mapping(target = "employeesToJobs", ignore = true)
    @Mapping(target = "employeesToJobHistories", ignore = true)
    @Mapping(source = "restWorkDaysId", target = "restWorkDays")
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
}
