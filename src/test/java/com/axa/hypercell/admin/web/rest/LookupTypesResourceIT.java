package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.LookupTypes;
import com.axa.hypercell.admin.domain.enumeration.LookupType;
import com.axa.hypercell.admin.domain.enumeration.LookupType;
import com.axa.hypercell.admin.repository.LookupTypesRepository;
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
 * Integration tests for the {@link LookupTypesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LookupTypesResourceIT {

    private static final LookupType DEFAULT_NAME = LookupType.NaceCode;
    private static final LookupType UPDATED_NAME = LookupType.Cresta;

    private static final LookupType DEFAULT_CHILD_NAME = LookupType.NaceCode;
    private static final LookupType UPDATED_CHILD_NAME = LookupType.Cresta;

    private static final String ENTITY_API_URL = "/api/lookup-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LookupTypesRepository lookupTypesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLookupTypesMockMvc;

    private LookupTypes lookupTypes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LookupTypes createEntity(EntityManager em) {
        LookupTypes lookupTypes = new LookupTypes().name(DEFAULT_NAME).childName(DEFAULT_CHILD_NAME);
        return lookupTypes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LookupTypes createUpdatedEntity(EntityManager em) {
        LookupTypes lookupTypes = new LookupTypes().name(UPDATED_NAME).childName(UPDATED_CHILD_NAME);
        return lookupTypes;
    }

    @BeforeEach
    public void initTest() {
        lookupTypes = createEntity(em);
    }

    @Test
    @Transactional
    void createLookupTypes() throws Exception {
        int databaseSizeBeforeCreate = lookupTypesRepository.findAll().size();
        // Create the LookupTypes
        restLookupTypesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lookupTypes)))
            .andExpect(status().isCreated());

        // Validate the LookupTypes in the database
        List<LookupTypes> lookupTypesList = lookupTypesRepository.findAll();
        assertThat(lookupTypesList).hasSize(databaseSizeBeforeCreate + 1);
        LookupTypes testLookupTypes = lookupTypesList.get(lookupTypesList.size() - 1);
        assertThat(testLookupTypes.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLookupTypes.getChildName()).isEqualTo(DEFAULT_CHILD_NAME);
    }

    @Test
    @Transactional
    void createLookupTypesWithExistingId() throws Exception {
        // Create the LookupTypes with an existing ID
        lookupTypes.setId(1L);

        int databaseSizeBeforeCreate = lookupTypesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLookupTypesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lookupTypes)))
            .andExpect(status().isBadRequest());

        // Validate the LookupTypes in the database
        List<LookupTypes> lookupTypesList = lookupTypesRepository.findAll();
        assertThat(lookupTypesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLookupTypes() throws Exception {
        // Initialize the database
        lookupTypesRepository.saveAndFlush(lookupTypes);

        // Get all the lookupTypesList
        restLookupTypesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lookupTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].childName").value(hasItem(DEFAULT_CHILD_NAME.toString())));
    }

    @Test
    @Transactional
    void getLookupTypes() throws Exception {
        // Initialize the database
        lookupTypesRepository.saveAndFlush(lookupTypes);

        // Get the lookupTypes
        restLookupTypesMockMvc
            .perform(get(ENTITY_API_URL_ID, lookupTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lookupTypes.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.childName").value(DEFAULT_CHILD_NAME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLookupTypes() throws Exception {
        // Get the lookupTypes
        restLookupTypesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLookupTypes() throws Exception {
        // Initialize the database
        lookupTypesRepository.saveAndFlush(lookupTypes);

        int databaseSizeBeforeUpdate = lookupTypesRepository.findAll().size();

        // Update the lookupTypes
        LookupTypes updatedLookupTypes = lookupTypesRepository.findById(lookupTypes.getId()).get();
        // Disconnect from session so that the updates on updatedLookupTypes are not directly saved in db
        em.detach(updatedLookupTypes);
        updatedLookupTypes.name(UPDATED_NAME).childName(UPDATED_CHILD_NAME);

        restLookupTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLookupTypes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLookupTypes))
            )
            .andExpect(status().isOk());

        // Validate the LookupTypes in the database
        List<LookupTypes> lookupTypesList = lookupTypesRepository.findAll();
        assertThat(lookupTypesList).hasSize(databaseSizeBeforeUpdate);
        LookupTypes testLookupTypes = lookupTypesList.get(lookupTypesList.size() - 1);
        assertThat(testLookupTypes.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLookupTypes.getChildName()).isEqualTo(UPDATED_CHILD_NAME);
    }

    @Test
    @Transactional
    void putNonExistingLookupTypes() throws Exception {
        int databaseSizeBeforeUpdate = lookupTypesRepository.findAll().size();
        lookupTypes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLookupTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lookupTypes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lookupTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the LookupTypes in the database
        List<LookupTypes> lookupTypesList = lookupTypesRepository.findAll();
        assertThat(lookupTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLookupTypes() throws Exception {
        int databaseSizeBeforeUpdate = lookupTypesRepository.findAll().size();
        lookupTypes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLookupTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lookupTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the LookupTypes in the database
        List<LookupTypes> lookupTypesList = lookupTypesRepository.findAll();
        assertThat(lookupTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLookupTypes() throws Exception {
        int databaseSizeBeforeUpdate = lookupTypesRepository.findAll().size();
        lookupTypes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLookupTypesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lookupTypes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LookupTypes in the database
        List<LookupTypes> lookupTypesList = lookupTypesRepository.findAll();
        assertThat(lookupTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLookupTypesWithPatch() throws Exception {
        // Initialize the database
        lookupTypesRepository.saveAndFlush(lookupTypes);

        int databaseSizeBeforeUpdate = lookupTypesRepository.findAll().size();

        // Update the lookupTypes using partial update
        LookupTypes partialUpdatedLookupTypes = new LookupTypes();
        partialUpdatedLookupTypes.setId(lookupTypes.getId());

        partialUpdatedLookupTypes.name(UPDATED_NAME);

        restLookupTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLookupTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLookupTypes))
            )
            .andExpect(status().isOk());

        // Validate the LookupTypes in the database
        List<LookupTypes> lookupTypesList = lookupTypesRepository.findAll();
        assertThat(lookupTypesList).hasSize(databaseSizeBeforeUpdate);
        LookupTypes testLookupTypes = lookupTypesList.get(lookupTypesList.size() - 1);
        assertThat(testLookupTypes.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLookupTypes.getChildName()).isEqualTo(DEFAULT_CHILD_NAME);
    }

    @Test
    @Transactional
    void fullUpdateLookupTypesWithPatch() throws Exception {
        // Initialize the database
        lookupTypesRepository.saveAndFlush(lookupTypes);

        int databaseSizeBeforeUpdate = lookupTypesRepository.findAll().size();

        // Update the lookupTypes using partial update
        LookupTypes partialUpdatedLookupTypes = new LookupTypes();
        partialUpdatedLookupTypes.setId(lookupTypes.getId());

        partialUpdatedLookupTypes.name(UPDATED_NAME).childName(UPDATED_CHILD_NAME);

        restLookupTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLookupTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLookupTypes))
            )
            .andExpect(status().isOk());

        // Validate the LookupTypes in the database
        List<LookupTypes> lookupTypesList = lookupTypesRepository.findAll();
        assertThat(lookupTypesList).hasSize(databaseSizeBeforeUpdate);
        LookupTypes testLookupTypes = lookupTypesList.get(lookupTypesList.size() - 1);
        assertThat(testLookupTypes.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLookupTypes.getChildName()).isEqualTo(UPDATED_CHILD_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingLookupTypes() throws Exception {
        int databaseSizeBeforeUpdate = lookupTypesRepository.findAll().size();
        lookupTypes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLookupTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lookupTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lookupTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the LookupTypes in the database
        List<LookupTypes> lookupTypesList = lookupTypesRepository.findAll();
        assertThat(lookupTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLookupTypes() throws Exception {
        int databaseSizeBeforeUpdate = lookupTypesRepository.findAll().size();
        lookupTypes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLookupTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lookupTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the LookupTypes in the database
        List<LookupTypes> lookupTypesList = lookupTypesRepository.findAll();
        assertThat(lookupTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLookupTypes() throws Exception {
        int databaseSizeBeforeUpdate = lookupTypesRepository.findAll().size();
        lookupTypes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLookupTypesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(lookupTypes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LookupTypes in the database
        List<LookupTypes> lookupTypesList = lookupTypesRepository.findAll();
        assertThat(lookupTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLookupTypes() throws Exception {
        // Initialize the database
        lookupTypesRepository.saveAndFlush(lookupTypes);

        int databaseSizeBeforeDelete = lookupTypesRepository.findAll().size();

        // Delete the lookupTypes
        restLookupTypesMockMvc
            .perform(delete(ENTITY_API_URL_ID, lookupTypes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LookupTypes> lookupTypesList = lookupTypesRepository.findAll();
        assertThat(lookupTypesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
