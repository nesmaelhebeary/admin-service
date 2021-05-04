package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.FireLineSettings;
import com.axa.hypercell.admin.repository.FireLineSettingsRepository;
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
 * Integration tests for the {@link FireLineSettingsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FireLineSettingsResourceIT {

    private static final String DEFAULT_CLASS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_FROM_VALUE = 1L;
    private static final Long UPDATED_FROM_VALUE = 2L;

    private static final Long DEFAULT_TO_VALUE = 1L;
    private static final Long UPDATED_TO_VALUE = 2L;

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final Long DEFAULT_COVERAGE_PERCENTAGE = 1L;
    private static final Long UPDATED_COVERAGE_PERCENTAGE = 2L;

    private static final String ENTITY_API_URL = "/api/fire-line-settings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FireLineSettingsRepository fireLineSettingsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFireLineSettingsMockMvc;

    private FireLineSettings fireLineSettings;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FireLineSettings createEntity(EntityManager em) {
        FireLineSettings fireLineSettings = new FireLineSettings()
            .className(DEFAULT_CLASS_NAME)
            .fromValue(DEFAULT_FROM_VALUE)
            .toValue(DEFAULT_TO_VALUE)
            .currency(DEFAULT_CURRENCY)
            .coveragePercentage(DEFAULT_COVERAGE_PERCENTAGE);
        return fireLineSettings;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FireLineSettings createUpdatedEntity(EntityManager em) {
        FireLineSettings fireLineSettings = new FireLineSettings()
            .className(UPDATED_CLASS_NAME)
            .fromValue(UPDATED_FROM_VALUE)
            .toValue(UPDATED_TO_VALUE)
            .currency(UPDATED_CURRENCY)
            .coveragePercentage(UPDATED_COVERAGE_PERCENTAGE);
        return fireLineSettings;
    }

    @BeforeEach
    public void initTest() {
        fireLineSettings = createEntity(em);
    }

    @Test
    @Transactional
    void createFireLineSettings() throws Exception {
        int databaseSizeBeforeCreate = fireLineSettingsRepository.findAll().size();
        // Create the FireLineSettings
        restFireLineSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fireLineSettings))
            )
            .andExpect(status().isCreated());

        // Validate the FireLineSettings in the database
        List<FireLineSettings> fireLineSettingsList = fireLineSettingsRepository.findAll();
        assertThat(fireLineSettingsList).hasSize(databaseSizeBeforeCreate + 1);
        FireLineSettings testFireLineSettings = fireLineSettingsList.get(fireLineSettingsList.size() - 1);
        assertThat(testFireLineSettings.getClassName()).isEqualTo(DEFAULT_CLASS_NAME);
        assertThat(testFireLineSettings.getFromValue()).isEqualTo(DEFAULT_FROM_VALUE);
        assertThat(testFireLineSettings.getToValue()).isEqualTo(DEFAULT_TO_VALUE);
        assertThat(testFireLineSettings.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testFireLineSettings.getCoveragePercentage()).isEqualTo(DEFAULT_COVERAGE_PERCENTAGE);
    }

    @Test
    @Transactional
    void createFireLineSettingsWithExistingId() throws Exception {
        // Create the FireLineSettings with an existing ID
        fireLineSettings.setId(1L);

        int databaseSizeBeforeCreate = fireLineSettingsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFireLineSettingsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fireLineSettings))
            )
            .andExpect(status().isBadRequest());

        // Validate the FireLineSettings in the database
        List<FireLineSettings> fireLineSettingsList = fireLineSettingsRepository.findAll();
        assertThat(fireLineSettingsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFireLineSettings() throws Exception {
        // Initialize the database
        fireLineSettingsRepository.saveAndFlush(fireLineSettings);

        // Get all the fireLineSettingsList
        restFireLineSettingsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fireLineSettings.getId().intValue())))
            .andExpect(jsonPath("$.[*].className").value(hasItem(DEFAULT_CLASS_NAME)))
            .andExpect(jsonPath("$.[*].fromValue").value(hasItem(DEFAULT_FROM_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].toValue").value(hasItem(DEFAULT_TO_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].coveragePercentage").value(hasItem(DEFAULT_COVERAGE_PERCENTAGE.intValue())));
    }

    @Test
    @Transactional
    void getFireLineSettings() throws Exception {
        // Initialize the database
        fireLineSettingsRepository.saveAndFlush(fireLineSettings);

        // Get the fireLineSettings
        restFireLineSettingsMockMvc
            .perform(get(ENTITY_API_URL_ID, fireLineSettings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fireLineSettings.getId().intValue()))
            .andExpect(jsonPath("$.className").value(DEFAULT_CLASS_NAME))
            .andExpect(jsonPath("$.fromValue").value(DEFAULT_FROM_VALUE.intValue()))
            .andExpect(jsonPath("$.toValue").value(DEFAULT_TO_VALUE.intValue()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.coveragePercentage").value(DEFAULT_COVERAGE_PERCENTAGE.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingFireLineSettings() throws Exception {
        // Get the fireLineSettings
        restFireLineSettingsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFireLineSettings() throws Exception {
        // Initialize the database
        fireLineSettingsRepository.saveAndFlush(fireLineSettings);

        int databaseSizeBeforeUpdate = fireLineSettingsRepository.findAll().size();

        // Update the fireLineSettings
        FireLineSettings updatedFireLineSettings = fireLineSettingsRepository.findById(fireLineSettings.getId()).get();
        // Disconnect from session so that the updates on updatedFireLineSettings are not directly saved in db
        em.detach(updatedFireLineSettings);
        updatedFireLineSettings
            .className(UPDATED_CLASS_NAME)
            .fromValue(UPDATED_FROM_VALUE)
            .toValue(UPDATED_TO_VALUE)
            .currency(UPDATED_CURRENCY)
            .coveragePercentage(UPDATED_COVERAGE_PERCENTAGE);

        restFireLineSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFireLineSettings.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFireLineSettings))
            )
            .andExpect(status().isOk());

        // Validate the FireLineSettings in the database
        List<FireLineSettings> fireLineSettingsList = fireLineSettingsRepository.findAll();
        assertThat(fireLineSettingsList).hasSize(databaseSizeBeforeUpdate);
        FireLineSettings testFireLineSettings = fireLineSettingsList.get(fireLineSettingsList.size() - 1);
        assertThat(testFireLineSettings.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
        assertThat(testFireLineSettings.getFromValue()).isEqualTo(UPDATED_FROM_VALUE);
        assertThat(testFireLineSettings.getToValue()).isEqualTo(UPDATED_TO_VALUE);
        assertThat(testFireLineSettings.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testFireLineSettings.getCoveragePercentage()).isEqualTo(UPDATED_COVERAGE_PERCENTAGE);
    }

    @Test
    @Transactional
    void putNonExistingFireLineSettings() throws Exception {
        int databaseSizeBeforeUpdate = fireLineSettingsRepository.findAll().size();
        fireLineSettings.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFireLineSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fireLineSettings.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fireLineSettings))
            )
            .andExpect(status().isBadRequest());

        // Validate the FireLineSettings in the database
        List<FireLineSettings> fireLineSettingsList = fireLineSettingsRepository.findAll();
        assertThat(fireLineSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFireLineSettings() throws Exception {
        int databaseSizeBeforeUpdate = fireLineSettingsRepository.findAll().size();
        fireLineSettings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFireLineSettingsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fireLineSettings))
            )
            .andExpect(status().isBadRequest());

        // Validate the FireLineSettings in the database
        List<FireLineSettings> fireLineSettingsList = fireLineSettingsRepository.findAll();
        assertThat(fireLineSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFireLineSettings() throws Exception {
        int databaseSizeBeforeUpdate = fireLineSettingsRepository.findAll().size();
        fireLineSettings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFireLineSettingsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fireLineSettings))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FireLineSettings in the database
        List<FireLineSettings> fireLineSettingsList = fireLineSettingsRepository.findAll();
        assertThat(fireLineSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFireLineSettingsWithPatch() throws Exception {
        // Initialize the database
        fireLineSettingsRepository.saveAndFlush(fireLineSettings);

        int databaseSizeBeforeUpdate = fireLineSettingsRepository.findAll().size();

        // Update the fireLineSettings using partial update
        FireLineSettings partialUpdatedFireLineSettings = new FireLineSettings();
        partialUpdatedFireLineSettings.setId(fireLineSettings.getId());

        partialUpdatedFireLineSettings.className(UPDATED_CLASS_NAME).fromValue(UPDATED_FROM_VALUE).toValue(UPDATED_TO_VALUE);

        restFireLineSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFireLineSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFireLineSettings))
            )
            .andExpect(status().isOk());

        // Validate the FireLineSettings in the database
        List<FireLineSettings> fireLineSettingsList = fireLineSettingsRepository.findAll();
        assertThat(fireLineSettingsList).hasSize(databaseSizeBeforeUpdate);
        FireLineSettings testFireLineSettings = fireLineSettingsList.get(fireLineSettingsList.size() - 1);
        assertThat(testFireLineSettings.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
        assertThat(testFireLineSettings.getFromValue()).isEqualTo(UPDATED_FROM_VALUE);
        assertThat(testFireLineSettings.getToValue()).isEqualTo(UPDATED_TO_VALUE);
        assertThat(testFireLineSettings.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testFireLineSettings.getCoveragePercentage()).isEqualTo(DEFAULT_COVERAGE_PERCENTAGE);
    }

    @Test
    @Transactional
    void fullUpdateFireLineSettingsWithPatch() throws Exception {
        // Initialize the database
        fireLineSettingsRepository.saveAndFlush(fireLineSettings);

        int databaseSizeBeforeUpdate = fireLineSettingsRepository.findAll().size();

        // Update the fireLineSettings using partial update
        FireLineSettings partialUpdatedFireLineSettings = new FireLineSettings();
        partialUpdatedFireLineSettings.setId(fireLineSettings.getId());

        partialUpdatedFireLineSettings
            .className(UPDATED_CLASS_NAME)
            .fromValue(UPDATED_FROM_VALUE)
            .toValue(UPDATED_TO_VALUE)
            .currency(UPDATED_CURRENCY)
            .coveragePercentage(UPDATED_COVERAGE_PERCENTAGE);

        restFireLineSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFireLineSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFireLineSettings))
            )
            .andExpect(status().isOk());

        // Validate the FireLineSettings in the database
        List<FireLineSettings> fireLineSettingsList = fireLineSettingsRepository.findAll();
        assertThat(fireLineSettingsList).hasSize(databaseSizeBeforeUpdate);
        FireLineSettings testFireLineSettings = fireLineSettingsList.get(fireLineSettingsList.size() - 1);
        assertThat(testFireLineSettings.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
        assertThat(testFireLineSettings.getFromValue()).isEqualTo(UPDATED_FROM_VALUE);
        assertThat(testFireLineSettings.getToValue()).isEqualTo(UPDATED_TO_VALUE);
        assertThat(testFireLineSettings.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testFireLineSettings.getCoveragePercentage()).isEqualTo(UPDATED_COVERAGE_PERCENTAGE);
    }

    @Test
    @Transactional
    void patchNonExistingFireLineSettings() throws Exception {
        int databaseSizeBeforeUpdate = fireLineSettingsRepository.findAll().size();
        fireLineSettings.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFireLineSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fireLineSettings.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fireLineSettings))
            )
            .andExpect(status().isBadRequest());

        // Validate the FireLineSettings in the database
        List<FireLineSettings> fireLineSettingsList = fireLineSettingsRepository.findAll();
        assertThat(fireLineSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFireLineSettings() throws Exception {
        int databaseSizeBeforeUpdate = fireLineSettingsRepository.findAll().size();
        fireLineSettings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFireLineSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fireLineSettings))
            )
            .andExpect(status().isBadRequest());

        // Validate the FireLineSettings in the database
        List<FireLineSettings> fireLineSettingsList = fireLineSettingsRepository.findAll();
        assertThat(fireLineSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFireLineSettings() throws Exception {
        int databaseSizeBeforeUpdate = fireLineSettingsRepository.findAll().size();
        fireLineSettings.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFireLineSettingsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fireLineSettings))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FireLineSettings in the database
        List<FireLineSettings> fireLineSettingsList = fireLineSettingsRepository.findAll();
        assertThat(fireLineSettingsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFireLineSettings() throws Exception {
        // Initialize the database
        fireLineSettingsRepository.saveAndFlush(fireLineSettings);

        int databaseSizeBeforeDelete = fireLineSettingsRepository.findAll().size();

        // Delete the fireLineSettings
        restFireLineSettingsMockMvc
            .perform(delete(ENTITY_API_URL_ID, fireLineSettings.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FireLineSettings> fireLineSettingsList = fireLineSettingsRepository.findAll();
        assertThat(fireLineSettingsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
