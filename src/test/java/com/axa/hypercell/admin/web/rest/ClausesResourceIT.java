package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.AdminserviceApp;
import com.axa.hypercell.admin.domain.Clauses;
import com.axa.hypercell.admin.repository.ClausesRepository;

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
 * Integration tests for the {@link ClausesResource} REST controller.
 */
@SpringBootTest(classes = AdminserviceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ClausesResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT_EN = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_EN = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT_AR = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_AR = "BBBBBBBBBB";

    @Autowired
    private ClausesRepository clausesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClausesMockMvc;

    private Clauses clauses;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Clauses createEntity(EntityManager em) {
        Clauses clauses = new Clauses()
            .description(DEFAULT_DESCRIPTION)
            .textEn(DEFAULT_TEXT_EN)
            .textAr(DEFAULT_TEXT_AR);
        return clauses;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Clauses createUpdatedEntity(EntityManager em) {
        Clauses clauses = new Clauses()
            .description(UPDATED_DESCRIPTION)
            .textEn(UPDATED_TEXT_EN)
            .textAr(UPDATED_TEXT_AR);
        return clauses;
    }

    @BeforeEach
    public void initTest() {
        clauses = createEntity(em);
    }

    @Test
    @Transactional
    public void createClauses() throws Exception {
        int databaseSizeBeforeCreate = clausesRepository.findAll().size();
        // Create the Clauses
        restClausesMockMvc.perform(post("/api/clauses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(clauses)))
            .andExpect(status().isCreated());

        // Validate the Clauses in the database
        List<Clauses> clausesList = clausesRepository.findAll();
        assertThat(clausesList).hasSize(databaseSizeBeforeCreate + 1);
        Clauses testClauses = clausesList.get(clausesList.size() - 1);
        assertThat(testClauses.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testClauses.getTextEn()).isEqualTo(DEFAULT_TEXT_EN);
        assertThat(testClauses.getTextAr()).isEqualTo(DEFAULT_TEXT_AR);
    }

    @Test
    @Transactional
    public void createClausesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clausesRepository.findAll().size();

        // Create the Clauses with an existing ID
        clauses.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClausesMockMvc.perform(post("/api/clauses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(clauses)))
            .andExpect(status().isBadRequest());

        // Validate the Clauses in the database
        List<Clauses> clausesList = clausesRepository.findAll();
        assertThat(clausesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllClauses() throws Exception {
        // Initialize the database
        clausesRepository.saveAndFlush(clauses);

        // Get all the clausesList
        restClausesMockMvc.perform(get("/api/clauses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clauses.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].textEn").value(hasItem(DEFAULT_TEXT_EN)))
            .andExpect(jsonPath("$.[*].textAr").value(hasItem(DEFAULT_TEXT_AR)));
    }
    
    @Test
    @Transactional
    public void getClauses() throws Exception {
        // Initialize the database
        clausesRepository.saveAndFlush(clauses);

        // Get the clauses
        restClausesMockMvc.perform(get("/api/clauses/{id}", clauses.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clauses.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.textEn").value(DEFAULT_TEXT_EN))
            .andExpect(jsonPath("$.textAr").value(DEFAULT_TEXT_AR));
    }
    @Test
    @Transactional
    public void getNonExistingClauses() throws Exception {
        // Get the clauses
        restClausesMockMvc.perform(get("/api/clauses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClauses() throws Exception {
        // Initialize the database
        clausesRepository.saveAndFlush(clauses);

        int databaseSizeBeforeUpdate = clausesRepository.findAll().size();

        // Update the clauses
        Clauses updatedClauses = clausesRepository.findById(clauses.getId()).get();
        // Disconnect from session so that the updates on updatedClauses are not directly saved in db
        em.detach(updatedClauses);
        updatedClauses
            .description(UPDATED_DESCRIPTION)
            .textEn(UPDATED_TEXT_EN)
            .textAr(UPDATED_TEXT_AR);

        restClausesMockMvc.perform(put("/api/clauses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedClauses)))
            .andExpect(status().isOk());

        // Validate the Clauses in the database
        List<Clauses> clausesList = clausesRepository.findAll();
        assertThat(clausesList).hasSize(databaseSizeBeforeUpdate);
        Clauses testClauses = clausesList.get(clausesList.size() - 1);
        assertThat(testClauses.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testClauses.getTextEn()).isEqualTo(UPDATED_TEXT_EN);
        assertThat(testClauses.getTextAr()).isEqualTo(UPDATED_TEXT_AR);
    }

    @Test
    @Transactional
    public void updateNonExistingClauses() throws Exception {
        int databaseSizeBeforeUpdate = clausesRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClausesMockMvc.perform(put("/api/clauses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(clauses)))
            .andExpect(status().isBadRequest());

        // Validate the Clauses in the database
        List<Clauses> clausesList = clausesRepository.findAll();
        assertThat(clausesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClauses() throws Exception {
        // Initialize the database
        clausesRepository.saveAndFlush(clauses);

        int databaseSizeBeforeDelete = clausesRepository.findAll().size();

        // Delete the clauses
        restClausesMockMvc.perform(delete("/api/clauses/{id}", clauses.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Clauses> clausesList = clausesRepository.findAll();
        assertThat(clausesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
