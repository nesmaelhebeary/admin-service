package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.LkClausesParameters;
import com.axa.hypercell.admin.repository.LkClausesParametersRepository;
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
 * Integration tests for the {@link LkClausesParametersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LkClausesParametersResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/lk-clauses-parameters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LkClausesParametersRepository lkClausesParametersRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLkClausesParametersMockMvc;

    private LkClausesParameters lkClausesParameters;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LkClausesParameters createEntity(EntityManager em) {
        LkClausesParameters lkClausesParameters = new LkClausesParameters().name(DEFAULT_NAME);
        return lkClausesParameters;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LkClausesParameters createUpdatedEntity(EntityManager em) {
        LkClausesParameters lkClausesParameters = new LkClausesParameters().name(UPDATED_NAME);
        return lkClausesParameters;
    }

    @BeforeEach
    public void initTest() {
        lkClausesParameters = createEntity(em);
    }

    @Test
    @Transactional
    void createLkClausesParameters() throws Exception {
        int databaseSizeBeforeCreate = lkClausesParametersRepository.findAll().size();
        // Create the LkClausesParameters
        restLkClausesParametersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lkClausesParameters))
            )
            .andExpect(status().isCreated());

        // Validate the LkClausesParameters in the database
        List<LkClausesParameters> lkClausesParametersList = lkClausesParametersRepository.findAll();
        assertThat(lkClausesParametersList).hasSize(databaseSizeBeforeCreate + 1);
        LkClausesParameters testLkClausesParameters = lkClausesParametersList.get(lkClausesParametersList.size() - 1);
        assertThat(testLkClausesParameters.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createLkClausesParametersWithExistingId() throws Exception {
        // Create the LkClausesParameters with an existing ID
        lkClausesParameters.setId(1L);

        int databaseSizeBeforeCreate = lkClausesParametersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLkClausesParametersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lkClausesParameters))
            )
            .andExpect(status().isBadRequest());

        // Validate the LkClausesParameters in the database
        List<LkClausesParameters> lkClausesParametersList = lkClausesParametersRepository.findAll();
        assertThat(lkClausesParametersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLkClausesParameters() throws Exception {
        // Initialize the database
        lkClausesParametersRepository.saveAndFlush(lkClausesParameters);

        // Get all the lkClausesParametersList
        restLkClausesParametersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lkClausesParameters.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getLkClausesParameters() throws Exception {
        // Initialize the database
        lkClausesParametersRepository.saveAndFlush(lkClausesParameters);

        // Get the lkClausesParameters
        restLkClausesParametersMockMvc
            .perform(get(ENTITY_API_URL_ID, lkClausesParameters.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lkClausesParameters.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingLkClausesParameters() throws Exception {
        // Get the lkClausesParameters
        restLkClausesParametersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLkClausesParameters() throws Exception {
        // Initialize the database
        lkClausesParametersRepository.saveAndFlush(lkClausesParameters);

        int databaseSizeBeforeUpdate = lkClausesParametersRepository.findAll().size();

        // Update the lkClausesParameters
        LkClausesParameters updatedLkClausesParameters = lkClausesParametersRepository.findById(lkClausesParameters.getId()).get();
        // Disconnect from session so that the updates on updatedLkClausesParameters are not directly saved in db
        em.detach(updatedLkClausesParameters);
        updatedLkClausesParameters.name(UPDATED_NAME);

        restLkClausesParametersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLkClausesParameters.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLkClausesParameters))
            )
            .andExpect(status().isOk());

        // Validate the LkClausesParameters in the database
        List<LkClausesParameters> lkClausesParametersList = lkClausesParametersRepository.findAll();
        assertThat(lkClausesParametersList).hasSize(databaseSizeBeforeUpdate);
        LkClausesParameters testLkClausesParameters = lkClausesParametersList.get(lkClausesParametersList.size() - 1);
        assertThat(testLkClausesParameters.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingLkClausesParameters() throws Exception {
        int databaseSizeBeforeUpdate = lkClausesParametersRepository.findAll().size();
        lkClausesParameters.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLkClausesParametersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lkClausesParameters.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lkClausesParameters))
            )
            .andExpect(status().isBadRequest());

        // Validate the LkClausesParameters in the database
        List<LkClausesParameters> lkClausesParametersList = lkClausesParametersRepository.findAll();
        assertThat(lkClausesParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLkClausesParameters() throws Exception {
        int databaseSizeBeforeUpdate = lkClausesParametersRepository.findAll().size();
        lkClausesParameters.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLkClausesParametersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lkClausesParameters))
            )
            .andExpect(status().isBadRequest());

        // Validate the LkClausesParameters in the database
        List<LkClausesParameters> lkClausesParametersList = lkClausesParametersRepository.findAll();
        assertThat(lkClausesParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLkClausesParameters() throws Exception {
        int databaseSizeBeforeUpdate = lkClausesParametersRepository.findAll().size();
        lkClausesParameters.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLkClausesParametersMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lkClausesParameters))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LkClausesParameters in the database
        List<LkClausesParameters> lkClausesParametersList = lkClausesParametersRepository.findAll();
        assertThat(lkClausesParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLkClausesParametersWithPatch() throws Exception {
        // Initialize the database
        lkClausesParametersRepository.saveAndFlush(lkClausesParameters);

        int databaseSizeBeforeUpdate = lkClausesParametersRepository.findAll().size();

        // Update the lkClausesParameters using partial update
        LkClausesParameters partialUpdatedLkClausesParameters = new LkClausesParameters();
        partialUpdatedLkClausesParameters.setId(lkClausesParameters.getId());

        partialUpdatedLkClausesParameters.name(UPDATED_NAME);

        restLkClausesParametersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLkClausesParameters.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLkClausesParameters))
            )
            .andExpect(status().isOk());

        // Validate the LkClausesParameters in the database
        List<LkClausesParameters> lkClausesParametersList = lkClausesParametersRepository.findAll();
        assertThat(lkClausesParametersList).hasSize(databaseSizeBeforeUpdate);
        LkClausesParameters testLkClausesParameters = lkClausesParametersList.get(lkClausesParametersList.size() - 1);
        assertThat(testLkClausesParameters.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateLkClausesParametersWithPatch() throws Exception {
        // Initialize the database
        lkClausesParametersRepository.saveAndFlush(lkClausesParameters);

        int databaseSizeBeforeUpdate = lkClausesParametersRepository.findAll().size();

        // Update the lkClausesParameters using partial update
        LkClausesParameters partialUpdatedLkClausesParameters = new LkClausesParameters();
        partialUpdatedLkClausesParameters.setId(lkClausesParameters.getId());

        partialUpdatedLkClausesParameters.name(UPDATED_NAME);

        restLkClausesParametersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLkClausesParameters.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLkClausesParameters))
            )
            .andExpect(status().isOk());

        // Validate the LkClausesParameters in the database
        List<LkClausesParameters> lkClausesParametersList = lkClausesParametersRepository.findAll();
        assertThat(lkClausesParametersList).hasSize(databaseSizeBeforeUpdate);
        LkClausesParameters testLkClausesParameters = lkClausesParametersList.get(lkClausesParametersList.size() - 1);
        assertThat(testLkClausesParameters.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingLkClausesParameters() throws Exception {
        int databaseSizeBeforeUpdate = lkClausesParametersRepository.findAll().size();
        lkClausesParameters.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLkClausesParametersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lkClausesParameters.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lkClausesParameters))
            )
            .andExpect(status().isBadRequest());

        // Validate the LkClausesParameters in the database
        List<LkClausesParameters> lkClausesParametersList = lkClausesParametersRepository.findAll();
        assertThat(lkClausesParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLkClausesParameters() throws Exception {
        int databaseSizeBeforeUpdate = lkClausesParametersRepository.findAll().size();
        lkClausesParameters.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLkClausesParametersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lkClausesParameters))
            )
            .andExpect(status().isBadRequest());

        // Validate the LkClausesParameters in the database
        List<LkClausesParameters> lkClausesParametersList = lkClausesParametersRepository.findAll();
        assertThat(lkClausesParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLkClausesParameters() throws Exception {
        int databaseSizeBeforeUpdate = lkClausesParametersRepository.findAll().size();
        lkClausesParameters.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLkClausesParametersMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lkClausesParameters))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LkClausesParameters in the database
        List<LkClausesParameters> lkClausesParametersList = lkClausesParametersRepository.findAll();
        assertThat(lkClausesParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLkClausesParameters() throws Exception {
        // Initialize the database
        lkClausesParametersRepository.saveAndFlush(lkClausesParameters);

        int databaseSizeBeforeDelete = lkClausesParametersRepository.findAll().size();

        // Delete the lkClausesParameters
        restLkClausesParametersMockMvc
            .perform(delete(ENTITY_API_URL_ID, lkClausesParameters.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LkClausesParameters> lkClausesParametersList = lkClausesParametersRepository.findAll();
        assertThat(lkClausesParametersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
