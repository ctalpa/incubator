package com.ctalpa.prxmen.web.rest;

import com.ctalpa.prxmen.PrxmenApp;
import com.ctalpa.prxmen.domain.Customer;
import com.ctalpa.prxmen.repository.CustomerRepository;
import com.ctalpa.prxmen.service.CustomerService;
import com.ctalpa.prxmen.repository.search.CustomerSearchRepository;
import com.ctalpa.prxmen.web.rest.dto.CustomerDTO;
import com.ctalpa.prxmen.web.rest.mapper.CustomerMapper;

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

import com.ctalpa.prxmen.domain.enumeration.CustomerType;

/**
 * Test class for the CustomerResource REST controller.
 *
 * @see CustomerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PrxmenApp.class)
@WebAppConfiguration
@IntegrationTest
public class CustomerResourceIntTest {


    private static final Long DEFAULT_CUSTUMER_ID = 1L;
    private static final Long UPDATED_CUSTUMER_ID = 2L;
    private static final String DEFAULT_BUSINESS_NAME = "AAAAA";
    private static final String UPDATED_BUSINESS_NAME = "BBBBB";
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_SURNAME = "AAAAA";
    private static final String UPDATED_SURNAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final CustomerType DEFAULT_CUSTOMER_TYPE = CustomerType.PERSON;
    private static final CustomerType UPDATED_CUSTOMER_TYPE = CustomerType.COMPANY;
    private static final String DEFAULT_PIVA = "AAAAA";
    private static final String UPDATED_PIVA = "BBBBB";
    private static final String DEFAULT_FISCAL_CODE = "AAAAA";
    private static final String UPDATED_FISCAL_CODE = "BBBBB";

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private CustomerMapper customerMapper;

    @Inject
    private CustomerService customerService;

    @Inject
    private CustomerSearchRepository customerSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCustomerMockMvc;

    private Customer customer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CustomerResource customerResource = new CustomerResource();
        ReflectionTestUtils.setField(customerResource, "customerService", customerService);
        ReflectionTestUtils.setField(customerResource, "customerMapper", customerMapper);
        this.restCustomerMockMvc = MockMvcBuilders.standaloneSetup(customerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        customerSearchRepository.deleteAll();
        customer = new Customer();
        customer.setCustumerId(DEFAULT_CUSTUMER_ID);
        customer.setBusinessName(DEFAULT_BUSINESS_NAME);
        customer.setName(DEFAULT_NAME);
        customer.setSurname(DEFAULT_SURNAME);
        customer.setDescription(DEFAULT_DESCRIPTION);
        customer.setCustomerType(DEFAULT_CUSTOMER_TYPE);
        customer.setPiva(DEFAULT_PIVA);
        customer.setFiscalCode(DEFAULT_FISCAL_CODE);
    }

    @Test
    @Transactional
    public void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Customer
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);

        restCustomerMockMvc.perform(post("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
                .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customers.get(customers.size() - 1);
        assertThat(testCustomer.getCustumerId()).isEqualTo(DEFAULT_CUSTUMER_ID);
        assertThat(testCustomer.getBusinessName()).isEqualTo(DEFAULT_BUSINESS_NAME);
        assertThat(testCustomer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCustomer.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testCustomer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCustomer.getCustomerType()).isEqualTo(DEFAULT_CUSTOMER_TYPE);
        assertThat(testCustomer.getPiva()).isEqualTo(DEFAULT_PIVA);
        assertThat(testCustomer.getFiscalCode()).isEqualTo(DEFAULT_FISCAL_CODE);

        // Validate the Customer in ElasticSearch
        Customer customerEs = customerSearchRepository.findOne(testCustomer.getId());
        assertThat(customerEs).isEqualToComparingFieldByField(testCustomer);
    }

    @Test
    @Transactional
    public void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customers
        restCustomerMockMvc.perform(get("/api/customers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
                .andExpect(jsonPath("$.[*].custumerId").value(hasItem(DEFAULT_CUSTUMER_ID.intValue())))
                .andExpect(jsonPath("$.[*].businessName").value(hasItem(DEFAULT_BUSINESS_NAME.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].customerType").value(hasItem(DEFAULT_CUSTOMER_TYPE.toString())))
                .andExpect(jsonPath("$.[*].piva").value(hasItem(DEFAULT_PIVA.toString())))
                .andExpect(jsonPath("$.[*].fiscalCode").value(hasItem(DEFAULT_FISCAL_CODE.toString())));
    }

    @Test
    @Transactional
    public void getCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(customer.getId().intValue()))
            .andExpect(jsonPath("$.custumerId").value(DEFAULT_CUSTUMER_ID.intValue()))
            .andExpect(jsonPath("$.businessName").value(DEFAULT_BUSINESS_NAME.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.customerType").value(DEFAULT_CUSTOMER_TYPE.toString()))
            .andExpect(jsonPath("$.piva").value(DEFAULT_PIVA.toString()))
            .andExpect(jsonPath("$.fiscalCode").value(DEFAULT_FISCAL_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get("/api/customers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        customerSearchRepository.save(customer);
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(customer.getId());
        updatedCustomer.setCustumerId(UPDATED_CUSTUMER_ID);
        updatedCustomer.setBusinessName(UPDATED_BUSINESS_NAME);
        updatedCustomer.setName(UPDATED_NAME);
        updatedCustomer.setSurname(UPDATED_SURNAME);
        updatedCustomer.setDescription(UPDATED_DESCRIPTION);
        updatedCustomer.setCustomerType(UPDATED_CUSTOMER_TYPE);
        updatedCustomer.setPiva(UPDATED_PIVA);
        updatedCustomer.setFiscalCode(UPDATED_FISCAL_CODE);
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(updatedCustomer);

        restCustomerMockMvc.perform(put("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerDTO)))
                .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customers.get(customers.size() - 1);
        assertThat(testCustomer.getCustumerId()).isEqualTo(UPDATED_CUSTUMER_ID);
        assertThat(testCustomer.getBusinessName()).isEqualTo(UPDATED_BUSINESS_NAME);
        assertThat(testCustomer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomer.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testCustomer.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCustomer.getCustomerType()).isEqualTo(UPDATED_CUSTOMER_TYPE);
        assertThat(testCustomer.getPiva()).isEqualTo(UPDATED_PIVA);
        assertThat(testCustomer.getFiscalCode()).isEqualTo(UPDATED_FISCAL_CODE);

        // Validate the Customer in ElasticSearch
        Customer customerEs = customerSearchRepository.findOne(testCustomer.getId());
        assertThat(customerEs).isEqualToComparingFieldByField(testCustomer);
    }

    @Test
    @Transactional
    public void deleteCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        customerSearchRepository.save(customer);
        int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Get the customer
        restCustomerMockMvc.perform(delete("/api/customers/{id}", customer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean customerExistsInEs = customerSearchRepository.exists(customer.getId());
        assertThat(customerExistsInEs).isFalse();

        // Validate the database is empty
        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        customerSearchRepository.save(customer);

        // Search the customer
        restCustomerMockMvc.perform(get("/api/_search/customers?query=id:" + customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].custumerId").value(hasItem(DEFAULT_CUSTUMER_ID.intValue())))
            .andExpect(jsonPath("$.[*].businessName").value(hasItem(DEFAULT_BUSINESS_NAME.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].customerType").value(hasItem(DEFAULT_CUSTOMER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].piva").value(hasItem(DEFAULT_PIVA.toString())))
            .andExpect(jsonPath("$.[*].fiscalCode").value(hasItem(DEFAULT_FISCAL_CODE.toString())));
    }
}
