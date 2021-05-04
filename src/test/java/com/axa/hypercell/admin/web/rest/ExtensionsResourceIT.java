package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.Extensions;
import com.axa.hypercell.admin.domain.enumeration.Status;
import com.axa.hypercell.admin.repository.ExtensionsRepository;
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
 * Integration tests for the {@link ExtensionsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExtensionsResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT_EN = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_EN = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT_AR = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_AR = "BBBBBBBBBB";

    private static final String DEFAULT_AFFECT_MPL = "AAAAAAAAAA";
    private static final String UPDATED_AFFECT_MPL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_EFFECTIVE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EFFECTIVE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Status DEFAULT_STATUS = Status.Active;
    private static final Status UPDATED_STATUS = Status.InActive;

    private static final String ENTITY_API_URL = "/api/extensions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExtensionsRepository extensionsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExtensionsMockMvc;

    private Extensions extensions;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Extensions createEntity(EntityManager em) {
        Extensions extensions = new Extensions()
            .code(DEFAULT_CODE)
            .textEn(DEFAULT_TEXT_EN)
            .textAr(DEFAULT_TEXT_AR)
            .affectMpl(DEFAULT_AFFECT_MPL)
            .effectiveDate(DEFAULT_EFFECTIVE_DATE)
            .status(DEFAULT_STATUS);
        return extensions;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Extensions createUpdatedEntity(EntityManager em) {
        Extensions extensions = new Extensions()
            .code(UPDATED_CODE)
            .textEn(UPDATED_TEXT_EN)
            .textAr(UPDATED_TEXT_AR)
            .affectMpl(UPDATED_AFFECT_MPL)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .status(UPDATED_STATUS);
        return extensions;
    }

    @BeforeEach
    public void initTest() {
        extensions = createEntity(em);
    }

    @Test
    @Transactional
    void createExtensions() throws Exception {
        int databaseSizeBeforeCreate = extensionsRepository.findAll().size();
        // Create the Extensions
        restExtensionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(extensions)))
            .andExpect(status().isCreated());

        // Validate the Extensions in the database
        List<Extensions> extensionsList = extensionsRepository.findAll();
        assertThat(extensionsList).hasSize(databaseSizeBeforeCreate + 1);
        Extensions testExtensions = extensionsList.get(extensionsList.size() - 1);
        assertThat(testExtensions.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testExtensions.getTextEn()).isEqualTo(DEFAULT_TEXT_EN);
        assertThat(testExtensions.getTextAr()).isEqualTo(DEFAULT_TEXT_AR);
        assertThat(testExtensions.getAffectMpl()).isEqualTo(DEFAULT_AFFECT_MPL);
        assertThat(testExtensions.getEffectiveDate()).isEqualTo(DEFAULT_EFFECTIVE_DATE);
        assertThat(testExtensions.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createExtensionsWithExistingId() throws Exception {
        // Create the Extensions with an existing ID
        extensions.setId(1L);

        int databaseSizeBeforeCreate = extensionsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtensionsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(extensions)))
            .andExpect(status().isBadRequest());

        // Validate the Extensions in the database
        List<Extensions> extensionsList = extensionsRepository.findAll();
        assertThat(extensionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExtensions() throws Exception {
        // Initialize the database
        extensionsRepository.saveAndFlush(extensions);

        // Get all the extensionsList
        restExtensionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extensions.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].textEn").value(hasItem(DEFAULT_TEXT_EN)))
            .andExpect(jsonPath("$.[*].textAr").value(hasItem(DEFAULT_TEXT_AR)))
            .andExpect(jsonPath("$.[*].affectMpl").value(hasItem(DEFAULT_AFFECT_MPL)))
            .andExpect(jsonPath("$.[*].effectiveDate").value(hasItem(DEFAULT_EFFECTIVE_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getExtensions() throws Exception {
        // Initialize the database
        extensionsRepository.saveAndFlush(extensions);

        // Get the extensions
        restExtensionsMockMvc
            .perform(get(ENTITY_API_URL_ID, extensions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(extensions.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.textEn").value(DEFAULT_TEXT_EN))
            .andExpect(jsonPath("$.textAr").value(DEFAULT_TEXT_AR))
            .andExpect(jsonPath("$.affectMpl").value(DEFAULT_AFFECT_MPL))
            .andExpect(jsonPath("$.effectiveDate").value(DEFAULT_EFFECTIVE_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingExtensions() throws Exception {
        // Get the extensions
        restExtensionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewExtensions() throws Exception {
        // Initialize the database
        extensionsRepository.saveAndFlush(extensions);

        int databaseSizeBeforeUpdate = extensionsRepository.findAll().size();

        // Update the extensions
        Extensions updatedExtensions = extensionsRepository.findById(extensions.getId()).get();
        // Disconnect from session so that the updates on updatedExtensions are not directly saved in db
        em.detach(updatedExtensions);
        updatedExtensions
            .code(UPDATED_CODE)
            .textEn(UPDATED_TEXT_EN)
            .textAr(UPDATED_TEXT_AR)
            .affectMpl(UPDATED_AFFECT_MPL)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .status(UPDATED_STATUS);

        restExtensionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExtensions.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExtensions))
            )
            .andExpect(status().isOk());

        // Validate the Extensions in the database
        List<Extensions> extensionsList = extensionsRepository.findAll();
        assertThat(extensionsList).hasSize(databaseSizeBeforeUpdate);
        Extensions testExtensions = extensionsList.get(extensionsList.size() - 1);
        assertThat(testExtensions.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testExtensions.getTextEn()).isEqualTo(UPDATED_TEXT_EN);
        assertThat(testExtensions.getTextAr()).isEqualTo(UPDATED_TEXT_AR);
        assertThat(testExtensions.getAffectMpl()).isEqualTo(UPDATED_AFFECT_MPL);
        assertThat(testExtensions.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testExtensions.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingExtensions() throws Exception {
        int databaseSizeBeforeUpdate = extensionsRepository.findAll().size();
        extensions.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtensionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, extensions.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(extensions))
            )
            .andExpect(status().isBadRequest());

        // Validate the Extensions in the database
        List<Extensions> extensionsList = extensionsRepository.findAll();
        assertThat(extensionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExtensions() throws Exception {
        int databaseSizeBeforeUpdate = extensionsRepository.findAll().size();
        extensions.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtensionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(extensions))
            )
            .andExpect(status().isBadRequest());

        // Validate the Extensions in the database
        List<Extensions> extensionsList = extensionsRepository.findAll();
        assertThat(extensionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExtensions() throws Exception {
        int databaseSizeBeforeUpdate = extensionsRepository.findAll().size();
        extensions.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtensionsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(extensions)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Extensions in the database
        List<Extensions> extensionsList = extensionsRepository.findAll();
        assertThat(extensionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExtensionsWithPatch() throws Exception {
        // Initialize the database
        extensionsRepository.saveAndFlush(extensions);

        int databaseSizeBeforeUpdate = extensionsRepository.findAll().size();

        // Update the extensions using partial update
        Extensions partialUpdatedExtensions = new Extensions();
        partialUpdatedExtensions.setId(extensions.getId());

        partialUpdatedExtensions.textEn(UPDATED_TEXT_EN).affectMpl(UPDATED_AFFECT_MPL);

        restExtensionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExtensions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExtensions))
            )
            .andExpect(status().isOk());

        // Validate the Extensions in the database
        List<Extensions> extensionsList = extensionsRepository.findAll();
        assertThat(extensionsList).hasSize(databaseSizeBeforeUpdate);
        Extensions testExtensions = extensionsList.get(extensionsList.size() - 1);
        assertThat(testExtensions.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testExtensions.getTextEn()).isEqualTo(UPDATED_TEXT_EN);
        assertThat(testExtensions.getTextAr()).isEqualTo(DEFAULT_TEXT_AR);
        assertThat(testExtensions.getAffectMpl()).isEqualTo(UPDATED_AFFECT_MPL);
        assertThat(testExtensions.getEffectiveDate()).isEqualTo(DEFAULT_EFFECTIVE_DATE);
        assertThat(testExtensions.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateExtensionsWithPatch() throws Exception {
        // Initialize the database
        extensionsRepository.saveAndFlush(extensions);

        int databaseSizeBeforeUpdate = extensionsRepository.findAll().size();

        // Update the extensions using partial update
        Extensions partialUpdatedExtensions = new Extensions();
        partialUpdatedExtensions.setId(extensions.getId());

        partialUpdatedExtensions
            .code(UPDATED_CODE)
            .textEn(UPDATED_TEXT_EN)
            .textAr(UPDATED_TEXT_AR)
            .affectMpl(UPDATED_AFFECT_MPL)
            .effectiveDate(UPDATED_EFFECTIVE_DATE)
            .status(UPDATED_STATUS);

        restExtensionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExtensions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExtensions))
            )
            .andExpect(status().isOk());

        // Validate the Extensions in the database
        List<Extensions> extensionsList = extensionsRepository.findAll();
        assertThat(extensionsList).hasSize(databaseSizeBeforeUpdate);
        Extensions testExtensions = extensionsList.get(extensionsList.size() - 1);
        assertThat(testExtensions.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testExtensions.getTextEn()).isEqualTo(UPDATED_TEXT_EN);
        assertThat(testExtensions.getTextAr()).isEqualTo(UPDATED_TEXT_AR);
        assertThat(testExtensions.getAffectMpl()).isEqualTo(UPDATED_AFFECT_MPL);
        assertThat(testExtensions.getEffectiveDate()).isEqualTo(UPDATED_EFFECTIVE_DATE);
        assertThat(testExtensions.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingExtensions() throws Exception {
        int databaseSizeBeforeUpdate = extensionsRepository.findAll().size();
        extensions.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtensionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, extensions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(extensions))
            )
            .andExpect(status().isBadRequest());

        // Validate the Extensions in the database
        List<Extensions> extensionsList = extensionsRepository.findAll();
        assertThat(extensionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExtensions() throws Exception {
        int databaseSizeBeforeUpdate = extensionsRepository.findAll().size();
        extensions.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtensionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(extensions))
            )
            .andExpect(status().isBadRequest());

        // Validate the Extensions in the database
        List<Extensions> extensionsList = extensionsRepository.findAll();
        assertThat(extensionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExtensions() throws Exception {
        int databaseSizeBeforeUpdate = extensionsRepository.findAll().size();
        extensions.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtensionsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(extensions))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Extensions in the database
        List<Extensions> extensionsList = extensionsRepository.findAll();
        assertThat(extensionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExtensions() throws Exception {
        // Initialize the database
        extensionsRepository.saveAndFlush(extensions);

        int databaseSizeBeforeDelete = extensionsRepository.findAll().size();

        // Delete the extensions
        restExtensionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, extensions.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Extensions> extensionsList = extensionsRepository.findAll();
        assertThat(extensionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
