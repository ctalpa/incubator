package com.ctalpa.prxmen.web.rest.mapper;

import com.ctalpa.prxmen.domain.*;
import com.ctalpa.prxmen.web.rest.dto.JobDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Job and its DTO JobDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface JobMapper {

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "task.id", target = "taskId")
    JobDTO jobToJobDTO(Job job);

    List<JobDTO> jobsToJobDTOs(List<Job> jobs);

    @Mapping(source = "employeeId", target = "employee")
    @Mapping(source = "taskId", target = "task")
    Job jobDTOToJob(JobDTO jobDTO);

    List<Job> jobDTOsToJobs(List<JobDTO> jobDTOs);

    default Employee employeeFromId(Long id) {
        if (id == null) {
            return null;
        }
        Employee employee = new Employee();
        employee.setId(id);
        return employee;
    }

    default Task taskFromId(Long id) {
        if (id == null) {
            return null;
        }
        Task task = new Task();
        task.setId(id);
        return task;
    }
}
