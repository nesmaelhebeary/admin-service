package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.EvaluationBasis;
import com.axa.hypercell.admin.repository.EvaluationBasisRepository;
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
 * Integration tests for the {@link EvaluationBasisResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EvaluationBasisResourceIT {

    private static final String DEFAULT_NAME_AR = "AAAAAAAAAA";
    private static final String UPDATED_NAME_AR = "BBBBBBBBBB";

    private static final String DEFAULT_NAEM_EN = "AAAAAAAAAA";
    private static final String UPDATED_NAEM_EN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/evaluation-bases";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EvaluationBasisRepository evaluationBasisRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEvaluationBasisMockMvc;

    private EvaluationBasis evaluationBasis;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EvaluationBasis createEntity(EntityManager em) {
        EvaluationBasis evaluationBasis = new EvaluationBasis().nameAr(DEFAULT_NAME_AR).naemEn(DEFAULT_NAEM_EN);
        return evaluationBasis;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EvaluationBasis createUpdatedEntity(EntityManager em) {
        EvaluationBasis evaluationBasis = new EvaluationBasis().nameAr(UPDATED_NAME_AR).naemEn(UPDATED_NAEM_EN);
        return evaluationBasis;
    }

    @BeforeEach
    public void initTest() {
        evaluationBasis = createEntity(em);
    }

    @Test
    @Transactional
    void createEvaluationBasis() throws Exception {
        int databaseSizeBeforeCreate = evaluationBasisRepository.findAll().size();
        // Create the EvaluationBasis
        restEvaluationBasisMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaluationBasis))
            )
            .andExpect(status().isCreated());

        // Validate the EvaluationBasis in the database
        List<EvaluationBasis> evaluationBasisList = evaluationBasisRepository.findAll();
        assertThat(evaluationBasisList).hasSize(databaseSizeBeforeCreate + 1);
        EvaluationBasis testEvaluationBasis = evaluationBasisList.get(evaluationBasisList.size() - 1);
        assertThat(testEvaluationBasis.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
        assertThat(testEvaluationBasis.getNaemEn()).isEqualTo(DEFAULT_NAEM_EN);
    }

    @Test
    @Transactional
    void createEvaluationBasisWithExistingId() throws Exception {
        // Create the EvaluationBasis with an existing ID
        evaluationBasis.setId(1L);

        int databaseSizeBeforeCreate = evaluationBasisRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEvaluationBasisMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaluationBasis))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvaluationBasis in the database
        List<EvaluationBasis> evaluationBasisList = evaluationBasisRepository.findAll();
        assertThat(evaluationBasisList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEvaluationBases() throws Exception {
        // Initialize the database
        evaluationBasisRepository.saveAndFlush(evaluationBasis);

        // Get all the evaluationBasisList
        restEvaluationBasisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evaluationBasis.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].naemEn").value(hasItem(DEFAULT_NAEM_EN)));
    }

    @Test
    @Transactional
    void getEvaluationBasis() throws Exception {
        // Initialize the database
        evaluationBasisRepository.saveAndFlush(evaluationBasis);

        // Get the evaluationBasis
        restEvaluationBasisMockMvc
            .perform(get(ENTITY_API_URL_ID, evaluationBasis.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(evaluationBasis.getId().intValue()))
            .andExpect(jsonPath("$.nameAr").value(DEFAULT_NAME_AR))
            .andExpect(jsonPath("$.naemEn").value(DEFAULT_NAEM_EN));
    }

    @Test
    @Transactional
    void getNonExistingEvaluationBasis() throws Exception {
        // Get the evaluationBasis
        restEvaluationBasisMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEvaluationBasis() throws Exception {
        // Initialize the database
        evaluationBasisRepository.saveAndFlush(evaluationBasis);

        int databaseSizeBeforeUpdate = evaluationBasisRepository.findAll().size();

        // Update the evaluationBasis
        EvaluationBasis updatedEvaluationBasis = evaluationBasisRepository.findById(evaluationBasis.getId()).get();
        // Disconnect from session so that the updates on updatedEvaluationBasis are not directly saved in db
        em.detach(updatedEvaluationBasis);
        updatedEvaluationBasis.nameAr(UPDATED_NAME_AR).naemEn(UPDATED_NAEM_EN);

        restEvaluationBasisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEvaluationBasis.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEvaluationBasis))
            )
            .andExpect(status().isOk());

        // Validate the EvaluationBasis in the database
        List<EvaluationBasis> evaluationBasisList = evaluationBasisRepository.findAll();
        assertThat(evaluationBasisList).hasSize(databaseSizeBeforeUpdate);
        EvaluationBasis testEvaluationBasis = evaluationBasisList.get(evaluationBasisList.size() - 1);
        assertThat(testEvaluationBasis.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testEvaluationBasis.getNaemEn()).isEqualTo(UPDATED_NAEM_EN);
    }

    @Test
    @Transactional
    void putNonExistingEvaluationBasis() throws Exception {
        int databaseSizeBeforeUpdate = evaluationBasisRepository.findAll().size();
        evaluationBasis.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvaluationBasisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, evaluationBasis.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(evaluationBasis))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvaluationBasis in the database
        List<EvaluationBasis> evaluationBasisList = evaluationBasisRepository.findAll();
        assertThat(evaluationBasisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvaluationBasis() throws Exception {
        int databaseSizeBeforeUpdate = evaluationBasisRepository.findAll().size();
        evaluationBasis.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluationBasisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(evaluationBasis))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvaluationBasis in the database
        List<EvaluationBasis> evaluationBasisList = evaluationBasisRepository.findAll();
        assertThat(evaluationBasisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvaluationBasis() throws Exception {
        int databaseSizeBeforeUpdate = evaluationBasisRepository.findAll().size();
        evaluationBasis.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluationBasisMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evaluationBasis))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EvaluationBasis in the database
        List<EvaluationBasis> evaluationBasisList = evaluationBasisRepository.findAll();
        assertThat(evaluationBasisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEvaluationBasisWithPatch() throws Exception {
        // Initialize the database
        evaluationBasisRepository.saveAndFlush(evaluationBasis);

        int databaseSizeBeforeUpdate = evaluationBasisRepository.findAll().size();

        // Update the evaluationBasis using partial update
        EvaluationBasis partialUpdatedEvaluationBasis = new EvaluationBasis();
        partialUpdatedEvaluationBasis.setId(evaluationBasis.getId());

        restEvaluationBasisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvaluationBasis.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvaluationBasis))
            )
            .andExpect(status().isOk());

        // Validate the EvaluationBasis in the database
        List<EvaluationBasis> evaluationBasisList = evaluationBasisRepository.findAll();
        assertThat(evaluationBasisList).hasSize(databaseSizeBeforeUpdate);
        EvaluationBasis testEvaluationBasis = evaluationBasisList.get(evaluationBasisList.size() - 1);
        assertThat(testEvaluationBasis.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
        assertThat(testEvaluationBasis.getNaemEn()).isEqualTo(DEFAULT_NAEM_EN);
    }

    @Test
    @Transactional
    void fullUpdateEvaluationBasisWithPatch() throws Exception {
        // Initialize the database
        evaluationBasisRepository.saveAndFlush(evaluationBasis);

        int databaseSizeBeforeUpdate = evaluationBasisRepository.findAll().size();

        // Update the evaluationBasis using partial update
        EvaluationBasis partialUpdatedEvaluationBasis = new EvaluationBasis();
        partialUpdatedEvaluationBasis.setId(evaluationBasis.getId());

        partialUpdatedEvaluationBasis.nameAr(UPDATED_NAME_AR).naemEn(UPDATED_NAEM_EN);

        restEvaluationBasisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvaluationBasis.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvaluationBasis))
            )
            .andExpect(status().isOk());

        // Validate the EvaluationBasis in the database
        List<EvaluationBasis> evaluationBasisList = evaluationBasisRepository.findAll();
        assertThat(evaluationBasisList).hasSize(databaseSizeBeforeUpdate);
        EvaluationBasis testEvaluationBasis = evaluationBasisList.get(evaluationBasisList.size() - 1);
        assertThat(testEvaluationBasis.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testEvaluationBasis.getNaemEn()).isEqualTo(UPDATED_NAEM_EN);
    }

    @Test
    @Transactional
    void patchNonExistingEvaluationBasis() throws Exception {
        int databaseSizeBeforeUpdate = evaluationBasisRepository.findAll().size();
        evaluationBasis.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvaluationBasisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, evaluationBasis.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evaluationBasis))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvaluationBasis in the database
        List<EvaluationBasis> evaluationBasisList = evaluationBasisRepository.findAll();
        assertThat(evaluationBasisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvaluationBasis() throws Exception {
        int databaseSizeBeforeUpdate = evaluationBasisRepository.findAll().size();
        evaluationBasis.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluationBasisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evaluationBasis))
            )
            .andExpect(status().isBadRequest());

        // Validate the EvaluationBasis in the database
        List<EvaluationBasis> evaluationBasisList = evaluationBasisRepository.findAll();
        assertThat(evaluationBasisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvaluationBasis() throws Exception {
        int databaseSizeBeforeUpdate = evaluationBasisRepository.findAll().size();
        evaluationBasis.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvaluationBasisMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evaluationBasis))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EvaluationBasis in the database
        List<EvaluationBasis> evaluationBasisList = evaluationBasisRepository.findAll();
        assertThat(evaluationBasisList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEvaluationBasis() throws Exception {
        // Initialize the database
        evaluationBasisRepository.saveAndFlush(evaluationBasis);

        int databaseSizeBeforeDelete = evaluationBasisRepository.findAll().size();

        // Delete the evaluationBasis
        restEvaluationBasisMockMvc
            .perform(delete(ENTITY_API_URL_ID, evaluationBasis.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EvaluationBasis> evaluationBasisList = evaluationBasisRepository.findAll();
        assertThat(evaluationBasisList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
