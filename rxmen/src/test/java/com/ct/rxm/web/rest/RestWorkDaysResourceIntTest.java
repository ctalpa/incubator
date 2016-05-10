package com.ct.rxm.web.rest;

import com.ct.rxm.RxmenApp;
import com.ct.rxm.domain.RestWorkDays;
import com.ct.rxm.repository.RestWorkDaysRepository;
import com.ct.rxm.repository.search.RestWorkDaysSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ct.rxm.domain.enumeration.RestWorkDaysStatus;

/**
 * Test class for the RestWorkDaysResource REST controller.
 *
 * @see RestWorkDaysResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RxmenApp.class)
@WebAppConfiguration
@IntegrationTest
public class RestWorkDaysResourceIntTest {


    private static final RestWorkDaysStatus DEFAULT_REST_WORK_DAYS_STATUS = RestWorkDaysStatus.PLANNED;
    private static final RestWorkDaysStatus UPDATED_REST_WORK_DAYS_STATUS = RestWorkDaysStatus.WAITING;
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private RestWorkDaysRepository restWorkDaysRepository;

    @Inject
    private RestWorkDaysSearchRepository restWorkDaysSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRestWorkDaysMockMvc;

    private RestWorkDays restWorkDays;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RestWorkDaysResource restWorkDaysResource = new RestWorkDaysResource();
        ReflectionTestUtils.setField(restWorkDaysResource, "restWorkDaysSearchRepository", restWorkDaysSearchRepository);
        ReflectionTestUtils.setField(restWorkDaysResource, "restWorkDaysRepository", restWorkDaysRepository);
        this.restRestWorkDaysMockMvc = MockMvcBuilders.standaloneSetup(restWorkDaysResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        restWorkDaysSearchRepository.deleteAll();
        restWorkDays = new RestWorkDays();
        restWorkDays.setRestWorkDaysStatus(DEFAULT_REST_WORK_DAYS_STATUS);
        restWorkDays.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createRestWorkDays() throws Exception {
        int databaseSizeBeforeCreate = restWorkDaysRepository.findAll().size();

        // Create the RestWorkDays

        restRestWorkDaysMockMvc.perform(post("/api/rest-work-days")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(restWorkDays)))
                .andExpect(status().isCreated());

        // Validate the RestWorkDays in the database
        List<RestWorkDays> restWorkDays = restWorkDaysRepository.findAll();
        assertThat(restWorkDays).hasSize(databaseSizeBeforeCreate + 1);
        RestWorkDays testRestWorkDays = restWorkDays.get(restWorkDays.size() - 1);
        assertThat(testRestWorkDays.getRestWorkDaysStatus()).isEqualTo(DEFAULT_REST_WORK_DAYS_STATUS);
        assertThat(testRestWorkDays.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the RestWorkDays in ElasticSearch
        RestWorkDays restWorkDaysEs = restWorkDaysSearchRepository.findOne(testRestWorkDays.getId());
        assertThat(restWorkDaysEs).isEqualToComparingFieldByField(testRestWorkDays);
    }

    @Test
    @Transactional
    public void getAllRestWorkDays() throws Exception {
        // Initialize the database
        restWorkDaysRepository.saveAndFlush(restWorkDays);

        // Get all the restWorkDays
        restRestWorkDaysMockMvc.perform(get("/api/rest-work-days?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(restWorkDays.getId().intValue())))
                .andExpect(jsonPath("$.[*].restWorkDaysStatus").value(hasItem(DEFAULT_REST_WORK_DAYS_STATUS.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getRestWorkDays() throws Exception {
        // Initialize the database
        restWorkDaysRepository.saveAndFlush(restWorkDays);

        // Get the restWorkDays
        restRestWorkDaysMockMvc.perform(get("/api/rest-work-days/{id}", restWorkDays.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(restWorkDays.getId().intValue()))
            .andExpect(jsonPath("$.restWorkDaysStatus").value(DEFAULT_REST_WORK_DAYS_STATUS.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRestWorkDays() throws Exception {
        // Get the restWorkDays
        restRestWorkDaysMockMvc.perform(get("/api/rest-work-days/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRestWorkDays() throws Exception {
        // Initialize the database
        restWorkDaysRepository.saveAndFlush(restWorkDays);
        restWorkDaysSearchRepository.save(restWorkDays);
        int databaseSizeBeforeUpdate = restWorkDaysRepository.findAll().size();

        // Update the restWorkDays
        RestWorkDays updatedRestWorkDays = new RestWorkDays();
        updatedRestWorkDays.setId(restWorkDays.getId());
        updatedRestWorkDays.setRestWorkDaysStatus(UPDATED_REST_WORK_DAYS_STATUS);
        updatedRestWorkDays.setDescription(UPDATED_DESCRIPTION);

        restRestWorkDaysMockMvc.perform(put("/api/rest-work-days")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRestWorkDays)))
                .andExpect(status().isOk());

        // Validate the RestWorkDays in the database
        List<RestWorkDays> restWorkDays = restWorkDaysRepository.findAll();
        assertThat(restWorkDays).hasSize(databaseSizeBeforeUpdate);
        RestWorkDays testRestWorkDays = restWorkDays.get(restWorkDays.size() - 1);
        assertThat(testRestWorkDays.getRestWorkDaysStatus()).isEqualTo(UPDATED_REST_WORK_DAYS_STATUS);
        assertThat(testRestWorkDays.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the RestWorkDays in ElasticSearch
        RestWorkDays restWorkDaysEs = restWorkDaysSearchRepository.findOne(testRestWorkDays.getId());
        assertThat(restWorkDaysEs).isEqualToComparingFieldByField(testRestWorkDays);
    }

    @Test
    @Transactional
    public void deleteRestWorkDays() throws Exception {
        // Initialize the database
        restWorkDaysRepository.saveAndFlush(restWorkDays);
        restWorkDaysSearchRepository.save(restWorkDays);
        int databaseSizeBeforeDelete = restWorkDaysRepository.findAll().size();

        // Get the restWorkDays
        restRestWorkDaysMockMvc.perform(delete("/api/rest-work-days/{id}", restWorkDays.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean restWorkDaysExistsInEs = restWorkDaysSearchRepository.exists(restWorkDays.getId());
        assertThat(restWorkDaysExistsInEs).isFalse();

        // Validate the database is empty
        List<RestWorkDays> restWorkDays = restWorkDaysRepository.findAll();
        assertThat(restWorkDays).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRestWorkDays() throws Exception {
        // Initialize the database
        restWorkDaysRepository.saveAndFlush(restWorkDays);
        restWorkDaysSearchRepository.save(restWorkDays);

        // Search the restWorkDays
        restRestWorkDaysMockMvc.perform(get("/api/_search/rest-work-days?query=id:" + restWorkDays.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restWorkDays.getId().intValue())))
            .andExpect(jsonPath("$.[*].restWorkDaysStatus").value(hasItem(DEFAULT_REST_WORK_DAYS_STATUS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
