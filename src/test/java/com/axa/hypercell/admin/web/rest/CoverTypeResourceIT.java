package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.CoverType;
import com.axa.hypercell.admin.repository.CoverTypeRepository;
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
 * Integration tests for the {@link CoverTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CoverTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cover-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CoverTypeRepository coverTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCoverTypeMockMvc;

    private CoverType coverType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CoverType createEntity(EntityManager em) {
        CoverType coverType = new CoverType().name(DEFAULT_NAME);
        return coverType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CoverType createUpdatedEntity(EntityManager em) {
        CoverType coverType = new CoverType().name(UPDATED_NAME);
        return coverType;
    }

    @BeforeEach
    public void initTest() {
        coverType = createEntity(em);
    }

    @Test
    @Transactional
    void createCoverType() throws Exception {
        int databaseSizeBeforeCreate = coverTypeRepository.findAll().size();
        // Create the CoverType
        restCoverTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coverType)))
            .andExpect(status().isCreated());

        // Validate the CoverType in the database
        List<CoverType> coverTypeList = coverTypeRepository.findAll();
        assertThat(coverTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CoverType testCoverType = coverTypeList.get(coverTypeList.size() - 1);
        assertThat(testCoverType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createCoverTypeWithExistingId() throws Exception {
        // Create the CoverType with an existing ID
        coverType.setId(1L);

        int databaseSizeBeforeCreate = coverTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoverTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coverType)))
            .andExpect(status().isBadRequest());

        // Validate the CoverType in the database
        List<CoverType> coverTypeList = coverTypeRepository.findAll();
        assertThat(coverTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCoverTypes() throws Exception {
        // Initialize the database
        coverTypeRepository.saveAndFlush(coverType);

        // Get all the coverTypeList
        restCoverTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coverType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getCoverType() throws Exception {
        // Initialize the database
        coverTypeRepository.saveAndFlush(coverType);

        // Get the coverType
        restCoverTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, coverType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(coverType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCoverType() throws Exception {
        // Get the coverType
        restCoverTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCoverType() throws Exception {
        // Initialize the database
        coverTypeRepository.saveAndFlush(coverType);

        int databaseSizeBeforeUpdate = coverTypeRepository.findAll().size();

        // Update the coverType
        CoverType updatedCoverType = coverTypeRepository.findById(coverType.getId()).get();
        // Disconnect from session so that the updates on updatedCoverType are not directly saved in db
        em.detach(updatedCoverType);
        updatedCoverType.name(UPDATED_NAME);

        restCoverTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCoverType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCoverType))
            )
            .andExpect(status().isOk());

        // Validate the CoverType in the database
        List<CoverType> coverTypeList = coverTypeRepository.findAll();
        assertThat(coverTypeList).hasSize(databaseSizeBeforeUpdate);
        CoverType testCoverType = coverTypeList.get(coverTypeList.size() - 1);
        assertThat(testCoverType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCoverType() throws Exception {
        int databaseSizeBeforeUpdate = coverTypeRepository.findAll().size();
        coverType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoverTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coverType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coverType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoverType in the database
        List<CoverType> coverTypeList = coverTypeRepository.findAll();
        assertThat(coverTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCoverType() throws Exception {
        int databaseSizeBeforeUpdate = coverTypeRepository.findAll().size();
        coverType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoverTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coverType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoverType in the database
        List<CoverType> coverTypeList = coverTypeRepository.findAll();
        assertThat(coverTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCoverType() throws Exception {
        int databaseSizeBeforeUpdate = coverTypeRepository.findAll().size();
        coverType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoverTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coverType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CoverType in the database
        List<CoverType> coverTypeList = coverTypeRepository.findAll();
        assertThat(coverTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCoverTypeWithPatch() throws Exception {
        // Initialize the database
        coverTypeRepository.saveAndFlush(coverType);

        int databaseSizeBeforeUpdate = coverTypeRepository.findAll().size();

        // Update the coverType using partial update
        CoverType partialUpdatedCoverType = new CoverType();
        partialUpdatedCoverType.setId(coverType.getId());

        partialUpdatedCoverType.name(UPDATED_NAME);

        restCoverTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoverType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCoverType))
            )
            .andExpect(status().isOk());

        // Validate the CoverType in the database
        List<CoverType> coverTypeList = coverTypeRepository.findAll();
        assertThat(coverTypeList).hasSize(databaseSizeBeforeUpdate);
        CoverType testCoverType = coverTypeList.get(coverTypeList.size() - 1);
        assertThat(testCoverType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCoverTypeWithPatch() throws Exception {
        // Initialize the database
        coverTypeRepository.saveAndFlush(coverType);

        int databaseSizeBeforeUpdate = coverTypeRepository.findAll().size();

        // Update the coverType using partial update
        CoverType partialUpdatedCoverType = new CoverType();
        partialUpdatedCoverType.setId(coverType.getId());

        partialUpdatedCoverType.name(UPDATED_NAME);

        restCoverTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoverType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCoverType))
            )
            .andExpect(status().isOk());

        // Validate the CoverType in the database
        List<CoverType> coverTypeList = coverTypeRepository.findAll();
        assertThat(coverTypeList).hasSize(databaseSizeBeforeUpdate);
        CoverType testCoverType = coverTypeList.get(coverTypeList.size() - 1);
        assertThat(testCoverType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCoverType() throws Exception {
        int databaseSizeBeforeUpdate = coverTypeRepository.findAll().size();
        coverType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoverTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, coverType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coverType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoverType in the database
        List<CoverType> coverTypeList = coverTypeRepository.findAll();
        assertThat(coverTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCoverType() throws Exception {
        int databaseSizeBeforeUpdate = coverTypeRepository.findAll().size();
        coverType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoverTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coverType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoverType in the database
        List<CoverType> coverTypeList = coverTypeRepository.findAll();
        assertThat(coverTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCoverType() throws Exception {
        int databaseSizeBeforeUpdate = coverTypeRepository.findAll().size();
        coverType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoverTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(coverType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CoverType in the database
        List<CoverType> coverTypeList = coverTypeRepository.findAll();
        assertThat(coverTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCoverType() throws Exception {
        // Initialize the database
        coverTypeRepository.saveAndFlush(coverType);

        int databaseSizeBeforeDelete = coverTypeRepository.findAll().size();

        // Delete the coverType
        restCoverTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, coverType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CoverType> coverTypeList = coverTypeRepository.findAll();
        assertThat(coverTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
