package com.ct.rxm.web.rest.mapper;

import com.ct.rxm.domain.*;
import com.ct.rxm.web.rest.dto.JobHistoryDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity JobHistory and its DTO JobHistoryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface JobHistoryMapper {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "employee.id", target = "employeeId")
    JobHistoryDTO jobHistoryToJobHistoryDTO(JobHistory jobHistory);

    List<JobHistoryDTO> jobHistoriesToJobHistoryDTOs(List<JobHistory> jobHistories);

    @Mapping(source = "customerId", target = "customer")
    @Mapping(source = "employeeId", target = "employee")
    JobHistory jobHistoryDTOToJobHistory(JobHistoryDTO jobHistoryDTO);

    List<JobHistory> jobHistoryDTOsToJobHistories(List<JobHistoryDTO> jobHistoryDTOs);

    default Customer customerFromId(Long id) {
        if (id == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }

    default Employee employeeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }
}
