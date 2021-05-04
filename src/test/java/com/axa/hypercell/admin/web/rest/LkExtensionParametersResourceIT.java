package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.LkExtensionParameters;
import com.axa.hypercell.admin.domain.enumeration.DataType;
import com.axa.hypercell.admin.repository.LkExtensionParametersRepository;
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
 * Integration tests for the {@link LkExtensionParametersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LkExtensionParametersResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final DataType DEFAULT_DATA_TYPE = DataType.STRING;
    private static final DataType UPDATED_DATA_TYPE = DataType.NUMBER;

    private static final String ENTITY_API_URL = "/api/lk-extension-parameters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LkExtensionParametersRepository lkExtensionParametersRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLkExtensionParametersMockMvc;

    private LkExtensionParameters lkExtensionParameters;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LkExtensionParameters createEntity(EntityManager em) {
        LkExtensionParameters lkExtensionParameters = new LkExtensionParameters().name(DEFAULT_NAME).dataType(DEFAULT_DATA_TYPE);
        return lkExtensionParameters;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LkExtensionParameters createUpdatedEntity(EntityManager em) {
        LkExtensionParameters lkExtensionParameters = new LkExtensionParameters().name(UPDATED_NAME).dataType(UPDATED_DATA_TYPE);
        return lkExtensionParameters;
    }

    @BeforeEach
    public void initTest() {
        lkExtensionParameters = createEntity(em);
    }

    @Test
    @Transactional
    void createLkExtensionParameters() throws Exception {
        int databaseSizeBeforeCreate = lkExtensionParametersRepository.findAll().size();
        // Create the LkExtensionParameters
        restLkExtensionParametersMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lkExtensionParameters))
            )
            .andExpect(status().isCreated());

        // Validate the LkExtensionParameters in the database
        List<LkExtensionParameters> lkExtensionParametersList = lkExtensionParametersRepository.findAll();
        assertThat(lkExtensionParametersList).hasSize(databaseSizeBeforeCreate + 1);
        LkExtensionParameters testLkExtensionParameters = lkExtensionParametersList.get(lkExtensionParametersList.size() - 1);
        assertThat(testLkExtensionParameters.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLkExtensionParameters.getDataType()).isEqualTo(DEFAULT_DATA_TYPE);
    }

    @Test
    @Transactional
    void createLkExtensionParametersWithExistingId() throws Exception {
        // Create the LkExtensionParameters with an existing ID
        lkExtensionParameters.setId(1L);

        int databaseSizeBeforeCreate = lkExtensionParametersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLkExtensionParametersMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lkExtensionParameters))
            )
            .andExpect(status().isBadRequest());

        // Validate the LkExtensionParameters in the database
        List<LkExtensionParameters> lkExtensionParametersList = lkExtensionParametersRepository.findAll();
        assertThat(lkExtensionParametersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLkExtensionParameters() throws Exception {
        // Initialize the database
        lkExtensionParametersRepository.saveAndFlush(lkExtensionParameters);

        // Get all the lkExtensionParametersList
        restLkExtensionParametersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lkExtensionParameters.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())));
    }

    @Test
    @Transactional
    void getLkExtensionParameters() throws Exception {
        // Initialize the database
        lkExtensionParametersRepository.saveAndFlush(lkExtensionParameters);

        // Get the lkExtensionParameters
        restLkExtensionParametersMockMvc
            .perform(get(ENTITY_API_URL_ID, lkExtensionParameters.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lkExtensionParameters.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLkExtensionParameters() throws Exception {
        // Get the lkExtensionParameters
        restLkExtensionParametersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLkExtensionParameters() throws Exception {
        // Initialize the database
        lkExtensionParametersRepository.saveAndFlush(lkExtensionParameters);

        int databaseSizeBeforeUpdate = lkExtensionParametersRepository.findAll().size();

        // Update the lkExtensionParameters
        LkExtensionParameters updatedLkExtensionParameters = lkExtensionParametersRepository.findById(lkExtensionParameters.getId()).get();
        // Disconnect from session so that the updates on updatedLkExtensionParameters are not directly saved in db
        em.detach(updatedLkExtensionParameters);
        updatedLkExtensionParameters.name(UPDATED_NAME).dataType(UPDATED_DATA_TYPE);

        restLkExtensionParametersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLkExtensionParameters.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLkExtensionParameters))
            )
            .andExpect(status().isOk());

        // Validate the LkExtensionParameters in the database
        List<LkExtensionParameters> lkExtensionParametersList = lkExtensionParametersRepository.findAll();
        assertThat(lkExtensionParametersList).hasSize(databaseSizeBeforeUpdate);
        LkExtensionParameters testLkExtensionParameters = lkExtensionParametersList.get(lkExtensionParametersList.size() - 1);
        assertThat(testLkExtensionParameters.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLkExtensionParameters.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingLkExtensionParameters() throws Exception {
        int databaseSizeBeforeUpdate = lkExtensionParametersRepository.findAll().size();
        lkExtensionParameters.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLkExtensionParametersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lkExtensionParameters.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lkExtensionParameters))
            )
            .andExpect(status().isBadRequest());

        // Validate the LkExtensionParameters in the database
        List<LkExtensionParameters> lkExtensionParametersList = lkExtensionParametersRepository.findAll();
        assertThat(lkExtensionParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLkExtensionParameters() throws Exception {
        int databaseSizeBeforeUpdate = lkExtensionParametersRepository.findAll().size();
        lkExtensionParameters.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLkExtensionParametersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lkExtensionParameters))
            )
            .andExpect(status().isBadRequest());

        // Validate the LkExtensionParameters in the database
        List<LkExtensionParameters> lkExtensionParametersList = lkExtensionParametersRepository.findAll();
        assertThat(lkExtensionParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLkExtensionParameters() throws Exception {
        int databaseSizeBeforeUpdate = lkExtensionParametersRepository.findAll().size();
        lkExtensionParameters.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLkExtensionParametersMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lkExtensionParameters))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LkExtensionParameters in the database
        List<LkExtensionParameters> lkExtensionParametersList = lkExtensionParametersRepository.findAll();
        assertThat(lkExtensionParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLkExtensionParametersWithPatch() throws Exception {
        // Initialize the database
        lkExtensionParametersRepository.saveAndFlush(lkExtensionParameters);

        int databaseSizeBeforeUpdate = lkExtensionParametersRepository.findAll().size();

        // Update the lkExtensionParameters using partial update
        LkExtensionParameters partialUpdatedLkExtensionParameters = new LkExtensionParameters();
        partialUpdatedLkExtensionParameters.setId(lkExtensionParameters.getId());

        partialUpdatedLkExtensionParameters.dataType(UPDATED_DATA_TYPE);

        restLkExtensionParametersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLkExtensionParameters.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLkExtensionParameters))
            )
            .andExpect(status().isOk());

        // Validate the LkExtensionParameters in the database
        List<LkExtensionParameters> lkExtensionParametersList = lkExtensionParametersRepository.findAll();
        assertThat(lkExtensionParametersList).hasSize(databaseSizeBeforeUpdate);
        LkExtensionParameters testLkExtensionParameters = lkExtensionParametersList.get(lkExtensionParametersList.size() - 1);
        assertThat(testLkExtensionParameters.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLkExtensionParameters.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateLkExtensionParametersWithPatch() throws Exception {
        // Initialize the database
        lkExtensionParametersRepository.saveAndFlush(lkExtensionParameters);

        int databaseSizeBeforeUpdate = lkExtensionParametersRepository.findAll().size();

        // Update the lkExtensionParameters using partial update
        LkExtensionParameters partialUpdatedLkExtensionParameters = new LkExtensionParameters();
        partialUpdatedLkExtensionParameters.setId(lkExtensionParameters.getId());

        partialUpdatedLkExtensionParameters.name(UPDATED_NAME).dataType(UPDATED_DATA_TYPE);

        restLkExtensionParametersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLkExtensionParameters.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLkExtensionParameters))
            )
            .andExpect(status().isOk());

        // Validate the LkExtensionParameters in the database
        List<LkExtensionParameters> lkExtensionParametersList = lkExtensionParametersRepository.findAll();
        assertThat(lkExtensionParametersList).hasSize(databaseSizeBeforeUpdate);
        LkExtensionParameters testLkExtensionParameters = lkExtensionParametersList.get(lkExtensionParametersList.size() - 1);
        assertThat(testLkExtensionParameters.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLkExtensionParameters.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingLkExtensionParameters() throws Exception {
        int databaseSizeBeforeUpdate = lkExtensionParametersRepository.findAll().size();
        lkExtensionParameters.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLkExtensionParametersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lkExtensionParameters.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lkExtensionParameters))
            )
            .andExpect(status().isBadRequest());

        // Validate the LkExtensionParameters in the database
        List<LkExtensionParameters> lkExtensionParametersList = lkExtensionParametersRepository.findAll();
        assertThat(lkExtensionParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLkExtensionParameters() throws Exception {
        int databaseSizeBeforeUpdate = lkExtensionParametersRepository.findAll().size();
        lkExtensionParameters.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLkExtensionParametersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lkExtensionParameters))
            )
            .andExpect(status().isBadRequest());

        // Validate the LkExtensionParameters in the database
        List<LkExtensionParameters> lkExtensionParametersList = lkExtensionParametersRepository.findAll();
        assertThat(lkExtensionParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLkExtensionParameters() throws Exception {
        int databaseSizeBeforeUpdate = lkExtensionParametersRepository.findAll().size();
        lkExtensionParameters.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLkExtensionParametersMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lkExtensionParameters))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LkExtensionParameters in the database
        List<LkExtensionParameters> lkExtensionParametersList = lkExtensionParametersRepository.findAll();
        assertThat(lkExtensionParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLkExtensionParameters() throws Exception {
        // Initialize the database
        lkExtensionParametersRepository.saveAndFlush(lkExtensionParameters);

        int databaseSizeBeforeDelete = lkExtensionParametersRepository.findAll().size();

        // Delete the lkExtensionParameters
        restLkExtensionParametersMockMvc
            .perform(delete(ENTITY_API_URL_ID, lkExtensionParameters.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LkExtensionParameters> lkExtensionParametersList = lkExtensionParametersRepository.findAll();
        assertThat(lkExtensionParametersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
