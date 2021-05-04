package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.Structure;
import com.axa.hypercell.admin.repository.StructureRepository;
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
 * Integration tests for the {@link StructureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StructureResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CAN_BUILDING_CLASS_ID = "AAAAAAAAAA";
    private static final String UPDATED_CAN_BUILDING_CLASS_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/structures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StructureRepository structureRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStructureMockMvc;

    private Structure structure;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Structure createEntity(EntityManager em) {
        Structure structure = new Structure()
            .name(DEFAULT_NAME)
            .shortName(DEFAULT_SHORT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .canBuildingClassId(DEFAULT_CAN_BUILDING_CLASS_ID);
        return structure;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Structure createUpdatedEntity(EntityManager em) {
        Structure structure = new Structure()
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .description(UPDATED_DESCRIPTION)
            .canBuildingClassId(UPDATED_CAN_BUILDING_CLASS_ID);
        return structure;
    }

    @BeforeEach
    public void initTest() {
        structure = createEntity(em);
    }

    @Test
    @Transactional
    void createStructure() throws Exception {
        int databaseSizeBeforeCreate = structureRepository.findAll().size();
        // Create the Structure
        restStructureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(structure)))
            .andExpect(status().isCreated());

        // Validate the Structure in the database
        List<Structure> structureList = structureRepository.findAll();
        assertThat(structureList).hasSize(databaseSizeBeforeCreate + 1);
        Structure testStructure = structureList.get(structureList.size() - 1);
        assertThat(testStructure.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStructure.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testStructure.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testStructure.getCanBuildingClassId()).isEqualTo(DEFAULT_CAN_BUILDING_CLASS_ID);
    }

    @Test
    @Transactional
    void createStructureWithExistingId() throws Exception {
        // Create the Structure with an existing ID
        structure.setId(1L);

        int databaseSizeBeforeCreate = structureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStructureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(structure)))
            .andExpect(status().isBadRequest());

        // Validate the Structure in the database
        List<Structure> structureList = structureRepository.findAll();
        assertThat(structureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStructures() throws Exception {
        // Initialize the database
        structureRepository.saveAndFlush(structure);

        // Get all the structureList
        restStructureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(structure.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].canBuildingClassId").value(hasItem(DEFAULT_CAN_BUILDING_CLASS_ID)));
    }

    @Test
    @Transactional
    void getStructure() throws Exception {
        // Initialize the database
        structureRepository.saveAndFlush(structure);

        // Get the structure
        restStructureMockMvc
            .perform(get(ENTITY_API_URL_ID, structure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(structure.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.canBuildingClassId").value(DEFAULT_CAN_BUILDING_CLASS_ID));
    }

    @Test
    @Transactional
    void getNonExistingStructure() throws Exception {
        // Get the structure
        restStructureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStructure() throws Exception {
        // Initialize the database
        structureRepository.saveAndFlush(structure);

        int databaseSizeBeforeUpdate = structureRepository.findAll().size();

        // Update the structure
        Structure updatedStructure = structureRepository.findById(structure.getId()).get();
        // Disconnect from session so that the updates on updatedStructure are not directly saved in db
        em.detach(updatedStructure);
        updatedStructure
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .description(UPDATED_DESCRIPTION)
            .canBuildingClassId(UPDATED_CAN_BUILDING_CLASS_ID);

        restStructureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStructure.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStructure))
            )
            .andExpect(status().isOk());

        // Validate the Structure in the database
        List<Structure> structureList = structureRepository.findAll();
        assertThat(structureList).hasSize(databaseSizeBeforeUpdate);
        Structure testStructure = structureList.get(structureList.size() - 1);
        assertThat(testStructure.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStructure.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testStructure.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStructure.getCanBuildingClassId()).isEqualTo(UPDATED_CAN_BUILDING_CLASS_ID);
    }

    @Test
    @Transactional
    void putNonExistingStructure() throws Exception {
        int databaseSizeBeforeUpdate = structureRepository.findAll().size();
        structure.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStructureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, structure.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(structure))
            )
            .andExpect(status().isBadRequest());

        // Validate the Structure in the database
        List<Structure> structureList = structureRepository.findAll();
        assertThat(structureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStructure() throws Exception {
        int databaseSizeBeforeUpdate = structureRepository.findAll().size();
        structure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStructureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(structure))
            )
            .andExpect(status().isBadRequest());

        // Validate the Structure in the database
        List<Structure> structureList = structureRepository.findAll();
        assertThat(structureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStructure() throws Exception {
        int databaseSizeBeforeUpdate = structureRepository.findAll().size();
        structure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStructureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(structure)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Structure in the database
        List<Structure> structureList = structureRepository.findAll();
        assertThat(structureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStructureWithPatch() throws Exception {
        // Initialize the database
        structureRepository.saveAndFlush(structure);

        int databaseSizeBeforeUpdate = structureRepository.findAll().size();

        // Update the structure using partial update
        Structure partialUpdatedStructure = new Structure();
        partialUpdatedStructure.setId(structure.getId());

        partialUpdatedStructure.description(UPDATED_DESCRIPTION).canBuildingClassId(UPDATED_CAN_BUILDING_CLASS_ID);

        restStructureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStructure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStructure))
            )
            .andExpect(status().isOk());

        // Validate the Structure in the database
        List<Structure> structureList = structureRepository.findAll();
        assertThat(structureList).hasSize(databaseSizeBeforeUpdate);
        Structure testStructure = structureList.get(structureList.size() - 1);
        assertThat(testStructure.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStructure.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testStructure.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStructure.getCanBuildingClassId()).isEqualTo(UPDATED_CAN_BUILDING_CLASS_ID);
    }

    @Test
    @Transactional
    void fullUpdateStructureWithPatch() throws Exception {
        // Initialize the database
        structureRepository.saveAndFlush(structure);

        int databaseSizeBeforeUpdate = structureRepository.findAll().size();

        // Update the structure using partial update
        Structure partialUpdatedStructure = new Structure();
        partialUpdatedStructure.setId(structure.getId());

        partialUpdatedStructure
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .description(UPDATED_DESCRIPTION)
            .canBuildingClassId(UPDATED_CAN_BUILDING_CLASS_ID);

        restStructureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStructure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStructure))
            )
            .andExpect(status().isOk());

        // Validate the Structure in the database
        List<Structure> structureList = structureRepository.findAll();
        assertThat(structureList).hasSize(databaseSizeBeforeUpdate);
        Structure testStructure = structureList.get(structureList.size() - 1);
        assertThat(testStructure.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStructure.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testStructure.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStructure.getCanBuildingClassId()).isEqualTo(UPDATED_CAN_BUILDING_CLASS_ID);
    }

    @Test
    @Transactional
    void patchNonExistingStructure() throws Exception {
        int databaseSizeBeforeUpdate = structureRepository.findAll().size();
        structure.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStructureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, structure.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(structure))
            )
            .andExpect(status().isBadRequest());

        // Validate the Structure in the database
        List<Structure> structureList = structureRepository.findAll();
        assertThat(structureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStructure() throws Exception {
        int databaseSizeBeforeUpdate = structureRepository.findAll().size();
        structure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStructureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(structure))
            )
            .andExpect(status().isBadRequest());

        // Validate the Structure in the database
        List<Structure> structureList = structureRepository.findAll();
        assertThat(structureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStructure() throws Exception {
        int databaseSizeBeforeUpdate = structureRepository.findAll().size();
        structure.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStructureMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(structure))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Structure in the database
        List<Structure> structureList = structureRepository.findAll();
        assertThat(structureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStructure() throws Exception {
        // Initialize the database
        structureRepository.saveAndFlush(structure);

        int databaseSizeBeforeDelete = structureRepository.findAll().size();

        // Delete the structure
        restStructureMockMvc
            .perform(delete(ENTITY_API_URL_ID, structure.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Structure> structureList = structureRepository.findAll();
        assertThat(structureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
