package com.ctalpa.prxmen.web.rest;

import com.ctalpa.prxmen.PrxmenApp;
import com.ctalpa.prxmen.domain.CompanyVehicles;
import com.ctalpa.prxmen.repository.CompanyVehiclesRepository;
import com.ctalpa.prxmen.repository.search.CompanyVehiclesSearchRepository;

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


/**
 * Test class for the CompanyVehiclesResource REST controller.
 *
 * @see CompanyVehiclesResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PrxmenApp.class)
@WebAppConfiguration
@IntegrationTest
public class CompanyVehiclesResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Long DEFAULT_COMPANY_VEHICLES_ID = 1L;
    private static final Long UPDATED_COMPANY_VEHICLES_ID = 2L;
    private static final String DEFAULT_NUMBER_PLATE = "AAAAA";
    private static final String UPDATED_NUMBER_PLATE = "BBBBB";
    private static final String DEFAULT_VENDOR_VEHICLE = "AAAAA";
    private static final String UPDATED_VENDOR_VEHICLE = "BBBBB";
    private static final String DEFAULT_MODEL_VEHICLES = "AAAAA";
    private static final String UPDATED_MODEL_VEHICLES = "BBBBB";

    private static final ZonedDateTime DEFAULT_INSURANCE_EXPIRATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_INSURANCE_EXPIRATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_INSURANCE_EXPIRATION_DATE_STR = dateTimeFormatter.format(DEFAULT_INSURANCE_EXPIRATION_DATE);

    @Inject
    private CompanyVehiclesRepository companyVehiclesRepository;

    @Inject
    private CompanyVehiclesSearchRepository companyVehiclesSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCompanyVehiclesMockMvc;

    private CompanyVehicles companyVehicles;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompanyVehiclesResource companyVehiclesResource = new CompanyVehiclesResource();
        ReflectionTestUtils.setField(companyVehiclesResource, "companyVehiclesSearchRepository", companyVehiclesSearchRepository);
        ReflectionTestUtils.setField(companyVehiclesResource, "companyVehiclesRepository", companyVehiclesRepository);
        this.restCompanyVehiclesMockMvc = MockMvcBuilders.standaloneSetup(companyVehiclesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        companyVehiclesSearchRepository.deleteAll();
        companyVehicles = new CompanyVehicles();
        companyVehicles.setCompanyVehiclesId(DEFAULT_COMPANY_VEHICLES_ID);
        companyVehicles.setNumberPlate(DEFAULT_NUMBER_PLATE);
        companyVehicles.setVendorVehicle(DEFAULT_VENDOR_VEHICLE);
        companyVehicles.setModelVehicles(DEFAULT_MODEL_VEHICLES);
        companyVehicles.setInsuranceExpirationDate(DEFAULT_INSURANCE_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    public void createCompanyVehicles() throws Exception {
        int databaseSizeBeforeCreate = companyVehiclesRepository.findAll().size();

        // Create the CompanyVehicles

        restCompanyVehiclesMockMvc.perform(post("/api/company-vehicles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(companyVehicles)))
                .andExpect(status().isCreated());

        // Validate the CompanyVehicles in the database
        List<CompanyVehicles> companyVehicles = companyVehiclesRepository.findAll();
        assertThat(companyVehicles).hasSize(databaseSizeBeforeCreate + 1);
        CompanyVehicles testCompanyVehicles = companyVehicles.get(companyVehicles.size() - 1);
        assertThat(testCompanyVehicles.getCompanyVehiclesId()).isEqualTo(DEFAULT_COMPANY_VEHICLES_ID);
        assertThat(testCompanyVehicles.getNumberPlate()).isEqualTo(DEFAULT_NUMBER_PLATE);
        assertThat(testCompanyVehicles.getVendorVehicle()).isEqualTo(DEFAULT_VENDOR_VEHICLE);
        assertThat(testCompanyVehicles.getModelVehicles()).isEqualTo(DEFAULT_MODEL_VEHICLES);
        assertThat(testCompanyVehicles.getInsuranceExpirationDate()).isEqualTo(DEFAULT_INSURANCE_EXPIRATION_DATE);

        // Validate the CompanyVehicles in ElasticSearch
        CompanyVehicles companyVehiclesEs = companyVehiclesSearchRepository.findOne(testCompanyVehicles.getId());
        assertThat(companyVehiclesEs).isEqualToComparingFieldByField(testCompanyVehicles);
    }

    @Test
    @Transactional
    public void getAllCompanyVehicles() throws Exception {
        // Initialize the database
        companyVehiclesRepository.saveAndFlush(companyVehicles);

        // Get all the companyVehicles
        restCompanyVehiclesMockMvc.perform(get("/api/company-vehicles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(companyVehicles.getId().intValue())))
                .andExpect(jsonPath("$.[*].companyVehiclesId").value(hasItem(DEFAULT_COMPANY_VEHICLES_ID.intValue())))
                .andExpect(jsonPath("$.[*].numberPlate").value(hasItem(DEFAULT_NUMBER_PLATE.toString())))
                .andExpect(jsonPath("$.[*].vendorVehicle").value(hasItem(DEFAULT_VENDOR_VEHICLE.toString())))
                .andExpect(jsonPath("$.[*].modelVehicles").value(hasItem(DEFAULT_MODEL_VEHICLES.toString())))
                .andExpect(jsonPath("$.[*].insuranceExpirationDate").value(hasItem(DEFAULT_INSURANCE_EXPIRATION_DATE_STR)));
    }

    @Test
    @Transactional
    public void getCompanyVehicles() throws Exception {
        // Initialize the database
        companyVehiclesRepository.saveAndFlush(companyVehicles);

        // Get the companyVehicles
        restCompanyVehiclesMockMvc.perform(get("/api/company-vehicles/{id}", companyVehicles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(companyVehicles.getId().intValue()))
            .andExpect(jsonPath("$.companyVehiclesId").value(DEFAULT_COMPANY_VEHICLES_ID.intValue()))
            .andExpect(jsonPath("$.numberPlate").value(DEFAULT_NUMBER_PLATE.toString()))
            .andExpect(jsonPath("$.vendorVehicle").value(DEFAULT_VENDOR_VEHICLE.toString()))
            .andExpect(jsonPath("$.modelVehicles").value(DEFAULT_MODEL_VEHICLES.toString()))
            .andExpect(jsonPath("$.insuranceExpirationDate").value(DEFAULT_INSURANCE_EXPIRATION_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingCompanyVehicles() throws Exception {
        // Get the companyVehicles
        restCompanyVehiclesMockMvc.perform(get("/api/company-vehicles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompanyVehicles() throws Exception {
        // Initialize the database
        companyVehiclesRepository.saveAndFlush(companyVehicles);
        companyVehiclesSearchRepository.save(companyVehicles);
        int databaseSizeBeforeUpdate = companyVehiclesRepository.findAll().size();

        // Update the companyVehicles
        CompanyVehicles updatedCompanyVehicles = new CompanyVehicles();
        updatedCompanyVehicles.setId(companyVehicles.getId());
        updatedCompanyVehicles.setCompanyVehiclesId(UPDATED_COMPANY_VEHICLES_ID);
        updatedCompanyVehicles.setNumberPlate(UPDATED_NUMBER_PLATE);
        updatedCompanyVehicles.setVendorVehicle(UPDATED_VENDOR_VEHICLE);
        updatedCompanyVehicles.setModelVehicles(UPDATED_MODEL_VEHICLES);
        updatedCompanyVehicles.setInsuranceExpirationDate(UPDATED_INSURANCE_EXPIRATION_DATE);

        restCompanyVehiclesMockMvc.perform(put("/api/company-vehicles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCompanyVehicles)))
                .andExpect(status().isOk());

        // Validate the CompanyVehicles in the database
        List<CompanyVehicles> companyVehicles = companyVehiclesRepository.findAll();
        assertThat(companyVehicles).hasSize(databaseSizeBeforeUpdate);
        CompanyVehicles testCompanyVehicles = companyVehicles.get(companyVehicles.size() - 1);
        assertThat(testCompanyVehicles.getCompanyVehiclesId()).isEqualTo(UPDATED_COMPANY_VEHICLES_ID);
        assertThat(testCompanyVehicles.getNumberPlate()).isEqualTo(UPDATED_NUMBER_PLATE);
        assertThat(testCompanyVehicles.getVendorVehicle()).isEqualTo(UPDATED_VENDOR_VEHICLE);
        assertThat(testCompanyVehicles.getModelVehicles()).isEqualTo(UPDATED_MODEL_VEHICLES);
        assertThat(testCompanyVehicles.getInsuranceExpirationDate()).isEqualTo(UPDATED_INSURANCE_EXPIRATION_DATE);

        // Validate the CompanyVehicles in ElasticSearch
        CompanyVehicles companyVehiclesEs = companyVehiclesSearchRepository.findOne(testCompanyVehicles.getId());
        assertThat(companyVehiclesEs).isEqualToComparingFieldByField(testCompanyVehicles);
    }

    @Test
    @Transactional
    public void deleteCompanyVehicles() throws Exception {
        // Initialize the database
        companyVehiclesRepository.saveAndFlush(companyVehicles);
        companyVehiclesSearchRepository.save(companyVehicles);
        int databaseSizeBeforeDelete = companyVehiclesRepository.findAll().size();

        // Get the companyVehicles
        restCompanyVehiclesMockMvc.perform(delete("/api/company-vehicles/{id}", companyVehicles.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean companyVehiclesExistsInEs = companyVehiclesSearchRepository.exists(companyVehicles.getId());
        assertThat(companyVehiclesExistsInEs).isFalse();

        // Validate the database is empty
        List<CompanyVehicles> companyVehicles = companyVehiclesRepository.findAll();
        assertThat(companyVehicles).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCompanyVehicles() throws Exception {
        // Initialize the database
        companyVehiclesRepository.saveAndFlush(companyVehicles);
        companyVehiclesSearchRepository.save(companyVehicles);

        // Search the companyVehicles
        restCompanyVehiclesMockMvc.perform(get("/api/_search/company-vehicles?query=id:" + companyVehicles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyVehicles.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyVehiclesId").value(hasItem(DEFAULT_COMPANY_VEHICLES_ID.intValue())))
            .andExpect(jsonPath("$.[*].numberPlate").value(hasItem(DEFAULT_NUMBER_PLATE.toString())))
            .andExpect(jsonPath("$.[*].vendorVehicle").value(hasItem(DEFAULT_VENDOR_VEHICLE.toString())))
            .andExpect(jsonPath("$.[*].modelVehicles").value(hasItem(DEFAULT_MODEL_VEHICLES.toString())))
            .andExpect(jsonPath("$.[*].insuranceExpirationDate").value(hasItem(DEFAULT_INSURANCE_EXPIRATION_DATE_STR)));
    }
}
