package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.NaceCode;
import com.axa.hypercell.admin.repository.NaceCodeRepository;
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
 * Integration tests for the {@link NaceCodeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NaceCodeResourceIT {

    private static final String DEFAULT_ACTIVITY = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVITY = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_GLOBAL_CLASS = "AAAAAAAAAA";
    private static final String UPDATED_GLOBAL_CLASS = "BBBBBBBBBB";

    private static final Integer DEFAULT_AUTO_FAC_CAPACITY = 1;
    private static final Integer UPDATED_AUTO_FAC_CAPACITY = 2;

    private static final String DEFAULT_LOCAL_CLASS = "AAAAAAAAAA";
    private static final String UPDATED_LOCAL_CLASS = "BBBBBBBBBB";

    private static final Float DEFAULT_BO_RATE = 1F;
    private static final Float UPDATED_BO_RATE = 2F;

    private static final Float DEFAULT_PD_RATE = 1F;
    private static final Float UPDATED_PD_RATE = 2F;

    private static final String ENTITY_API_URL = "/api/nace-codes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NaceCodeRepository naceCodeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNaceCodeMockMvc;

    private NaceCode naceCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NaceCode createEntity(EntityManager em) {
        NaceCode naceCode = new NaceCode()
            .activity(DEFAULT_ACTIVITY)
            .code(DEFAULT_CODE)
            .globalClass(DEFAULT_GLOBAL_CLASS)
            .autoFacCapacity(DEFAULT_AUTO_FAC_CAPACITY)
            .localClass(DEFAULT_LOCAL_CLASS)
            .boRate(DEFAULT_BO_RATE)
            .pdRate(DEFAULT_PD_RATE);
        return naceCode;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NaceCode createUpdatedEntity(EntityManager em) {
        NaceCode naceCode = new NaceCode()
            .activity(UPDATED_ACTIVITY)
            .code(UPDATED_CODE)
            .globalClass(UPDATED_GLOBAL_CLASS)
            .autoFacCapacity(UPDATED_AUTO_FAC_CAPACITY)
            .localClass(UPDATED_LOCAL_CLASS)
            .boRate(UPDATED_BO_RATE)
            .pdRate(UPDATED_PD_RATE);
        return naceCode;
    }

    @BeforeEach
    public void initTest() {
        naceCode = createEntity(em);
    }

    @Test
    @Transactional
    void createNaceCode() throws Exception {
        int databaseSizeBeforeCreate = naceCodeRepository.findAll().size();
        // Create the NaceCode
        restNaceCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(naceCode)))
            .andExpect(status().isCreated());

        // Validate the NaceCode in the database
        List<NaceCode> naceCodeList = naceCodeRepository.findAll();
        assertThat(naceCodeList).hasSize(databaseSizeBeforeCreate + 1);
        NaceCode testNaceCode = naceCodeList.get(naceCodeList.size() - 1);
        assertThat(testNaceCode.getActivity()).isEqualTo(DEFAULT_ACTIVITY);
        assertThat(testNaceCode.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testNaceCode.getGlobalClass()).isEqualTo(DEFAULT_GLOBAL_CLASS);
        assertThat(testNaceCode.getAutoFacCapacity()).isEqualTo(DEFAULT_AUTO_FAC_CAPACITY);
        assertThat(testNaceCode.getLocalClass()).isEqualTo(DEFAULT_LOCAL_CLASS);
        assertThat(testNaceCode.getBoRate()).isEqualTo(DEFAULT_BO_RATE);
        assertThat(testNaceCode.getPdRate()).isEqualTo(DEFAULT_PD_RATE);
    }

    @Test
    @Transactional
    void createNaceCodeWithExistingId() throws Exception {
        // Create the NaceCode with an existing ID
        naceCode.setId(1L);

        int databaseSizeBeforeCreate = naceCodeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNaceCodeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(naceCode)))
            .andExpect(status().isBadRequest());

        // Validate the NaceCode in the database
        List<NaceCode> naceCodeList = naceCodeRepository.findAll();
        assertThat(naceCodeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllNaceCodes() throws Exception {
        // Initialize the database
        naceCodeRepository.saveAndFlush(naceCode);

        // Get all the naceCodeList
        restNaceCodeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(naceCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].activity").value(hasItem(DEFAULT_ACTIVITY)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].globalClass").value(hasItem(DEFAULT_GLOBAL_CLASS)))
            .andExpect(jsonPath("$.[*].autoFacCapacity").value(hasItem(DEFAULT_AUTO_FAC_CAPACITY)))
            .andExpect(jsonPath("$.[*].localClass").value(hasItem(DEFAULT_LOCAL_CLASS)))
            .andExpect(jsonPath("$.[*].boRate").value(hasItem(DEFAULT_BO_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].pdRate").value(hasItem(DEFAULT_PD_RATE.doubleValue())));
    }

    @Test
    @Transactional
    void getNaceCode() throws Exception {
        // Initialize the database
        naceCodeRepository.saveAndFlush(naceCode);

        // Get the naceCode
        restNaceCodeMockMvc
            .perform(get(ENTITY_API_URL_ID, naceCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(naceCode.getId().intValue()))
            .andExpect(jsonPath("$.activity").value(DEFAULT_ACTIVITY))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.globalClass").value(DEFAULT_GLOBAL_CLASS))
            .andExpect(jsonPath("$.autoFacCapacity").value(DEFAULT_AUTO_FAC_CAPACITY))
            .andExpect(jsonPath("$.localClass").value(DEFAULT_LOCAL_CLASS))
            .andExpect(jsonPath("$.boRate").value(DEFAULT_BO_RATE.doubleValue()))
            .andExpect(jsonPath("$.pdRate").value(DEFAULT_PD_RATE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingNaceCode() throws Exception {
        // Get the naceCode
        restNaceCodeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNaceCode() throws Exception {
        // Initialize the database
        naceCodeRepository.saveAndFlush(naceCode);

        int databaseSizeBeforeUpdate = naceCodeRepository.findAll().size();

        // Update the naceCode
        NaceCode updatedNaceCode = naceCodeRepository.findById(naceCode.getId()).get();
        // Disconnect from session so that the updates on updatedNaceCode are not directly saved in db
        em.detach(updatedNaceCode);
        updatedNaceCode
            .activity(UPDATED_ACTIVITY)
            .code(UPDATED_CODE)
            .globalClass(UPDATED_GLOBAL_CLASS)
            .autoFacCapacity(UPDATED_AUTO_FAC_CAPACITY)
            .localClass(UPDATED_LOCAL_CLASS)
            .boRate(UPDATED_BO_RATE)
            .pdRate(UPDATED_PD_RATE);

        restNaceCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNaceCode.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedNaceCode))
            )
            .andExpect(status().isOk());

        // Validate the NaceCode in the database
        List<NaceCode> naceCodeList = naceCodeRepository.findAll();
        assertThat(naceCodeList).hasSize(databaseSizeBeforeUpdate);
        NaceCode testNaceCode = naceCodeList.get(naceCodeList.size() - 1);
        assertThat(testNaceCode.getActivity()).isEqualTo(UPDATED_ACTIVITY);
        assertThat(testNaceCode.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testNaceCode.getGlobalClass()).isEqualTo(UPDATED_GLOBAL_CLASS);
        assertThat(testNaceCode.getAutoFacCapacity()).isEqualTo(UPDATED_AUTO_FAC_CAPACITY);
        assertThat(testNaceCode.getLocalClass()).isEqualTo(UPDATED_LOCAL_CLASS);
        assertThat(testNaceCode.getBoRate()).isEqualTo(UPDATED_BO_RATE);
        assertThat(testNaceCode.getPdRate()).isEqualTo(UPDATED_PD_RATE);
    }

    @Test
    @Transactional
    void putNonExistingNaceCode() throws Exception {
        int databaseSizeBeforeUpdate = naceCodeRepository.findAll().size();
        naceCode.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNaceCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, naceCode.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(naceCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the NaceCode in the database
        List<NaceCode> naceCodeList = naceCodeRepository.findAll();
        assertThat(naceCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNaceCode() throws Exception {
        int databaseSizeBeforeUpdate = naceCodeRepository.findAll().size();
        naceCode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNaceCodeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(naceCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the NaceCode in the database
        List<NaceCode> naceCodeList = naceCodeRepository.findAll();
        assertThat(naceCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNaceCode() throws Exception {
        int databaseSizeBeforeUpdate = naceCodeRepository.findAll().size();
        naceCode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNaceCodeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(naceCode)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NaceCode in the database
        List<NaceCode> naceCodeList = naceCodeRepository.findAll();
        assertThat(naceCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNaceCodeWithPatch() throws Exception {
        // Initialize the database
        naceCodeRepository.saveAndFlush(naceCode);

        int databaseSizeBeforeUpdate = naceCodeRepository.findAll().size();

        // Update the naceCode using partial update
        NaceCode partialUpdatedNaceCode = new NaceCode();
        partialUpdatedNaceCode.setId(naceCode.getId());

        partialUpdatedNaceCode
            .activity(UPDATED_ACTIVITY)
            .code(UPDATED_CODE)
            .autoFacCapacity(UPDATED_AUTO_FAC_CAPACITY)
            .pdRate(UPDATED_PD_RATE);

        restNaceCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNaceCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNaceCode))
            )
            .andExpect(status().isOk());

        // Validate the NaceCode in the database
        List<NaceCode> naceCodeList = naceCodeRepository.findAll();
        assertThat(naceCodeList).hasSize(databaseSizeBeforeUpdate);
        NaceCode testNaceCode = naceCodeList.get(naceCodeList.size() - 1);
        assertThat(testNaceCode.getActivity()).isEqualTo(UPDATED_ACTIVITY);
        assertThat(testNaceCode.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testNaceCode.getGlobalClass()).isEqualTo(DEFAULT_GLOBAL_CLASS);
        assertThat(testNaceCode.getAutoFacCapacity()).isEqualTo(UPDATED_AUTO_FAC_CAPACITY);
        assertThat(testNaceCode.getLocalClass()).isEqualTo(DEFAULT_LOCAL_CLASS);
        assertThat(testNaceCode.getBoRate()).isEqualTo(DEFAULT_BO_RATE);
        assertThat(testNaceCode.getPdRate()).isEqualTo(UPDATED_PD_RATE);
    }

    @Test
    @Transactional
    void fullUpdateNaceCodeWithPatch() throws Exception {
        // Initialize the database
        naceCodeRepository.saveAndFlush(naceCode);

        int databaseSizeBeforeUpdate = naceCodeRepository.findAll().size();

        // Update the naceCode using partial update
        NaceCode partialUpdatedNaceCode = new NaceCode();
        partialUpdatedNaceCode.setId(naceCode.getId());

        partialUpdatedNaceCode
            .activity(UPDATED_ACTIVITY)
            .code(UPDATED_CODE)
            .globalClass(UPDATED_GLOBAL_CLASS)
            .autoFacCapacity(UPDATED_AUTO_FAC_CAPACITY)
            .localClass(UPDATED_LOCAL_CLASS)
            .boRate(UPDATED_BO_RATE)
            .pdRate(UPDATED_PD_RATE);

        restNaceCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNaceCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNaceCode))
            )
            .andExpect(status().isOk());

        // Validate the NaceCode in the database
        List<NaceCode> naceCodeList = naceCodeRepository.findAll();
        assertThat(naceCodeList).hasSize(databaseSizeBeforeUpdate);
        NaceCode testNaceCode = naceCodeList.get(naceCodeList.size() - 1);
        assertThat(testNaceCode.getActivity()).isEqualTo(UPDATED_ACTIVITY);
        assertThat(testNaceCode.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testNaceCode.getGlobalClass()).isEqualTo(UPDATED_GLOBAL_CLASS);
        assertThat(testNaceCode.getAutoFacCapacity()).isEqualTo(UPDATED_AUTO_FAC_CAPACITY);
        assertThat(testNaceCode.getLocalClass()).isEqualTo(UPDATED_LOCAL_CLASS);
        assertThat(testNaceCode.getBoRate()).isEqualTo(UPDATED_BO_RATE);
        assertThat(testNaceCode.getPdRate()).isEqualTo(UPDATED_PD_RATE);
    }

    @Test
    @Transactional
    void patchNonExistingNaceCode() throws Exception {
        int databaseSizeBeforeUpdate = naceCodeRepository.findAll().size();
        naceCode.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNaceCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, naceCode.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(naceCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the NaceCode in the database
        List<NaceCode> naceCodeList = naceCodeRepository.findAll();
        assertThat(naceCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNaceCode() throws Exception {
        int databaseSizeBeforeUpdate = naceCodeRepository.findAll().size();
        naceCode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNaceCodeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(naceCode))
            )
            .andExpect(status().isBadRequest());

        // Validate the NaceCode in the database
        List<NaceCode> naceCodeList = naceCodeRepository.findAll();
        assertThat(naceCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNaceCode() throws Exception {
        int databaseSizeBeforeUpdate = naceCodeRepository.findAll().size();
        naceCode.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNaceCodeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(naceCode)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NaceCode in the database
        List<NaceCode> naceCodeList = naceCodeRepository.findAll();
        assertThat(naceCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNaceCode() throws Exception {
        // Initialize the database
        naceCodeRepository.saveAndFlush(naceCode);

        int databaseSizeBeforeDelete = naceCodeRepository.findAll().size();

        // Delete the naceCode
        restNaceCodeMockMvc
            .perform(delete(ENTITY_API_URL_ID, naceCode.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NaceCode> naceCodeList = naceCodeRepository.findAll();
        assertThat(naceCodeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
