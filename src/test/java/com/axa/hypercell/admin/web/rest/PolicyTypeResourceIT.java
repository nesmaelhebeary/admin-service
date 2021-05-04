package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.PolicyType;
import com.axa.hypercell.admin.repository.PolicyTypeRepository;
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
 * Integration tests for the {@link PolicyTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PolicyTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/policy-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PolicyTypeRepository policyTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPolicyTypeMockMvc;

    private PolicyType policyType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PolicyType createEntity(EntityManager em) {
        PolicyType policyType = new PolicyType().name(DEFAULT_NAME);
        return policyType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PolicyType createUpdatedEntity(EntityManager em) {
        PolicyType policyType = new PolicyType().name(UPDATED_NAME);
        return policyType;
    }

    @BeforeEach
    public void initTest() {
        policyType = createEntity(em);
    }

    @Test
    @Transactional
    void createPolicyType() throws Exception {
        int databaseSizeBeforeCreate = policyTypeRepository.findAll().size();
        // Create the PolicyType
        restPolicyTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(policyType)))
            .andExpect(status().isCreated());

        // Validate the PolicyType in the database
        List<PolicyType> policyTypeList = policyTypeRepository.findAll();
        assertThat(policyTypeList).hasSize(databaseSizeBeforeCreate + 1);
        PolicyType testPolicyType = policyTypeList.get(policyTypeList.size() - 1);
        assertThat(testPolicyType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createPolicyTypeWithExistingId() throws Exception {
        // Create the PolicyType with an existing ID
        policyType.setId(1L);

        int databaseSizeBeforeCreate = policyTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPolicyTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(policyType)))
            .andExpect(status().isBadRequest());

        // Validate the PolicyType in the database
        List<PolicyType> policyTypeList = policyTypeRepository.findAll();
        assertThat(policyTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPolicyTypes() throws Exception {
        // Initialize the database
        policyTypeRepository.saveAndFlush(policyType);

        // Get all the policyTypeList
        restPolicyTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(policyType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getPolicyType() throws Exception {
        // Initialize the database
        policyTypeRepository.saveAndFlush(policyType);

        // Get the policyType
        restPolicyTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, policyType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(policyType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingPolicyType() throws Exception {
        // Get the policyType
        restPolicyTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPolicyType() throws Exception {
        // Initialize the database
        policyTypeRepository.saveAndFlush(policyType);

        int databaseSizeBeforeUpdate = policyTypeRepository.findAll().size();

        // Update the policyType
        PolicyType updatedPolicyType = policyTypeRepository.findById(policyType.getId()).get();
        // Disconnect from session so that the updates on updatedPolicyType are not directly saved in db
        em.detach(updatedPolicyType);
        updatedPolicyType.name(UPDATED_NAME);

        restPolicyTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPolicyType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPolicyType))
            )
            .andExpect(status().isOk());

        // Validate the PolicyType in the database
        List<PolicyType> policyTypeList = policyTypeRepository.findAll();
        assertThat(policyTypeList).hasSize(databaseSizeBeforeUpdate);
        PolicyType testPolicyType = policyTypeList.get(policyTypeList.size() - 1);
        assertThat(testPolicyType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingPolicyType() throws Exception {
        int databaseSizeBeforeUpdate = policyTypeRepository.findAll().size();
        policyType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPolicyTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, policyType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(policyType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PolicyType in the database
        List<PolicyType> policyTypeList = policyTypeRepository.findAll();
        assertThat(policyTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPolicyType() throws Exception {
        int databaseSizeBeforeUpdate = policyTypeRepository.findAll().size();
        policyType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPolicyTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(policyType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PolicyType in the database
        List<PolicyType> policyTypeList = policyTypeRepository.findAll();
        assertThat(policyTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPolicyType() throws Exception {
        int databaseSizeBeforeUpdate = policyTypeRepository.findAll().size();
        policyType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPolicyTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(policyType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PolicyType in the database
        List<PolicyType> policyTypeList = policyTypeRepository.findAll();
        assertThat(policyTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePolicyTypeWithPatch() throws Exception {
        // Initialize the database
        policyTypeRepository.saveAndFlush(policyType);

        int databaseSizeBeforeUpdate = policyTypeRepository.findAll().size();

        // Update the policyType using partial update
        PolicyType partialUpdatedPolicyType = new PolicyType();
        partialUpdatedPolicyType.setId(policyType.getId());

        restPolicyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPolicyType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPolicyType))
            )
            .andExpect(status().isOk());

        // Validate the PolicyType in the database
        List<PolicyType> policyTypeList = policyTypeRepository.findAll();
        assertThat(policyTypeList).hasSize(databaseSizeBeforeUpdate);
        PolicyType testPolicyType = policyTypeList.get(policyTypeList.size() - 1);
        assertThat(testPolicyType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdatePolicyTypeWithPatch() throws Exception {
        // Initialize the database
        policyTypeRepository.saveAndFlush(policyType);

        int databaseSizeBeforeUpdate = policyTypeRepository.findAll().size();

        // Update the policyType using partial update
        PolicyType partialUpdatedPolicyType = new PolicyType();
        partialUpdatedPolicyType.setId(policyType.getId());

        partialUpdatedPolicyType.name(UPDATED_NAME);

        restPolicyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPolicyType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPolicyType))
            )
            .andExpect(status().isOk());

        // Validate the PolicyType in the database
        List<PolicyType> policyTypeList = policyTypeRepository.findAll();
        assertThat(policyTypeList).hasSize(databaseSizeBeforeUpdate);
        PolicyType testPolicyType = policyTypeList.get(policyTypeList.size() - 1);
        assertThat(testPolicyType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingPolicyType() throws Exception {
        int databaseSizeBeforeUpdate = policyTypeRepository.findAll().size();
        policyType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPolicyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, policyType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(policyType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PolicyType in the database
        List<PolicyType> policyTypeList = policyTypeRepository.findAll();
        assertThat(policyTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPolicyType() throws Exception {
        int databaseSizeBeforeUpdate = policyTypeRepository.findAll().size();
        policyType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPolicyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(policyType))
            )
            .andExpect(status().isBadRequest());

        // Validate the PolicyType in the database
        List<PolicyType> policyTypeList = policyTypeRepository.findAll();
        assertThat(policyTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPolicyType() throws Exception {
        int databaseSizeBeforeUpdate = policyTypeRepository.findAll().size();
        policyType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPolicyTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(policyType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PolicyType in the database
        List<PolicyType> policyTypeList = policyTypeRepository.findAll();
        assertThat(policyTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePolicyType() throws Exception {
        // Initialize the database
        policyTypeRepository.saveAndFlush(policyType);

        int databaseSizeBeforeDelete = policyTypeRepository.findAll().size();

        // Delete the policyType
        restPolicyTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, policyType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PolicyType> policyTypeList = policyTypeRepository.findAll();
        assertThat(policyTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
