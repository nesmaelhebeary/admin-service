package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.AdminserviceApp;
import com.axa.hypercell.admin.domain.TermsAndConditions;
import com.axa.hypercell.admin.repository.TermsAndConditionsRepository;

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
 * Integration tests for the {@link TermsAndConditionsResource} REST controller.
 */
@SpringBootTest(classes = AdminserviceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TermsAndConditionsResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT_EN = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_EN = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT_AR = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_AR = "BBBBBBBBBB";

    @Autowired
    private TermsAndConditionsRepository termsAndConditionsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTermsAndConditionsMockMvc;

    private TermsAndConditions termsAndConditions;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TermsAndConditions createEntity(EntityManager em) {
        TermsAndConditions termsAndConditions = new TermsAndConditions()
            .description(DEFAULT_DESCRIPTION)
            .textEn(DEFAULT_TEXT_EN)
            .textAr(DEFAULT_TEXT_AR);
        return termsAndConditions;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TermsAndConditions createUpdatedEntity(EntityManager em) {
        TermsAndConditions termsAndConditions = new TermsAndConditions()
            .description(UPDATED_DESCRIPTION)
            .textEn(UPDATED_TEXT_EN)
            .textAr(UPDATED_TEXT_AR);
        return termsAndConditions;
    }

    @BeforeEach
    public void initTest() {
        termsAndConditions = createEntity(em);
    }

    @Test
    @Transactional
    public void createTermsAndConditions() throws Exception {
        int databaseSizeBeforeCreate = termsAndConditionsRepository.findAll().size();
        // Create the TermsAndConditions
        restTermsAndConditionsMockMvc.perform(post("/api/terms-and-conditions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(termsAndConditions)))
            .andExpect(status().isCreated());

        // Validate the TermsAndConditions in the database
        List<TermsAndConditions> termsAndConditionsList = termsAndConditionsRepository.findAll();
        assertThat(termsAndConditionsList).hasSize(databaseSizeBeforeCreate + 1);
        TermsAndConditions testTermsAndConditions = termsAndConditionsList.get(termsAndConditionsList.size() - 1);
        assertThat(testTermsAndConditions.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTermsAndConditions.getTextEn()).isEqualTo(DEFAULT_TEXT_EN);
        assertThat(testTermsAndConditions.getTextAr()).isEqualTo(DEFAULT_TEXT_AR);
    }

    @Test
    @Transactional
    public void createTermsAndConditionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = termsAndConditionsRepository.findAll().size();

        // Create the TermsAndConditions with an existing ID
        termsAndConditions.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTermsAndConditionsMockMvc.perform(post("/api/terms-and-conditions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(termsAndConditions)))
            .andExpect(status().isBadRequest());

        // Validate the TermsAndConditions in the database
        List<TermsAndConditions> termsAndConditionsList = termsAndConditionsRepository.findAll();
        assertThat(termsAndConditionsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTermsAndConditions() throws Exception {
        // Initialize the database
        termsAndConditionsRepository.saveAndFlush(termsAndConditions);

        // Get all the termsAndConditionsList
        restTermsAndConditionsMockMvc.perform(get("/api/terms-and-conditions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(termsAndConditions.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].textEn").value(hasItem(DEFAULT_TEXT_EN)))
            .andExpect(jsonPath("$.[*].textAr").value(hasItem(DEFAULT_TEXT_AR)));
    }
    
    @Test
    @Transactional
    public void getTermsAndConditions() throws Exception {
        // Initialize the database
        termsAndConditionsRepository.saveAndFlush(termsAndConditions);

        // Get the termsAndConditions
        restTermsAndConditionsMockMvc.perform(get("/api/terms-and-conditions/{id}", termsAndConditions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(termsAndConditions.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.textEn").value(DEFAULT_TEXT_EN))
            .andExpect(jsonPath("$.textAr").value(DEFAULT_TEXT_AR));
    }
    @Test
    @Transactional
    public void getNonExistingTermsAndConditions() throws Exception {
        // Get the termsAndConditions
        restTermsAndConditionsMockMvc.perform(get("/api/terms-and-conditions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTermsAndConditions() throws Exception {
        // Initialize the database
        termsAndConditionsRepository.saveAndFlush(termsAndConditions);

        int databaseSizeBeforeUpdate = termsAndConditionsRepository.findAll().size();

        // Update the termsAndConditions
        TermsAndConditions updatedTermsAndConditions = termsAndConditionsRepository.findById(termsAndConditions.getId()).get();
        // Disconnect from session so that the updates on updatedTermsAndConditions are not directly saved in db
        em.detach(updatedTermsAndConditions);
        updatedTermsAndConditions
            .description(UPDATED_DESCRIPTION)
            .textEn(UPDATED_TEXT_EN)
            .textAr(UPDATED_TEXT_AR);

        restTermsAndConditionsMockMvc.perform(put("/api/terms-and-conditions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTermsAndConditions)))
            .andExpect(status().isOk());

        // Validate the TermsAndConditions in the database
        List<TermsAndConditions> termsAndConditionsList = termsAndConditionsRepository.findAll();
        assertThat(termsAndConditionsList).hasSize(databaseSizeBeforeUpdate);
        TermsAndConditions testTermsAndConditions = termsAndConditionsList.get(termsAndConditionsList.size() - 1);
        assertThat(testTermsAndConditions.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTermsAndConditions.getTextEn()).isEqualTo(UPDATED_TEXT_EN);
        assertThat(testTermsAndConditions.getTextAr()).isEqualTo(UPDATED_TEXT_AR);
    }

    @Test
    @Transactional
    public void updateNonExistingTermsAndConditions() throws Exception {
        int databaseSizeBeforeUpdate = termsAndConditionsRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTermsAndConditionsMockMvc.perform(put("/api/terms-and-conditions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(termsAndConditions)))
            .andExpect(status().isBadRequest());

        // Validate the TermsAndConditions in the database
        List<TermsAndConditions> termsAndConditionsList = termsAndConditionsRepository.findAll();
        assertThat(termsAndConditionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTermsAndConditions() throws Exception {
        // Initialize the database
        termsAndConditionsRepository.saveAndFlush(termsAndConditions);

        int databaseSizeBeforeDelete = termsAndConditionsRepository.findAll().size();

        // Delete the termsAndConditions
        restTermsAndConditionsMockMvc.perform(delete("/api/terms-and-conditions/{id}", termsAndConditions.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TermsAndConditions> termsAndConditionsList = termsAndConditionsRepository.findAll();
        assertThat(termsAndConditionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
