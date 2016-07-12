package com.ctalpa.prxmen.web.rest.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.ctalpa.prxmen.domain.enumeration.TaskStatus;
import com.ctalpa.prxmen.domain.enumeration.TaskPriority;
import com.ctalpa.prxmen.domain.enumeration.TaskType;

/**
 * A DTO for the Task entity.
 */
public class TaskDTO implements Serializable {

    private Long id;

    private Long taskId;

    @NotNull
    private String title;

    private String description;

    private ZonedDateTime taskDate;

    private TaskStatus taskStatus;

    private TaskPriority taskPriority;

    private TaskType taskType;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public ZonedDateTime getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(ZonedDateTime taskDate) {
        this.taskDate = taskDate;
    }
    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }
    public TaskPriority getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(TaskPriority taskPriority) {
        this.taskPriority = taskPriority;
    }
    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TaskDTO taskDTO = (TaskDTO) o;

        if ( ! Objects.equals(id, taskDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
            "id=" + id +
            ", taskId='" + taskId + "'" +
            ", title='" + title + "'" +
            ", description='" + description + "'" +
            ", taskDate='" + taskDate + "'" +
            ", taskStatus='" + taskStatus + "'" +
            ", taskPriority='" + taskPriority + "'" +
            ", taskType='" + taskType + "'" +
            '}';
    }
}
