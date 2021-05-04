package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.ExtensionParameters;
import com.axa.hypercell.admin.repository.ExtensionParametersRepository;
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
 * Integration tests for the {@link ExtensionParametersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExtensionParametersResourceIT {

    private static final Long DEFAULT_PARAMETER_ID = 1L;
    private static final Long UPDATED_PARAMETER_ID = 2L;

    private static final Long DEFAULT_EXTENSION_ID = 1L;
    private static final Long UPDATED_EXTENSION_ID = 2L;

    private static final String ENTITY_API_URL = "/api/extension-parameters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExtensionParametersRepository extensionParametersRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExtensionParametersMockMvc;

    private ExtensionParameters extensionParameters;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtensionParameters createEntity(EntityManager em) {
        ExtensionParameters extensionParameters = new ExtensionParameters()
            .parameterId(DEFAULT_PARAMETER_ID)
            .extensionId(DEFAULT_EXTENSION_ID);
        return extensionParameters;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtensionParameters createUpdatedEntity(EntityManager em) {
        ExtensionParameters extensionParameters = new ExtensionParameters()
            .parameterId(UPDATED_PARAMETER_ID)
            .extensionId(UPDATED_EXTENSION_ID);
        return extensionParameters;
    }

    @BeforeEach
    public void initTest() {
        extensionParameters = createEntity(em);
    }

    @Test
    @Transactional
    void createExtensionParameters() throws Exception {
        int databaseSizeBeforeCreate = extensionParametersRepository.findAll().size();
        // Create the ExtensionParameters
        restExtensionParametersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(extensionParameters))
            )
            .andExpect(status().isCreated());

        // Validate the ExtensionParameters in the database
        List<ExtensionParameters> extensionParametersList = extensionParametersRepository.findAll();
        assertThat(extensionParametersList).hasSize(databaseSizeBeforeCreate + 1);
        ExtensionParameters testExtensionParameters = extensionParametersList.get(extensionParametersList.size() - 1);
        assertThat(testExtensionParameters.getParameterId()).isEqualTo(DEFAULT_PARAMETER_ID);
        assertThat(testExtensionParameters.getExtensionId()).isEqualTo(DEFAULT_EXTENSION_ID);
    }

    @Test
    @Transactional
    void createExtensionParametersWithExistingId() throws Exception {
        // Create the ExtensionParameters with an existing ID
        extensionParameters.setId(1L);

        int databaseSizeBeforeCreate = extensionParametersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtensionParametersMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(extensionParameters))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtensionParameters in the database
        List<ExtensionParameters> extensionParametersList = extensionParametersRepository.findAll();
        assertThat(extensionParametersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExtensionParameters() throws Exception {
        // Initialize the database
        extensionParametersRepository.saveAndFlush(extensionParameters);

        // Get all the extensionParametersList
        restExtensionParametersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extensionParameters.getId().intValue())))
            .andExpect(jsonPath("$.[*].parameterId").value(hasItem(DEFAULT_PARAMETER_ID.intValue())))
            .andExpect(jsonPath("$.[*].extensionId").value(hasItem(DEFAULT_EXTENSION_ID.intValue())));
    }

    @Test
    @Transactional
    void getExtensionParameters() throws Exception {
        // Initialize the database
        extensionParametersRepository.saveAndFlush(extensionParameters);

        // Get the extensionParameters
        restExtensionParametersMockMvc
            .perform(get(ENTITY_API_URL_ID, extensionParameters.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(extensionParameters.getId().intValue()))
            .andExpect(jsonPath("$.parameterId").value(DEFAULT_PARAMETER_ID.intValue()))
            .andExpect(jsonPath("$.extensionId").value(DEFAULT_EXTENSION_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingExtensionParameters() throws Exception {
        // Get the extensionParameters
        restExtensionParametersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExtensionParameters() throws Exception {
        // Initialize the database
        extensionParametersRepository.saveAndFlush(extensionParameters);

        int databaseSizeBeforeUpdate = extensionParametersRepository.findAll().size();

        // Update the extensionParameters
        ExtensionParameters updatedExtensionParameters = extensionParametersRepository.findById(extensionParameters.getId()).get();
        // Disconnect from session so that the updates on updatedExtensionParameters are not directly saved in db
        em.detach(updatedExtensionParameters);
        updatedExtensionParameters.parameterId(UPDATED_PARAMETER_ID).extensionId(UPDATED_EXTENSION_ID);

        restExtensionParametersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExtensionParameters.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExtensionParameters))
            )
            .andExpect(status().isOk());

        // Validate the ExtensionParameters in the database
        List<ExtensionParameters> extensionParametersList = extensionParametersRepository.findAll();
        assertThat(extensionParametersList).hasSize(databaseSizeBeforeUpdate);
        ExtensionParameters testExtensionParameters = extensionParametersList.get(extensionParametersList.size() - 1);
        assertThat(testExtensionParameters.getParameterId()).isEqualTo(UPDATED_PARAMETER_ID);
        assertThat(testExtensionParameters.getExtensionId()).isEqualTo(UPDATED_EXTENSION_ID);
    }

    @Test
    @Transactional
    void putNonExistingExtensionParameters() throws Exception {
        int databaseSizeBeforeUpdate = extensionParametersRepository.findAll().size();
        extensionParameters.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtensionParametersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, extensionParameters.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(extensionParameters))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtensionParameters in the database
        List<ExtensionParameters> extensionParametersList = extensionParametersRepository.findAll();
        assertThat(extensionParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExtensionParameters() throws Exception {
        int databaseSizeBeforeUpdate = extensionParametersRepository.findAll().size();
        extensionParameters.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtensionParametersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(extensionParameters))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtensionParameters in the database
        List<ExtensionParameters> extensionParametersList = extensionParametersRepository.findAll();
        assertThat(extensionParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExtensionParameters() throws Exception {
        int databaseSizeBeforeUpdate = extensionParametersRepository.findAll().size();
        extensionParameters.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtensionParametersMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(extensionParameters))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExtensionParameters in the database
        List<ExtensionParameters> extensionParametersList = extensionParametersRepository.findAll();
        assertThat(extensionParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExtensionParametersWithPatch() throws Exception {
        // Initialize the database
        extensionParametersRepository.saveAndFlush(extensionParameters);

        int databaseSizeBeforeUpdate = extensionParametersRepository.findAll().size();

        // Update the extensionParameters using partial update
        ExtensionParameters partialUpdatedExtensionParameters = new ExtensionParameters();
        partialUpdatedExtensionParameters.setId(extensionParameters.getId());

        partialUpdatedExtensionParameters.parameterId(UPDATED_PARAMETER_ID).extensionId(UPDATED_EXTENSION_ID);

        restExtensionParametersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExtensionParameters.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExtensionParameters))
            )
            .andExpect(status().isOk());

        // Validate the ExtensionParameters in the database
        List<ExtensionParameters> extensionParametersList = extensionParametersRepository.findAll();
        assertThat(extensionParametersList).hasSize(databaseSizeBeforeUpdate);
        ExtensionParameters testExtensionParameters = extensionParametersList.get(extensionParametersList.size() - 1);
        assertThat(testExtensionParameters.getParameterId()).isEqualTo(UPDATED_PARAMETER_ID);
        assertThat(testExtensionParameters.getExtensionId()).isEqualTo(UPDATED_EXTENSION_ID);
    }

    @Test
    @Transactional
    void fullUpdateExtensionParametersWithPatch() throws Exception {
        // Initialize the database
        extensionParametersRepository.saveAndFlush(extensionParameters);

        int databaseSizeBeforeUpdate = extensionParametersRepository.findAll().size();

        // Update the extensionParameters using partial update
        ExtensionParameters partialUpdatedExtensionParameters = new ExtensionParameters();
        partialUpdatedExtensionParameters.setId(extensionParameters.getId());

        partialUpdatedExtensionParameters.parameterId(UPDATED_PARAMETER_ID).extensionId(UPDATED_EXTENSION_ID);

        restExtensionParametersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExtensionParameters.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExtensionParameters))
            )
            .andExpect(status().isOk());

        // Validate the ExtensionParameters in the database
        List<ExtensionParameters> extensionParametersList = extensionParametersRepository.findAll();
        assertThat(extensionParametersList).hasSize(databaseSizeBeforeUpdate);
        ExtensionParameters testExtensionParameters = extensionParametersList.get(extensionParametersList.size() - 1);
        assertThat(testExtensionParameters.getParameterId()).isEqualTo(UPDATED_PARAMETER_ID);
        assertThat(testExtensionParameters.getExtensionId()).isEqualTo(UPDATED_EXTENSION_ID);
    }

    @Test
    @Transactional
    void patchNonExistingExtensionParameters() throws Exception {
        int databaseSizeBeforeUpdate = extensionParametersRepository.findAll().size();
        extensionParameters.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtensionParametersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, extensionParameters.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(extensionParameters))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtensionParameters in the database
        List<ExtensionParameters> extensionParametersList = extensionParametersRepository.findAll();
        assertThat(extensionParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExtensionParameters() throws Exception {
        int databaseSizeBeforeUpdate = extensionParametersRepository.findAll().size();
        extensionParameters.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtensionParametersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(extensionParameters))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtensionParameters in the database
        List<ExtensionParameters> extensionParametersList = extensionParametersRepository.findAll();
        assertThat(extensionParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExtensionParameters() throws Exception {
        int databaseSizeBeforeUpdate = extensionParametersRepository.findAll().size();
        extensionParameters.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtensionParametersMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(extensionParameters))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExtensionParameters in the database
        List<ExtensionParameters> extensionParametersList = extensionParametersRepository.findAll();
        assertThat(extensionParametersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExtensionParameters() throws Exception {
        // Initialize the database
        extensionParametersRepository.saveAndFlush(extensionParameters);

        int databaseSizeBeforeDelete = extensionParametersRepository.findAll().size();

        // Delete the extensionParameters
        restExtensionParametersMockMvc
            .perform(delete(ENTITY_API_URL_ID, extensionParameters.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExtensionParameters> extensionParametersList = extensionParametersRepository.findAll();
        assertThat(extensionParametersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
