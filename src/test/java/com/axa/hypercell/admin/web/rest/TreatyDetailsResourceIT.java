package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.AdminserviceApp;
import com.axa.hypercell.admin.domain.TreatyDetails;
import com.axa.hypercell.admin.repository.TreatyDetailsRepository;

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

/**
 * Integration tests for the {@link TreatyDetailsResource} REST controller.
 */
@SpringBootTest(classes = AdminserviceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TreatyDetailsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_TREATY_ID = 1L;
    private static final Long UPDATED_TREATY_ID = 2L;

    private static final Double DEFAULT_MAXIMUM_LIMIT = 1D;
    private static final Double UPDATED_MAXIMUM_LIMIT = 2D;

    private static final Double DEFAULT_MIN_LIMIT = 1D;
    private static final Double UPDATED_MIN_LIMIT = 2D;

    private static final Double DEFAULT_RETAINED = 1D;
    private static final Double UPDATED_RETAINED = 2D;

    private static final Double DEFAULT_QUOTA_SHARED = 1D;
    private static final Double UPDATED_QUOTA_SHARED = 2D;

    private static final Double DEFAULT_SURPLUS = 1D;
    private static final Double UPDATED_SURPLUS = 2D;

    private static final Double DEFAULT_AUTO_FAC = 1D;
    private static final Double UPDATED_AUTO_FAC = 2D;

    @Autowired
    private TreatyDetailsRepository treatyDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTreatyDetailsMockMvc;

    private TreatyDetails treatyDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TreatyDetails createEntity(EntityManager em) {
        TreatyDetails treatyDetails = new TreatyDetails()
            .name(DEFAULT_NAME)
            .treatyId(DEFAULT_TREATY_ID)
            .maximumLimit(DEFAULT_MAXIMUM_LIMIT)
            .minLimit(DEFAULT_MIN_LIMIT)
            .retained(DEFAULT_RETAINED)
            .quotaShared(DEFAULT_QUOTA_SHARED)
            .surplus(DEFAULT_SURPLUS)
            .autoFac(DEFAULT_AUTO_FAC);
        return treatyDetails;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TreatyDetails createUpdatedEntity(EntityManager em) {
        TreatyDetails treatyDetails = new TreatyDetails()
            .name(UPDATED_NAME)
            .treatyId(UPDATED_TREATY_ID)
            .maximumLimit(UPDATED_MAXIMUM_LIMIT)
            .minLimit(UPDATED_MIN_LIMIT)
            .retained(UPDATED_RETAINED)
            .quotaShared(UPDATED_QUOTA_SHARED)
            .surplus(UPDATED_SURPLUS)
            .autoFac(UPDATED_AUTO_FAC);
        return treatyDetails;
    }

    @BeforeEach
    public void initTest() {
        treatyDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createTreatyDetails() throws Exception {
        int databaseSizeBeforeCreate = treatyDetailsRepository.findAll().size();
        // Create the TreatyDetails
        restTreatyDetailsMockMvc.perform(post("/api/treaty-details")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(treatyDetails)))
            .andExpect(status().isCreated());

        // Validate the TreatyDetails in the database
        List<TreatyDetails> treatyDetailsList = treatyDetailsRepository.findAll();
        assertThat(treatyDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        TreatyDetails testTreatyDetails = treatyDetailsList.get(treatyDetailsList.size() - 1);
        assertThat(testTreatyDetails.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTreatyDetails.getTreatyId()).isEqualTo(DEFAULT_TREATY_ID);
        assertThat(testTreatyDetails.getMaximumLimit()).isEqualTo(DEFAULT_MAXIMUM_LIMIT);
        assertThat(testTreatyDetails.getMinLimit()).isEqualTo(DEFAULT_MIN_LIMIT);
        assertThat(testTreatyDetails.getRetained()).isEqualTo(DEFAULT_RETAINED);
        assertThat(testTreatyDetails.getQuotaShared()).isEqualTo(DEFAULT_QUOTA_SHARED);
        assertThat(testTreatyDetails.getSurplus()).isEqualTo(DEFAULT_SURPLUS);
        assertThat(testTreatyDetails.getAutoFac()).isEqualTo(DEFAULT_AUTO_FAC);
    }

    @Test
    @Transactional
    public void createTreatyDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = treatyDetailsRepository.findAll().size();

        // Create the TreatyDetails with an existing ID
        treatyDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTreatyDetailsMockMvc.perform(post("/api/treaty-details")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(treatyDetails)))
            .andExpect(status().isBadRequest());

        // Validate the TreatyDetails in the database
        List<TreatyDetails> treatyDetailsList = treatyDetailsRepository.findAll();
        assertThat(treatyDetailsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTreatyDetails() throws Exception {
        // Initialize the database
        treatyDetailsRepository.saveAndFlush(treatyDetails);

        // Get all the treatyDetailsList
        restTreatyDetailsMockMvc.perform(get("/api/treaty-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(treatyDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].treatyId").value(hasItem(DEFAULT_TREATY_ID.intValue())))
            .andExpect(jsonPath("$.[*].maximumLimit").value(hasItem(DEFAULT_MAXIMUM_LIMIT.doubleValue())))
            .andExpect(jsonPath("$.[*].minLimit").value(hasItem(DEFAULT_MIN_LIMIT.doubleValue())))
            .andExpect(jsonPath("$.[*].retained").value(hasItem(DEFAULT_RETAINED.doubleValue())))
            .andExpect(jsonPath("$.[*].quotaShared").value(hasItem(DEFAULT_QUOTA_SHARED.doubleValue())))
            .andExpect(jsonPath("$.[*].surplus").value(hasItem(DEFAULT_SURPLUS.doubleValue())))
            .andExpect(jsonPath("$.[*].autoFac").value(hasItem(DEFAULT_AUTO_FAC.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getTreatyDetails() throws Exception {
        // Initialize the database
        treatyDetailsRepository.saveAndFlush(treatyDetails);

        // Get the treatyDetails
        restTreatyDetailsMockMvc.perform(get("/api/treaty-details/{id}", treatyDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(treatyDetails.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.treatyId").value(DEFAULT_TREATY_ID.intValue()))
            .andExpect(jsonPath("$.maximumLimit").value(DEFAULT_MAXIMUM_LIMIT.doubleValue()))
            .andExpect(jsonPath("$.minLimit").value(DEFAULT_MIN_LIMIT.doubleValue()))
            .andExpect(jsonPath("$.retained").value(DEFAULT_RETAINED.doubleValue()))
            .andExpect(jsonPath("$.quotaShared").value(DEFAULT_QUOTA_SHARED.doubleValue()))
            .andExpect(jsonPath("$.surplus").value(DEFAULT_SURPLUS.doubleValue()))
            .andExpect(jsonPath("$.autoFac").value(DEFAULT_AUTO_FAC.doubleValue()));
    }
    @Test
    @Transactional
    public void getNonExistingTreatyDetails() throws Exception {
        // Get the treatyDetails
        restTreatyDetailsMockMvc.perform(get("/api/treaty-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTreatyDetails() throws Exception {
        // Initialize the database
        treatyDetailsRepository.saveAndFlush(treatyDetails);

        int databaseSizeBeforeUpdate = treatyDetailsRepository.findAll().size();

        // Update the treatyDetails
        TreatyDetails updatedTreatyDetails = treatyDetailsRepository.findById(treatyDetails.getId()).get();
        // Disconnect from session so that the updates on updatedTreatyDetails are not directly saved in db
        em.detach(updatedTreatyDetails);
        updatedTreatyDetails
            .name(UPDATED_NAME)
            .treatyId(UPDATED_TREATY_ID)
            .maximumLimit(UPDATED_MAXIMUM_LIMIT)
            .minLimit(UPDATED_MIN_LIMIT)
            .retained(UPDATED_RETAINED)
            .quotaShared(UPDATED_QUOTA_SHARED)
            .surplus(UPDATED_SURPLUS)
            .autoFac(UPDATED_AUTO_FAC);

        restTreatyDetailsMockMvc.perform(put("/api/treaty-details")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTreatyDetails)))
            .andExpect(status().isOk());

        // Validate the TreatyDetails in the database
        List<TreatyDetails> treatyDetailsList = treatyDetailsRepository.findAll();
        assertThat(treatyDetailsList).hasSize(databaseSizeBeforeUpdate);
        TreatyDetails testTreatyDetails = treatyDetailsList.get(treatyDetailsList.size() - 1);
        assertThat(testTreatyDetails.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTreatyDetails.getTreatyId()).isEqualTo(UPDATED_TREATY_ID);
        assertThat(testTreatyDetails.getMaximumLimit()).isEqualTo(UPDATED_MAXIMUM_LIMIT);
        assertThat(testTreatyDetails.getMinLimit()).isEqualTo(UPDATED_MIN_LIMIT);
        assertThat(testTreatyDetails.getRetained()).isEqualTo(UPDATED_RETAINED);
        assertThat(testTreatyDetails.getQuotaShared()).isEqualTo(UPDATED_QUOTA_SHARED);
        assertThat(testTreatyDetails.getSurplus()).isEqualTo(UPDATED_SURPLUS);
        assertThat(testTreatyDetails.getAutoFac()).isEqualTo(UPDATED_AUTO_FAC);
    }

    @Test
    @Transactional
    public void updateNonExistingTreatyDetails() throws Exception {
        int databaseSizeBeforeUpdate = treatyDetailsRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTreatyDetailsMockMvc.perform(put("/api/treaty-details")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(treatyDetails)))
            .andExpect(status().isBadRequest());

        // Validate the TreatyDetails in the database
        List<TreatyDetails> treatyDetailsList = treatyDetailsRepository.findAll();
        assertThat(treatyDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTreatyDetails() throws Exception {
        // Initialize the database
        treatyDetailsRepository.saveAndFlush(treatyDetails);

        int databaseSizeBeforeDelete = treatyDetailsRepository.findAll().size();

        // Delete the treatyDetails
        restTreatyDetailsMockMvc.perform(delete("/api/treaty-details/{id}", treatyDetails.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TreatyDetails> treatyDetailsList = treatyDetailsRepository.findAll();
        assertThat(treatyDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
