package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.Roof;
import com.axa.hypercell.admin.repository.RoofRepository;
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
 * Integration tests for the {@link RoofResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoofResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/roofs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RoofRepository roofRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoofMockMvc;

    private Roof roof;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Roof createEntity(EntityManager em) {
        Roof roof = new Roof().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return roof;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Roof createUpdatedEntity(EntityManager em) {
        Roof roof = new Roof().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return roof;
    }

    @BeforeEach
    public void initTest() {
        roof = createEntity(em);
    }

    @Test
    @Transactional
    void createRoof() throws Exception {
        int databaseSizeBeforeCreate = roofRepository.findAll().size();
        // Create the Roof
        restRoofMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roof)))
            .andExpect(status().isCreated());

        // Validate the Roof in the database
        List<Roof> roofList = roofRepository.findAll();
        assertThat(roofList).hasSize(databaseSizeBeforeCreate + 1);
        Roof testRoof = roofList.get(roofList.size() - 1);
        assertThat(testRoof.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRoof.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createRoofWithExistingId() throws Exception {
        // Create the Roof with an existing ID
        roof.setId(1L);

        int databaseSizeBeforeCreate = roofRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoofMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roof)))
            .andExpect(status().isBadRequest());

        // Validate the Roof in the database
        List<Roof> roofList = roofRepository.findAll();
        assertThat(roofList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRoofs() throws Exception {
        // Initialize the database
        roofRepository.saveAndFlush(roof);

        // Get all the roofList
        restRoofMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roof.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getRoof() throws Exception {
        // Initialize the database
        roofRepository.saveAndFlush(roof);

        // Get the roof
        restRoofMockMvc
            .perform(get(ENTITY_API_URL_ID, roof.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roof.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingRoof() throws Exception {
        // Get the roof
        restRoofMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRoof() throws Exception {
        // Initialize the database
        roofRepository.saveAndFlush(roof);

        int databaseSizeBeforeUpdate = roofRepository.findAll().size();

        // Update the roof
        Roof updatedRoof = roofRepository.findById(roof.getId()).get();
        // Disconnect from session so that the updates on updatedRoof are not directly saved in db
        em.detach(updatedRoof);
        updatedRoof.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restRoofMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRoof.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRoof))
            )
            .andExpect(status().isOk());

        // Validate the Roof in the database
        List<Roof> roofList = roofRepository.findAll();
        assertThat(roofList).hasSize(databaseSizeBeforeUpdate);
        Roof testRoof = roofList.get(roofList.size() - 1);
        assertThat(testRoof.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRoof.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingRoof() throws Exception {
        int databaseSizeBeforeUpdate = roofRepository.findAll().size();
        roof.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoofMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roof.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roof))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roof in the database
        List<Roof> roofList = roofRepository.findAll();
        assertThat(roofList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoof() throws Exception {
        int databaseSizeBeforeUpdate = roofRepository.findAll().size();
        roof.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoofMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roof))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roof in the database
        List<Roof> roofList = roofRepository.findAll();
        assertThat(roofList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoof() throws Exception {
        int databaseSizeBeforeUpdate = roofRepository.findAll().size();
        roof.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoofMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roof)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Roof in the database
        List<Roof> roofList = roofRepository.findAll();
        assertThat(roofList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoofWithPatch() throws Exception {
        // Initialize the database
        roofRepository.saveAndFlush(roof);

        int databaseSizeBeforeUpdate = roofRepository.findAll().size();

        // Update the roof using partial update
        Roof partialUpdatedRoof = new Roof();
        partialUpdatedRoof.setId(roof.getId());

        partialUpdatedRoof.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restRoofMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoof.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoof))
            )
            .andExpect(status().isOk());

        // Validate the Roof in the database
        List<Roof> roofList = roofRepository.findAll();
        assertThat(roofList).hasSize(databaseSizeBeforeUpdate);
        Roof testRoof = roofList.get(roofList.size() - 1);
        assertThat(testRoof.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRoof.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateRoofWithPatch() throws Exception {
        // Initialize the database
        roofRepository.saveAndFlush(roof);

        int databaseSizeBeforeUpdate = roofRepository.findAll().size();

        // Update the roof using partial update
        Roof partialUpdatedRoof = new Roof();
        partialUpdatedRoof.setId(roof.getId());

        partialUpdatedRoof.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restRoofMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoof.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoof))
            )
            .andExpect(status().isOk());

        // Validate the Roof in the database
        List<Roof> roofList = roofRepository.findAll();
        assertThat(roofList).hasSize(databaseSizeBeforeUpdate);
        Roof testRoof = roofList.get(roofList.size() - 1);
        assertThat(testRoof.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRoof.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingRoof() throws Exception {
        int databaseSizeBeforeUpdate = roofRepository.findAll().size();
        roof.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoofMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roof.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roof))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roof in the database
        List<Roof> roofList = roofRepository.findAll();
        assertThat(roofList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoof() throws Exception {
        int databaseSizeBeforeUpdate = roofRepository.findAll().size();
        roof.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoofMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roof))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roof in the database
        List<Roof> roofList = roofRepository.findAll();
        assertThat(roofList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoof() throws Exception {
        int databaseSizeBeforeUpdate = roofRepository.findAll().size();
        roof.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoofMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(roof)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Roof in the database
        List<Roof> roofList = roofRepository.findAll();
        assertThat(roofList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoof() throws Exception {
        // Initialize the database
        roofRepository.saveAndFlush(roof);

        int databaseSizeBeforeDelete = roofRepository.findAll().size();

        // Delete the roof
        restRoofMockMvc
            .perform(delete(ENTITY_API_URL_ID, roof.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Roof> roofList = roofRepository.findAll();
        assertThat(roofList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
