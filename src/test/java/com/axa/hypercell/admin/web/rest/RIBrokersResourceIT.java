package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.AdminserviceApp;
import com.axa.hypercell.admin.domain.RIBrokers;
import com.axa.hypercell.admin.repository.RIBrokersRepository;

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
 * Integration tests for the {@link RIBrokersResource} REST controller.
 */
@SpringBootTest(classes = AdminserviceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RIBrokersResourceIT {

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
    private RIBrokersRepository rIBrokersRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRIBrokersMockMvc;

    private RIBrokers rIBrokers;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RIBrokers createEntity(EntityManager em) {
        RIBrokers rIBrokers = new RIBrokers()
            .name(DEFAULT_NAME)
            .registerationNumber(DEFAULT_REGISTERATION_NUMBER)
            .commissionPercentage(DEFAULT_COMMISSION_PERCENTAGE)
            .status(DEFAULT_STATUS)
            .contactDial(DEFAULT_CONTACT_DIAL)
            .contactEmail(DEFAULT_CONTACT_EMAIL);
        return rIBrokers;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RIBrokers createUpdatedEntity(EntityManager em) {
        RIBrokers rIBrokers = new RIBrokers()
            .name(UPDATED_NAME)
            .registerationNumber(UPDATED_REGISTERATION_NUMBER)
            .commissionPercentage(UPDATED_COMMISSION_PERCENTAGE)
            .status(UPDATED_STATUS)
            .contactDial(UPDATED_CONTACT_DIAL)
            .contactEmail(UPDATED_CONTACT_EMAIL);
        return rIBrokers;
    }

    @BeforeEach
    public void initTest() {
        rIBrokers = createEntity(em);
    }

    @Test
    @Transactional
    public void createRIBrokers() throws Exception {
        int databaseSizeBeforeCreate = rIBrokersRepository.findAll().size();
        // Create the RIBrokers
        restRIBrokersMockMvc.perform(post("/api/ri-brokers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rIBrokers)))
            .andExpect(status().isCreated());

        // Validate the RIBrokers in the database
        List<RIBrokers> rIBrokersList = rIBrokersRepository.findAll();
        assertThat(rIBrokersList).hasSize(databaseSizeBeforeCreate + 1);
        RIBrokers testRIBrokers = rIBrokersList.get(rIBrokersList.size() - 1);
        assertThat(testRIBrokers.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRIBrokers.getRegisterationNumber()).isEqualTo(DEFAULT_REGISTERATION_NUMBER);
        assertThat(testRIBrokers.getCommissionPercentage()).isEqualTo(DEFAULT_COMMISSION_PERCENTAGE);
        assertThat(testRIBrokers.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRIBrokers.getContactDial()).isEqualTo(DEFAULT_CONTACT_DIAL);
        assertThat(testRIBrokers.getContactEmail()).isEqualTo(DEFAULT_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    public void createRIBrokersWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rIBrokersRepository.findAll().size();

        // Create the RIBrokers with an existing ID
        rIBrokers.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRIBrokersMockMvc.perform(post("/api/ri-brokers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rIBrokers)))
            .andExpect(status().isBadRequest());

        // Validate the RIBrokers in the database
        List<RIBrokers> rIBrokersList = rIBrokersRepository.findAll();
        assertThat(rIBrokersList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRIBrokers() throws Exception {
        // Initialize the database
        rIBrokersRepository.saveAndFlush(rIBrokers);

        // Get all the rIBrokersList
        restRIBrokersMockMvc.perform(get("/api/ri-brokers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rIBrokers.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].registerationNumber").value(hasItem(DEFAULT_REGISTERATION_NUMBER)))
            .andExpect(jsonPath("$.[*].commissionPercentage").value(hasItem(DEFAULT_COMMISSION_PERCENTAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].contactDial").value(hasItem(DEFAULT_CONTACT_DIAL)))
            .andExpect(jsonPath("$.[*].contactEmail").value(hasItem(DEFAULT_CONTACT_EMAIL)));
    }
    
    @Test
    @Transactional
    public void getRIBrokers() throws Exception {
        // Initialize the database
        rIBrokersRepository.saveAndFlush(rIBrokers);

        // Get the rIBrokers
        restRIBrokersMockMvc.perform(get("/api/ri-brokers/{id}", rIBrokers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rIBrokers.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.registerationNumber").value(DEFAULT_REGISTERATION_NUMBER))
            .andExpect(jsonPath("$.commissionPercentage").value(DEFAULT_COMMISSION_PERCENTAGE.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.contactDial").value(DEFAULT_CONTACT_DIAL))
            .andExpect(jsonPath("$.contactEmail").value(DEFAULT_CONTACT_EMAIL));
    }
    @Test
    @Transactional
    public void getNonExistingRIBrokers() throws Exception {
        // Get the rIBrokers
        restRIBrokersMockMvc.perform(get("/api/ri-brokers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRIBrokers() throws Exception {
        // Initialize the database
        rIBrokersRepository.saveAndFlush(rIBrokers);

        int databaseSizeBeforeUpdate = rIBrokersRepository.findAll().size();

        // Update the rIBrokers
        RIBrokers updatedRIBrokers = rIBrokersRepository.findById(rIBrokers.getId()).get();
        // Disconnect from session so that the updates on updatedRIBrokers are not directly saved in db
        em.detach(updatedRIBrokers);
        updatedRIBrokers
            .name(UPDATED_NAME)
            .registerationNumber(UPDATED_REGISTERATION_NUMBER)
            .commissionPercentage(UPDATED_COMMISSION_PERCENTAGE)
            .status(UPDATED_STATUS)
            .contactDial(UPDATED_CONTACT_DIAL)
            .contactEmail(UPDATED_CONTACT_EMAIL);

        restRIBrokersMockMvc.perform(put("/api/ri-brokers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRIBrokers)))
            .andExpect(status().isOk());

        // Validate the RIBrokers in the database
        List<RIBrokers> rIBrokersList = rIBrokersRepository.findAll();
        assertThat(rIBrokersList).hasSize(databaseSizeBeforeUpdate);
        RIBrokers testRIBrokers = rIBrokersList.get(rIBrokersList.size() - 1);
        assertThat(testRIBrokers.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRIBrokers.getRegisterationNumber()).isEqualTo(UPDATED_REGISTERATION_NUMBER);
        assertThat(testRIBrokers.getCommissionPercentage()).isEqualTo(UPDATED_COMMISSION_PERCENTAGE);
        assertThat(testRIBrokers.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRIBrokers.getContactDial()).isEqualTo(UPDATED_CONTACT_DIAL);
        assertThat(testRIBrokers.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
    }

    @Test
    @Transactional
    public void updateNonExistingRIBrokers() throws Exception {
        int databaseSizeBeforeUpdate = rIBrokersRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRIBrokersMockMvc.perform(put("/api/ri-brokers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rIBrokers)))
            .andExpect(status().isBadRequest());

        // Validate the RIBrokers in the database
        List<RIBrokers> rIBrokersList = rIBrokersRepository.findAll();
        assertThat(rIBrokersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRIBrokers() throws Exception {
        // Initialize the database
        rIBrokersRepository.saveAndFlush(rIBrokers);

        int databaseSizeBeforeDelete = rIBrokersRepository.findAll().size();

        // Delete the rIBrokers
        restRIBrokersMockMvc.perform(delete("/api/ri-brokers/{id}", rIBrokers.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RIBrokers> rIBrokersList = rIBrokersRepository.findAll();
        assertThat(rIBrokersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
