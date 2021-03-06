package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.AdminserviceApp;
import com.axa.hypercell.admin.domain.SalesBrokers;
import com.axa.hypercell.admin.repository.SalesBrokersRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.domain.enumeration.Status;
/**
 * Integration tests for the {@link SalesBrokersResource} REST controller.
 */
@SpringBootTest(classes = AdminserviceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class SalesBrokersResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REGISTERATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REGISTERATION_NUMBER = "BBBBBBBBBB";

    private static final Double DEFAULT_COMMISSION_PERCENTAGE = 1D;
    private static final Double UPDATED_COMMISSION_PERCENTAGE = 2D;

    private static final Status DEFAULT_STATUS = Status.Active;
    private static final Status UPDATED_STATUS = Status.InActive;

    private static final String DEFAULT_CONTACT_DIAL = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_DIAL = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_EMAIL = "BBBBBBBBBB";

    @Autowired
    private SalesBrokersRepository salesBrokersRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSalesBrokersMockMvc;

    private SalesBrokers salesBrokers;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalesBrokers createEntity(EntityManager em) {
        SalesBrokers salesBrokers = new SalesBrokers()
            .name(DEFAULT_NAME)
            .registerationNumber(DEFAULT_REGISTERATION_NUMBER)
            .commissionPercentage(DEFAULT_COMMISSION_PERCENTAGE)
            .status(DEFAULT_STATUS)
            .contactDial(DEFAULT_CONTACT_DIAL)
            .contactEmail(DEFAULT_CONTACT_EMAIL);
        return salesBrokers;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SalesBrokers createUpdatedEntity(EntityManager em) {
        SalesBrokers salesBrokers = new SalesBrokers()
            .name(UPDATED_NAME)
            .registerationNumber(UPDATED_REGISTERATION_NUMBER)
            .commissionPercentage(UPDATED_COMMISSION_PERCENTAGE)
            .status(UPDATED_STATUS)
            .contactDial(UPDATED_CONTACT_DIAL)
            .contactEmail(UPDATED_CONTACT_EMAIL);
        return salesBrokers;
    }

    @BeforeEach
    public void initTest() {
        salesBrokers = createEntity(em);
    }

    @Test
    @Transactional
    public void createSalesBrokers() throws Exception {
        int databaseSizeBeforeCreate = salesBrokersRepository.findAll().size();
        // Create the SalesBrokers
        restSalesBrokersMockMvc.perform(post("/api/sales-brokers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(salesBrokers)))
            .andExpect(status().isCreated());

        // Validate the SalesBrokers in the database
        List<SalesBrokers> salesBrokersList = salesBrokersRepository.findAll();
        assertThat(salesBrokersList).hasSize(databaseSizeBeforeCreate + 1);
        SalesBrokers testSalesBrokers = salesBrokersList.get(salesBrokersList.size() - 1);
        assertThat(testSalesBrokers.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSalesBrokers.getRegisterationNumber()).isEqualTo(DEFAULT_REGISTERATION_NUMBER);
        assertThat(testSalesBrokers.getCommissionPercentage()).isEqualTo(DEFAULT_COMMISSION_PERCENTAGE);
        assertThat(testSalesBrokers.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSalesBrokers.getContactDial()).isEqualTo(DEFAULT_CONTACT_DIAL);
        assertThat(testSalesBrokers.getContactEmail()).isEqualTo(DEFAULT_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    public void createSalesBrokersWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = salesBrokersRepository.findAll().size();

        // Create the SalesBrokers with an existing ID
        salesBrokers.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalesBrokersMockMvc.perform(post("/api/sales-brokers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(salesBrokers)))
            .andExpect(status().isBadRequest());

        // Validate the SalesBrokers in the database
        List<SalesBrokers> salesBrokersList = salesBrokersRepository.findAll();
        assertThat(salesBrokersList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSalesBrokers() throws Exception {
        // Initialize the database
        salesBrokersRepository.saveAndFlush(salesBrokers);

        // Get all the salesBrokersList
        restSalesBrokersMockMvc.perform(get("/api/sales-brokers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salesBrokers.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].registerationNumber").value(hasItem(DEFAULT_REGISTERATION_NUMBER)))
            .andExpect(jsonPath("$.[*].commissionPercentage").value(hasItem(DEFAULT_COMMISSION_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].contactDial").value(hasItem(DEFAULT_CONTACT_DIAL)))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL)));
    }
    
    @Test
    @Transactional
    public void getSalesBrokers() throws Exception {
        // Initialize the database
        salesBrokersRepository.saveAndFlush(salesBrokers);

        // Get the salesBrokers
        restSalesBrokersMockMvc.perform(get("/api/sales-brokers/{id}", salesBrokers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(salesBrokers.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.registerationNumber").value(DEFAULT_REGISTERATION_NUMBER))
            .andExpect(jsonPath("$.commissionPercentage").value(DEFAULT_COMMISSION_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.contactDial").value(DEFAULT_CONTACT_DIAL))
            .andExpect(jsonPath("$.contactEmail").value(DEFAULT_CONTACT_EMAIL));
    }
    @Test
    @Transactional
    public void getNonExistingSalesBrokers() throws Exception {
        // Get the salesBrokers
        restSalesBrokersMockMvc.perform(get("/api/sales-brokers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSalesBrokers() throws Exception {
        // Initialize the database
        salesBrokersRepository.saveAndFlush(salesBrokers);

        int databaseSizeBeforeUpdate = salesBrokersRepository.findAll().size();

        // Update the salesBrokers
        SalesBrokers updatedSalesBrokers = salesBrokersRepository.findById(salesBrokers.getId()).get();
        // Disconnect from session so that the updates on updatedSalesBrokers are not directly saved in db
        em.detach(updatedSalesBrokers);
        updatedSalesBrokers
            .name(UPDATED_NAME)
            .registerationNumber(UPDATED_REGISTERATION_NUMBER)
            .commissionPercentage(UPDATED_COMMISSION_PERCENTAGE)
            .status(UPDATED_STATUS)
            .contactDial(UPDATED_CONTACT_DIAL)
            .contactEmail(UPDATED_CONTACT_EMAIL);

        restSalesBrokersMockMvc.perform(put("/api/sales-brokers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSalesBrokers)))
            .andExpect(status().isOk());

        // Validate the SalesBrokers in the database
        List<SalesBrokers> salesBrokersList = salesBrokersRepository.findAll();
        assertThat(salesBrokersList).hasSize(databaseSizeBeforeUpdate);
        SalesBrokers testSalesBrokers = salesBrokersList.get(salesBrokersList.size() - 1);
        assertThat(testSalesBrokers.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSalesBrokers.getRegisterationNumber()).isEqualTo(UPDATED_REGISTERATION_NUMBER);
        assertThat(testSalesBrokers.getCommissionPercentage()).isEqualTo(UPDATED_COMMISSION_PERCENTAGE);
        assertThat(testSalesBrokers.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSalesBrokers.getContactDial()).isEqualTo(UPDATED_CONTACT_DIAL);
        assertThat(testSalesBrokers.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    public void updateNonExistingSalesBrokers() throws Exception {
        int databaseSizeBeforeUpdate = salesBrokersRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalesBrokersMockMvc.perform(put("/api/sales-brokers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(salesBrokers)))
            .andExpect(status().isBadRequest());

        // Validate the SalesBrokers in the database
        List<SalesBrokers> salesBrokersList = salesBrokersRepository.findAll();
        assertThat(salesBrokersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSalesBrokers() throws Exception {
        // Initialize the database
        salesBrokersRepository.saveAndFlush(salesBrokers);

        int databaseSizeBeforeDelete = salesBrokersRepository.findAll().size();

        // Delete the salesBrokers
        restSalesBrokersMockMvc.perform(delete("/api/sales-brokers/{id}", salesBrokers.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SalesBrokers> salesBrokersList = salesBrokersRepository.findAll();
        assertThat(salesBrokersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
