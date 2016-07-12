package com.ctalpa.prxmen.web.rest;

import com.ctalpa.prxmen.PrxmenApp;
import com.ctalpa.prxmen.domain.Vendor;
import com.ctalpa.prxmen.repository.VendorRepository;
import com.ctalpa.prxmen.repository.search.VendorSearchRepository;

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


/**
 * Test class for the VendorResource REST controller.
 *
 * @see VendorResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PrxmenApp.class)
@WebAppConfiguration
@IntegrationTest
public class VendorResourceIntTest {


    private static final Long DEFAULT_VENDOR_ID = 1L;
    private static final Long UPDATED_VENDOR_ID = 2L;
    private static final String DEFAULT_BUSINESS_NAME = "AAAAA";
    private static final String UPDATED_BUSINESS_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_PIVA = "AAAAA";
    private static final String UPDATED_PIVA = "BBBBB";

    @Inject
    private VendorRepository vendorRepository;

    @Inject
    private VendorSearchRepository vendorSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restVendorMockMvc;

    private Vendor vendor;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VendorResource vendorResource = new VendorResource();
        ReflectionTestUtils.setField(vendorResource, "vendorSearchRepository", vendorSearchRepository);
        ReflectionTestUtils.setField(vendorResource, "vendorRepository", vendorRepository);
        this.restVendorMockMvc = MockMvcBuilders.standaloneSetup(vendorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        vendorSearchRepository.deleteAll();
        vendor = new Vendor();
        vendor.setVendorId(DEFAULT_VENDOR_ID);
        vendor.setBusinessName(DEFAULT_BUSINESS_NAME);
        vendor.setDescription(DEFAULT_DESCRIPTION);
        vendor.setPiva(DEFAULT_PIVA);
    }

    @Test
    @Transactional
    public void createVendor() throws Exception {
        int databaseSizeBeforeCreate = vendorRepository.findAll().size();

        // Create the Vendor

        restVendorMockMvc.perform(post("/api/vendors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(vendor)))
                .andExpect(status().isCreated());

        // Validate the Vendor in the database
        List<Vendor> vendors = vendorRepository.findAll();
        assertThat(vendors).hasSize(databaseSizeBeforeCreate + 1);
        Vendor testVendor = vendors.get(vendors.size() - 1);
        assertThat(testVendor.getVendorId()).isEqualTo(DEFAULT_VENDOR_ID);
        assertThat(testVendor.getBusinessName()).isEqualTo(DEFAULT_BUSINESS_NAME);
        assertThat(testVendor.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVendor.getPiva()).isEqualTo(DEFAULT_PIVA);

        // Validate the Vendor in ElasticSearch
        Vendor vendorEs = vendorSearchRepository.findOne(testVendor.getId());
        assertThat(vendorEs).isEqualToComparingFieldByField(testVendor);
    }

    @Test
    @Transactional
    public void getAllVendors() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get all the vendors
        restVendorMockMvc.perform(get("/api/vendors?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(vendor.getId().intValue())))
                .andExpect(jsonPath("$.[*].vendorId").value(hasItem(DEFAULT_VENDOR_ID.intValue())))
                .andExpect(jsonPath("$.[*].businessName").value(hasItem(DEFAULT_BUSINESS_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].piva").value(hasItem(DEFAULT_PIVA.toString())));
    }

    @Test
    @Transactional
    public void getVendor() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);

        // Get the vendor
        restVendorMockMvc.perform(get("/api/vendors/{id}", vendor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(vendor.getId().intValue()))
            .andExpect(jsonPath("$.vendorId").value(DEFAULT_VENDOR_ID.intValue()))
            .andExpect(jsonPath("$.businessName").value(DEFAULT_BUSINESS_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.piva").value(DEFAULT_PIVA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVendor() throws Exception {
        // Get the vendor
        restVendorMockMvc.perform(get("/api/vendors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVendor() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);
        vendorSearchRepository.save(vendor);
        int databaseSizeBeforeUpdate = vendorRepository.findAll().size();

        // Update the vendor
        Vendor updatedVendor = new Vendor();
        updatedVendor.setId(vendor.getId());
        updatedVendor.setVendorId(UPDATED_VENDOR_ID);
        updatedVendor.setBusinessName(UPDATED_BUSINESS_NAME);
        updatedVendor.setDescription(UPDATED_DESCRIPTION);
        updatedVendor.setPiva(UPDATED_PIVA);

        restVendorMockMvc.perform(put("/api/vendors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedVendor)))
                .andExpect(status().isOk());

        // Validate the Vendor in the database
        List<Vendor> vendors = vendorRepository.findAll();
        assertThat(vendors).hasSize(databaseSizeBeforeUpdate);
        Vendor testVendor = vendors.get(vendors.size() - 1);
        assertThat(testVendor.getVendorId()).isEqualTo(UPDATED_VENDOR_ID);
        assertThat(testVendor.getBusinessName()).isEqualTo(UPDATED_BUSINESS_NAME);
        assertThat(testVendor.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVendor.getPiva()).isEqualTo(UPDATED_PIVA);

        // Validate the Vendor in ElasticSearch
        Vendor vendorEs = vendorSearchRepository.findOne(testVendor.getId());
        assertThat(vendorEs).isEqualToComparingFieldByField(testVendor);
    }

    @Test
    @Transactional
    public void deleteVendor() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);
        vendorSearchRepository.save(vendor);
        int databaseSizeBeforeDelete = vendorRepository.findAll().size();

        // Get the vendor
        restVendorMockMvc.perform(delete("/api/vendors/{id}", vendor.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean vendorExistsInEs = vendorSearchRepository.exists(vendor.getId());
        assertThat(vendorExistsInEs).isFalse();

        // Validate the database is empty
        List<Vendor> vendors = vendorRepository.findAll();
        assertThat(vendors).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchVendor() throws Exception {
        // Initialize the database
        vendorRepository.saveAndFlush(vendor);
        vendorSearchRepository.save(vendor);

        // Search the vendor
        restVendorMockMvc.perform(get("/api/_search/vendors?query=id:" + vendor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vendor.getId().intValue())))
            .andExpect(jsonPath("$.[*].vendorId").value(hasItem(DEFAULT_VENDOR_ID.intValue())))
            .andExpect(jsonPath("$.[*].businessName").value(hasItem(DEFAULT_BUSINESS_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].piva").value(hasItem(DEFAULT_PIVA.toString())));
    }
}
