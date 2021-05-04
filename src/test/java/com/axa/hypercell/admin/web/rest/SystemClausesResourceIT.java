package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.SystemClauses;
import com.axa.hypercell.admin.domain.enumeration.ClauseType;
import com.axa.hypercell.admin.domain.enumeration.Status;
import com.axa.hypercell.admin.repository.SystemClausesRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SystemClausesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SystemClausesResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT_EN = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_EN = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT_AR = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_AR = "BBBBBBBBBB";

    private static final ClauseType DEFAULT_CLAUSE_TYPE = ClauseType.TermsAndCondition;
    private static final ClauseType UPDATED_CLAUSE_TYPE = ClauseType.Clause;

    private static final LocalDate DEFAULT_EFFECTIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EFFECTIVE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Status DEFAULT_STATUS = Status.Active;
    private static final Status UPDATED_STATUS = Status.InActive;

    private static final String ENTITY_API_URL = "/api/system-clauses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SystemClausesRepository systemClausesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSystemClausesMockMvc;

    private SystemClauses systemClauses;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemClauses createEntity(EntityManager em) {
        SystemClauses systemClauses = new SystemClauses()
            .code(DEFAULT_CODE)
            .textEn(DEFAULT_TEXT_EN)
            .textAr(DEFAULT_TEXT_AR)
            .clauseType(DEFAULT_CLAUSE_TYPE)
            .effectiveDate(DEFAULT_EFFECTIVE_DATE)
            .status(DEFAULT_STATUS);
        return systemClauses;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemClauses createUpdatedEntity(EntityManager em) {
        SystemClauses systemClauses = new SystemClauses()
            .code(UPDATED_CODE)
            .textEn(UPDATED_TEXT_EN)
            .textAr(UPDATED_TEXT_AR)
            .clauseType(UPDATED_CLAUSE_TYPE)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .status(UPDATED_STATUS);
        return systemClauses;
    }

    @BeforeEach
    public void initTest() {
        systemClauses = createEntity(em);
    }

    @Test
    @Transactional
    void createSystemClauses() throws Exception {
        int databaseSizeBeforeCreate = systemClausesRepository.findAll().size();
        // Create the SystemClauses
        restSystemClausesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemClauses)))
            .andExpect(status().isCreated());

        // Validate the SystemClauses in the database
        List<SystemClauses> systemClausesList = systemClausesRepository.findAll();
        assertThat(systemClausesList).hasSize(databaseSizeBeforeCreate + 1);
        SystemClauses testSystemClauses = systemClausesList.get(systemClausesList.size() - 1);
        assertThat(testSystemClauses.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSystemClauses.getTextEn()).isEqualTo(DEFAULT_TEXT_EN);
        assertThat(testSystemClauses.getTextAr()).isEqualTo(DEFAULT_TEXT_AR);
        assertThat(testSystemClauses.getClauseType()).isEqualTo(DEFAULT_CLAUSE_TYPE);
        assertThat(testSystemClauses.getEffectiveDate()).isEqualTo(DEFAULT_EFFECTIVE_DATE);
        assertThat(testSystemClauses.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createSystemClausesWithExistingId() throws Exception {
        // Create the SystemClauses with an existing ID
        systemClauses.setId(1L);

        int databaseSizeBeforeCreate = systemClausesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemClausesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemClauses)))
            .andExpect(status().isBadRequest());

        // Validate the SystemClauses in the database
        List<SystemClauses> systemClausesList = systemClausesRepository.findAll();
        assertThat(systemClausesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSystemClauses() throws Exception {
        // Initialize the database
        systemClausesRepository.saveAndFlush(systemClauses);

        // Get all the systemClausesList
        restSystemClausesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemClauses.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].textEn").value(hasItem(DEFAULT_TEXT_EN)))
            .andExpect(jsonPath("$.[*].textAr").value(hasItem(DEFAULT_TEXT_AR)))
            .andExpect(jsonPath("$.[*].clauseType").value(hasItem(DEFAULT_CLAUSE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].effectiveDate").value(hasItem(DEFAULT_EFFECTIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getSystemClauses() throws Exception {
        // Initialize the database
        systemClausesRepository.saveAndFlush(systemClauses);

        // Get the systemClauses
        restSystemClausesMockMvc
            .perform(get(ENTITY_API_URL_ID, systemClauses.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemClauses.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.textEn").value(DEFAULT_TEXT_EN))
            .andExpect(jsonPath("$.textAr").value(DEFAULT_TEXT_AR))
            .andExpect(jsonPath("$.clauseType").value(DEFAULT_CLAUSE_TYPE.toString()))
            .andExpect(jsonPath("$.effectiveDate").value(DEFAULT_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSystemClauses() throws Exception {
        // Get the systemClauses
        restSystemClausesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSystemClauses() throws Exception {
        // Initialize the database
        systemClausesRepository.saveAndFlush(systemClauses);

        int databaseSizeBeforeUpdate = systemClausesRepository.findAll().size();

        // Update the systemClauses
        SystemClauses updatedSystemClauses = systemClausesRepository.findById(systemClauses.getId()).get();
        // Disconnect from session so that the updates on updatedSystemClauses are not directly saved in db
        em.detach(updatedSystemClauses);
        updatedSystemClauses
            .code(UPDATED_CODE)
            .textEn(UPDATED_TEXT_EN)
            .textAr(UPDATED_TEXT_AR)
            .clauseType(UPDATED_CLAUSE_TYPE)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .status(UPDATED_STATUS);

        restSystemClausesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSystemClauses.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSystemClauses))
            )
            .andExpect(status().isOk());

        // Validate the SystemClauses in the database
        List<SystemClauses> systemClausesList = systemClausesRepository.findAll();
        assertThat(systemClausesList).hasSize(databaseSizeBeforeUpdate);
        SystemClauses testSystemClauses = systemClausesList.get(systemClausesList.size() - 1);
        assertThat(testSystemClauses.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSystemClauses.getTextEn()).isEqualTo(UPDATED_TEXT_EN);
        assertThat(testSystemClauses.getTextAr()).isEqualTo(UPDATED_TEXT_AR);
        assertThat(testSystemClauses.getClauseType()).isEqualTo(UPDATED_CLAUSE_TYPE);
        assertThat(testSystemClauses.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testSystemClauses.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingSystemClauses() throws Exception {
        int databaseSizeBeforeUpdate = systemClausesRepository.findAll().size();
        systemClauses.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemClausesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemClauses.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemClauses))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemClauses in the database
        List<SystemClauses> systemClausesList = systemClausesRepository.findAll();
        assertThat(systemClausesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSystemClauses() throws Exception {
        int databaseSizeBeforeUpdate = systemClausesRepository.findAll().size();
        systemClauses.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemClausesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemClauses))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemClauses in the database
        List<SystemClauses> systemClausesList = systemClausesRepository.findAll();
        assertThat(systemClausesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSystemClauses() throws Exception {
        int databaseSizeBeforeUpdate = systemClausesRepository.findAll().size();
        systemClauses.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemClausesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(systemClauses)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemClauses in the database
        List<SystemClauses> systemClausesList = systemClausesRepository.findAll();
        assertThat(systemClausesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSystemClausesWithPatch() throws Exception {
        // Initialize the database
        systemClausesRepository.saveAndFlush(systemClauses);

        int databaseSizeBeforeUpdate = systemClausesRepository.findAll().size();

        // Update the systemClauses using partial update
        SystemClauses partialUpdatedSystemClauses = new SystemClauses();
        partialUpdatedSystemClauses.setId(systemClauses.getId());

        partialUpdatedSystemClauses.textEn(UPDATED_TEXT_EN).textAr(UPDATED_TEXT_AR);

        restSystemClausesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemClauses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystemClauses))
            )
            .andExpect(status().isOk());

        // Validate the SystemClauses in the database
        List<SystemClauses> systemClausesList = systemClausesRepository.findAll();
        assertThat(systemClausesList).hasSize(databaseSizeBeforeUpdate);
        SystemClauses testSystemClauses = systemClausesList.get(systemClausesList.size() - 1);
        assertThat(testSystemClauses.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSystemClauses.getTextEn()).isEqualTo(UPDATED_TEXT_EN);
        assertThat(testSystemClauses.getTextAr()).isEqualTo(UPDATED_TEXT_AR);
        assertThat(testSystemClauses.getClauseType()).isEqualTo(DEFAULT_CLAUSE_TYPE);
        assertThat(testSystemClauses.getEffectiveDate()).isEqualTo(DEFAULT_EFFECTIVE_DATE);
        assertThat(testSystemClauses.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateSystemClausesWithPatch() throws Exception {
        // Initialize the database
        systemClausesRepository.saveAndFlush(systemClauses);

        int databaseSizeBeforeUpdate = systemClausesRepository.findAll().size();

        // Update the systemClauses using partial update
        SystemClauses partialUpdatedSystemClauses = new SystemClauses();
        partialUpdatedSystemClauses.setId(systemClauses.getId());

        partialUpdatedSystemClauses
            .code(UPDATED_CODE)
            .textEn(UPDATED_TEXT_EN)
            .textAr(UPDATED_TEXT_AR)
            .clauseType(UPDATED_CLAUSE_TYPE)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .status(UPDATED_STATUS);

        restSystemClausesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemClauses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystemClauses))
            )
            .andExpect(status().isOk());

        // Validate the SystemClauses in the database
        List<SystemClauses> systemClausesList = systemClausesRepository.findAll();
        assertThat(systemClausesList).hasSize(databaseSizeBeforeUpdate);
        SystemClauses testSystemClauses = systemClausesList.get(systemClausesList.size() - 1);
        assertThat(testSystemClauses.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSystemClauses.getTextEn()).isEqualTo(UPDATED_TEXT_EN);
        assertThat(testSystemClauses.getTextAr()).isEqualTo(UPDATED_TEXT_AR);
        assertThat(testSystemClauses.getClauseType()).isEqualTo(UPDATED_CLAUSE_TYPE);
        assertThat(testSystemClauses.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testSystemClauses.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingSystemClauses() throws Exception {
        int databaseSizeBeforeUpdate = systemClausesRepository.findAll().size();
        systemClauses.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemClausesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, systemClauses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemClauses))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemClauses in the database
        List<SystemClauses> systemClausesList = systemClausesRepository.findAll();
        assertThat(systemClausesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSystemClauses() throws Exception {
        int databaseSizeBeforeUpdate = systemClausesRepository.findAll().size();
        systemClauses.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemClausesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemClauses))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemClauses in the database
        List<SystemClauses> systemClausesList = systemClausesRepository.findAll();
        assertThat(systemClausesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSystemClauses() throws Exception {
        int databaseSizeBeforeUpdate = systemClausesRepository.findAll().size();
        systemClauses.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemClausesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(systemClauses))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemClauses in the database
        List<SystemClauses> systemClausesList = systemClausesRepository.findAll();
        assertThat(systemClausesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSystemClauses() throws Exception {
        // Initialize the database
        systemClausesRepository.saveAndFlush(systemClauses);

        int databaseSizeBeforeDelete = systemClausesRepository.findAll().size();

        // Delete the systemClauses
        restSystemClausesMockMvc
            .perform(delete(ENTITY_API_URL_ID, systemClauses.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SystemClauses> systemClausesList = systemClausesRepository.findAll();
        assertThat(systemClausesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
