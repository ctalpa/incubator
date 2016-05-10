package com.ct.rxm.web.rest;

import com.ct.rxm.RxmenApp;
import com.ct.rxm.domain.Task;
import com.ct.rxm.repository.TaskRepository;
import com.ct.rxm.service.TaskService;
import com.ct.rxm.repository.search.TaskSearchRepository;
import com.ct.rxm.web.rest.dto.TaskDTO;
import com.ct.rxm.web.rest.mapper.TaskMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ct.rxm.domain.enumeration.TaskStatus;
import com.ct.rxm.domain.enumeration.TaskPriority;
import com.ct.rxm.domain.enumeration.TaskType;

/**
 * Test class for the TaskResource REST controller.
 *
 * @see TaskResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RxmenApp.class)
@WebAppConfiguration
@IntegrationTest
public class TaskResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Long DEFAULT_TASK_ID = 1L;
    private static final Long UPDATED_TASK_ID = 2L;
    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final ZonedDateTime DEFAULT_TASK_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_TASK_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_TASK_DATE_STR = dateTimeFormatter.format(DEFAULT_TASK_DATE);

    private static final TaskStatus DEFAULT_TASK_STATUS = TaskStatus.CREATED;
    private static final TaskStatus UPDATED_TASK_STATUS = TaskStatus.RUNNING;

    private static final TaskPriority DEFAULT_TASK_PRIORITY = TaskPriority.LOW;
    private static final TaskPriority UPDATED_TASK_PRIORITY = TaskPriority.MEDIUM;

    private static final TaskType DEFAULT_TASK_TYPE = TaskType.PLANNED;
    private static final TaskType UPDATED_TASK_TYPE = TaskType.UNEXPECTED;

    @Inject
    private TaskRepository taskRepository;

    @Inject
    private TaskMapper taskMapper;

    @Inject
    private TaskService taskService;

    @Inject
    private TaskSearchRepository taskSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTaskMockMvc;

    private Task task;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TaskResource taskResource = new TaskResource();
        ReflectionTestUtils.setField(taskResource, "taskService", taskService);
        ReflectionTestUtils.setField(taskResource, "taskMapper", taskMapper);
        this.restTaskMockMvc = MockMvcBuilders.standaloneSetup(taskResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        taskSearchRepository.deleteAll();
        task = new Task();
        task.setTaskId(DEFAULT_TASK_ID);
        task.setTitle(DEFAULT_TITLE);
        task.setDescription(DEFAULT_DESCRIPTION);
        task.setTaskDate(DEFAULT_TASK_DATE);
        task.setTaskStatus(DEFAULT_TASK_STATUS);
        task.setTaskPriority(DEFAULT_TASK_PRIORITY);
        task.setTaskType(DEFAULT_TASK_TYPE);
    }

    @Test
    @Transactional
    public void createTask() throws Exception {
        int databaseSizeBeforeCreate = taskRepository.findAll().size();

        // Create the Task
        TaskDTO taskDTO = taskMapper.taskToTaskDTO(task);

        restTaskMockMvc.perform(post("/api/tasks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskDTO)))
                .andExpect(status().isCreated());

        // Validate the Task in the database
        List<Task> tasks = taskRepository.findAll();
        assertThat(tasks).hasSize(databaseSizeBeforeCreate + 1);
        Task testTask = tasks.get(tasks.size() - 1);
        assertThat(testTask.getTaskId()).isEqualTo(DEFAULT_TASK_ID);
        assertThat(testTask.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTask.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTask.getTaskDate()).isEqualTo(DEFAULT_TASK_DATE);
        assertThat(testTask.getTaskStatus()).isEqualTo(DEFAULT_TASK_STATUS);
        assertThat(testTask.getTaskPriority()).isEqualTo(DEFAULT_TASK_PRIORITY);
        assertThat(testTask.getTaskType()).isEqualTo(DEFAULT_TASK_TYPE);

        // Validate the Task in ElasticSearch
        Task taskEs = taskSearchRepository.findOne(testTask.getId());
        assertThat(taskEs).isEqualToComparingFieldByField(testTask);
    }

    @Test
    @Transactional
    public void getAllTasks() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the tasks
        restTaskMockMvc.perform(get("/api/tasks?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
                .andExpect(jsonPath("$.[*].taskId").value(hasItem(DEFAULT_TASK_ID.intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].taskDate").value(hasItem(DEFAULT_TASK_DATE_STR)))
                .andExpect(jsonPath("$.[*].taskStatus").value(hasItem(DEFAULT_TASK_STATUS.toString())))
                .andExpect(jsonPath("$.[*].taskPriority").value(hasItem(DEFAULT_TASK_PRIORITY.toString())))
                .andExpect(jsonPath("$.[*].taskType").value(hasItem(DEFAULT_TASK_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get the task
        restTaskMockMvc.perform(get("/api/tasks/{id}", task.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(task.getId().intValue()))
            .andExpect(jsonPath("$.taskId").value(DEFAULT_TASK_ID.intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.taskDate").value(DEFAULT_TASK_DATE_STR))
            .andExpect(jsonPath("$.taskStatus").value(DEFAULT_TASK_STATUS.toString()))
            .andExpect(jsonPath("$.taskPriority").value(DEFAULT_TASK_PRIORITY.toString()))
            .andExpect(jsonPath("$.taskType").value(DEFAULT_TASK_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTask() throws Exception {
        // Get the task
        restTaskMockMvc.perform(get("/api/tasks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);
        taskSearchRepository.save(task);
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Update the task
        Task updatedTask = new Task();
        updatedTask.setId(task.getId());
        updatedTask.setTaskId(UPDATED_TASK_ID);
        updatedTask.setTitle(UPDATED_TITLE);
        updatedTask.setDescription(UPDATED_DESCRIPTION);
        updatedTask.setTaskDate(UPDATED_TASK_DATE);
        updatedTask.setTaskStatus(UPDATED_TASK_STATUS);
        updatedTask.setTaskPriority(UPDATED_TASK_PRIORITY);
        updatedTask.setTaskType(UPDATED_TASK_TYPE);
        TaskDTO taskDTO = taskMapper.taskToTaskDTO(updatedTask);

        restTaskMockMvc.perform(put("/api/tasks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(taskDTO)))
                .andExpect(status().isOk());

        // Validate the Task in the database
        List<Task> tasks = taskRepository.findAll();
        assertThat(tasks).hasSize(databaseSizeBeforeUpdate);
        Task testTask = tasks.get(tasks.size() - 1);
        assertThat(testTask.getTaskId()).isEqualTo(UPDATED_TASK_ID);
        assertThat(testTask.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTask.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTask.getTaskDate()).isEqualTo(UPDATED_TASK_DATE);
        assertThat(testTask.getTaskStatus()).isEqualTo(UPDATED_TASK_STATUS);
        assertThat(testTask.getTaskPriority()).isEqualTo(UPDATED_TASK_PRIORITY);
        assertThat(testTask.getTaskType()).isEqualTo(UPDATED_TASK_TYPE);

        // Validate the Task in ElasticSearch
        Task taskEs = taskSearchRepository.findOne(testTask.getId());
        assertThat(taskEs).isEqualToComparingFieldByField(testTask);
    }

    @Test
    @Transactional
    public void deleteTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);
        taskSearchRepository.save(task);
        int databaseSizeBeforeDelete = taskRepository.findAll().size();

        // Get the task
        restTaskMockMvc.perform(delete("/api/tasks/{id}", task.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean taskExistsInEs = taskSearchRepository.exists(task.getId());
        assertThat(taskExistsInEs).isFalse();

        // Validate the database is empty
        List<Task> tasks = taskRepository.findAll();
        assertThat(tasks).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);
        taskSearchRepository.save(task);

        // Search the task
        restTaskMockMvc.perform(get("/api/_search/tasks?query=id:" + task.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
            .andExpect(jsonPath("$.[*].taskId").value(hasItem(DEFAULT_TASK_ID.intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].taskDate").value(hasItem(DEFAULT_TASK_DATE_STR)))
            .andExpect(jsonPath("$.[*].taskStatus").value(hasItem(DEFAULT_TASK_STATUS.toString())))
            .andExpect(jsonPath("$.[*].taskPriority").value(hasItem(DEFAULT_TASK_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].taskType").value(hasItem(DEFAULT_TASK_TYPE.toString())));
    }
}
