package com.ct.rxm.web.rest;

import com.ct.rxm.RxmenApp;
import com.ct.rxm.domain.DayOff;
import com.ct.rxm.repository.DayOffRepository;
import com.ct.rxm.repository.search.DayOffSearchRepository;

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

import com.ct.rxm.domain.enumeration.DayOffType;

/**
 * Test class for the DayOffResource REST controller.
 *
 * @see DayOffResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RxmenApp.class)
@WebAppConfiguration
@IntegrationTest
public class DayOffResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Long DEFAULT_DAY_ID = 1L;
    private static final Long UPDATED_DAY_ID = 2L;

    private static final DayOffType DEFAULT_DAY_OFF_TYPE = DayOffType.HOLIDAY;
    private static final DayOffType UPDATED_DAY_OFF_TYPE = DayOffType.SICKNESS;
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_START_DATE_STR = dateTimeFormatter.format(DEFAULT_START_DATE);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_END_DATE_STR = dateTimeFormatter.format(DEFAULT_END_DATE);

    @Inject
    private DayOffRepository dayOffRepository;

    @Inject
    private DayOffSearchRepository dayOffSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDayOffMockMvc;

    private DayOff dayOff;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DayOffResource dayOffResource = new DayOffResource();
        ReflectionTestUtils.setField(dayOffResource, "dayOffSearchRepository", dayOffSearchRepository);
        ReflectionTestUtils.setField(dayOffResource, "dayOffRepository", dayOffRepository);
        this.restDayOffMockMvc = MockMvcBuilders.standaloneSetup(dayOffResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        dayOffSearchRepository.deleteAll();
        dayOff = new DayOff();
        dayOff.setDayId(DEFAULT_DAY_ID);
        dayOff.setDayOffType(DEFAULT_DAY_OFF_TYPE);
        dayOff.setDescription(DEFAULT_DESCRIPTION);
        dayOff.setStartDate(DEFAULT_START_DATE);
        dayOff.setEndDate(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void createDayOff() throws Exception {
        int databaseSizeBeforeCreate = dayOffRepository.findAll().size();

        // Create the DayOff

        restDayOffMockMvc.perform(post("/api/day-offs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dayOff)))
                .andExpect(status().isCreated());

        // Validate the DayOff in the database
        List<DayOff> dayOffs = dayOffRepository.findAll();
        assertThat(dayOffs).hasSize(databaseSizeBeforeCreate + 1);
        DayOff testDayOff = dayOffs.get(dayOffs.size() - 1);
        assertThat(testDayOff.getDayId()).isEqualTo(DEFAULT_DAY_ID);
        assertThat(testDayOff.getDayOffType()).isEqualTo(DEFAULT_DAY_OFF_TYPE);
        assertThat(testDayOff.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDayOff.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testDayOff.getEndDate()).isEqualTo(DEFAULT_END_DATE);

        // Validate the DayOff in ElasticSearch
        DayOff dayOffEs = dayOffSearchRepository.findOne(testDayOff.getId());
        assertThat(dayOffEs).isEqualToComparingFieldByField(testDayOff);
    }

    @Test
    @Transactional
    public void getAllDayOffs() throws Exception {
        // Initialize the database
        dayOffRepository.saveAndFlush(dayOff);

        // Get all the dayOffs
        restDayOffMockMvc.perform(get("/api/day-offs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dayOff.getId().intValue())))
                .andExpect(jsonPath("$.[*].dayId").value(hasItem(DEFAULT_DAY_ID.intValue())))
                .andExpect(jsonPath("$.[*].dayOffType").value(hasItem(DEFAULT_DAY_OFF_TYPE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE_STR)))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE_STR)));
    }

    @Test
    @Transactional
    public void getDayOff() throws Exception {
        // Initialize the database
        dayOffRepository.saveAndFlush(dayOff);

        // Get the dayOff
        restDayOffMockMvc.perform(get("/api/day-offs/{id}", dayOff.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(dayOff.getId().intValue()))
            .andExpect(jsonPath("$.dayId").value(DEFAULT_DAY_ID.intValue()))
            .andExpect(jsonPath("$.dayOffType").value(DEFAULT_DAY_OFF_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE_STR))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingDayOff() throws Exception {
        // Get the dayOff
        restDayOffMockMvc.perform(get("/api/day-offs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDayOff() throws Exception {
        // Initialize the database
        dayOffRepository.saveAndFlush(dayOff);
        dayOffSearchRepository.save(dayOff);
        int databaseSizeBeforeUpdate = dayOffRepository.findAll().size();

        // Update the dayOff
        DayOff updatedDayOff = new DayOff();
        updatedDayOff.setId(dayOff.getId());
        updatedDayOff.setDayId(UPDATED_DAY_ID);
        updatedDayOff.setDayOffType(UPDATED_DAY_OFF_TYPE);
        updatedDayOff.setDescription(UPDATED_DESCRIPTION);
        updatedDayOff.setStartDate(UPDATED_START_DATE);
        updatedDayOff.setEndDate(UPDATED_END_DATE);

        restDayOffMockMvc.perform(put("/api/day-offs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDayOff)))
                .andExpect(status().isOk());

        // Validate the DayOff in the database
        List<DayOff> dayOffs = dayOffRepository.findAll();
        assertThat(dayOffs).hasSize(databaseSizeBeforeUpdate);
        DayOff testDayOff = dayOffs.get(dayOffs.size() - 1);
        assertThat(testDayOff.getDayId()).isEqualTo(UPDATED_DAY_ID);
        assertThat(testDayOff.getDayOffType()).isEqualTo(UPDATED_DAY_OFF_TYPE);
        assertThat(testDayOff.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDayOff.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testDayOff.getEndDate()).isEqualTo(UPDATED_END_DATE);

        // Validate the DayOff in ElasticSearch
        DayOff dayOffEs = dayOffSearchRepository.findOne(testDayOff.getId());
        assertThat(dayOffEs).isEqualToComparingFieldByField(testDayOff);
    }

    @Test
    @Transactional
    public void deleteDayOff() throws Exception {
        // Initialize the database
        dayOffRepository.saveAndFlush(dayOff);
        dayOffSearchRepository.save(dayOff);
        int databaseSizeBeforeDelete = dayOffRepository.findAll().size();

        // Get the dayOff
        restDayOffMockMvc.perform(delete("/api/day-offs/{id}", dayOff.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean dayOffExistsInEs = dayOffSearchRepository.exists(dayOff.getId());
        assertThat(dayOffExistsInEs).isFalse();

        // Validate the database is empty
        List<DayOff> dayOffs = dayOffRepository.findAll();
        assertThat(dayOffs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDayOff() throws Exception {
        // Initialize the database
        dayOffRepository.saveAndFlush(dayOff);
        dayOffSearchRepository.save(dayOff);

        // Search the dayOff
        restDayOffMockMvc.perform(get("/api/_search/day-offs?query=id:" + dayOff.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dayOff.getId().intValue())))
            .andExpect(jsonPath("$.[*].dayId").value(hasItem(DEFAULT_DAY_ID.intValue())))
            .andExpect(jsonPath("$.[*].dayOffType").value(hasItem(DEFAULT_DAY_OFF_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE_STR)))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE_STR)));
    }
}
