package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.Packing;
import com.axa.hypercell.admin.repository.PackingRepository;
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
 * Integration tests for the {@link PackingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PackingResourceIT {

    private static final String DEFAULT_NAME_ENGLISH = "AAAAAAAAAA";
    private static final String UPDATED_NAME_ENGLISH = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_ARABIC = "AAAAAAAAAA";
    private static final String UPDATED_NAME_ARABIC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/packings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PackingRepository packingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPackingMockMvc;

    private Packing packing;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Packing createEntity(EntityManager em) {
        Packing packing = new Packing().nameEnglish(DEFAULT_NAME_ENGLISH).nameArabic(DEFAULT_NAME_ARABIC);
        return packing;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Packing createUpdatedEntity(EntityManager em) {
        Packing packing = new Packing().nameEnglish(UPDATED_NAME_ENGLISH).nameArabic(UPDATED_NAME_ARABIC);
        return packing;
    }

    @BeforeEach
    public void initTest() {
        packing = createEntity(em);
    }

    @Test
    @Transactional
    void createPacking() throws Exception {
        int databaseSizeBeforeCreate = packingRepository.findAll().size();
        // Create the Packing
        restPackingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(packing)))
            .andExpect(status().isCreated());

        // Validate the Packing in the database
        List<Packing> packingList = packingRepository.findAll();
        assertThat(packingList).hasSize(databaseSizeBeforeCreate + 1);
        Packing testPacking = packingList.get(packingList.size() - 1);
        assertThat(testPacking.getNameEnglish()).isEqualTo(DEFAULT_NAME_ENGLISH);
        assertThat(testPacking.getNameArabic()).isEqualTo(DEFAULT_NAME_ARABIC);
    }

    @Test
    @Transactional
    void createPackingWithExistingId() throws Exception {
        // Create the Packing with an existing ID
        packing.setId(1L);

        int databaseSizeBeforeCreate = packingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPackingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(packing)))
            .andExpect(status().isBadRequest());

        // Validate the Packing in the database
        List<Packing> packingList = packingRepository.findAll();
        assertThat(packingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPackings() throws Exception {
        // Initialize the database
        packingRepository.saveAndFlush(packing);

        // Get all the packingList
        restPackingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(packing.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameEnglish").value(hasItem(DEFAULT_NAME_ENGLISH)))
            .andExpect(jsonPath("$.[*].nameArabic").value(hasItem(DEFAULT_NAME_ARABIC)));
    }

    @Test
    @Transactional
    void getPacking() throws Exception {
        // Initialize the database
        packingRepository.saveAndFlush(packing);

        // Get the packing
        restPackingMockMvc
            .perform(get(ENTITY_API_URL_ID, packing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(packing.getId().intValue()))
            .andExpect(jsonPath("$.nameEnglish").value(DEFAULT_NAME_ENGLISH))
            .andExpect(jsonPath("$.nameArabic").value(DEFAULT_NAME_ARABIC));
    }

    @Test
    @Transactional
    void getNonExistingPacking() throws Exception {
        // Get the packing
        restPackingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPacking() throws Exception {
        // Initialize the database
        packingRepository.saveAndFlush(packing);

        int databaseSizeBeforeUpdate = packingRepository.findAll().size();

        // Update the packing
        Packing updatedPacking = packingRepository.findById(packing.getId()).get();
        // Disconnect from session so that the updates on updatedPacking are not directly saved in db
        em.detach(updatedPacking);
        updatedPacking.nameEnglish(UPDATED_NAME_ENGLISH).nameArabic(UPDATED_NAME_ARABIC);

        restPackingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPacking.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPacking))
            )
            .andExpect(status().isOk());

        // Validate the Packing in the database
        List<Packing> packingList = packingRepository.findAll();
        assertThat(packingList).hasSize(databaseSizeBeforeUpdate);
        Packing testPacking = packingList.get(packingList.size() - 1);
        assertThat(testPacking.getNameEnglish()).isEqualTo(UPDATED_NAME_ENGLISH);
        assertThat(testPacking.getNameArabic()).isEqualTo(UPDATED_NAME_ARABIC);
    }

    @Test
    @Transactional
    void putNonExistingPacking() throws Exception {
        int databaseSizeBeforeUpdate = packingRepository.findAll().size();
        packing.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPackingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, packing.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(packing))
            )
            .andExpect(status().isBadRequest());

        // Validate the Packing in the database
        List<Packing> packingList = packingRepository.findAll();
        assertThat(packingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPacking() throws Exception {
        int databaseSizeBeforeUpdate = packingRepository.findAll().size();
        packing.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(packing))
            )
            .andExpect(status().isBadRequest());

        // Validate the Packing in the database
        List<Packing> packingList = packingRepository.findAll();
        assertThat(packingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPacking() throws Exception {
        int databaseSizeBeforeUpdate = packingRepository.findAll().size();
        packing.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(packing)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Packing in the database
        List<Packing> packingList = packingRepository.findAll();
        assertThat(packingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePackingWithPatch() throws Exception {
        // Initialize the database
        packingRepository.saveAndFlush(packing);

        int databaseSizeBeforeUpdate = packingRepository.findAll().size();

        // Update the packing using partial update
        Packing partialUpdatedPacking = new Packing();
        partialUpdatedPacking.setId(packing.getId());

        partialUpdatedPacking.nameArabic(UPDATED_NAME_ARABIC);

        restPackingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPacking.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPacking))
            )
            .andExpect(status().isOk());

        // Validate the Packing in the database
        List<Packing> packingList = packingRepository.findAll();
        assertThat(packingList).hasSize(databaseSizeBeforeUpdate);
        Packing testPacking = packingList.get(packingList.size() - 1);
        assertThat(testPacking.getNameEnglish()).isEqualTo(DEFAULT_NAME_ENGLISH);
        assertThat(testPacking.getNameArabic()).isEqualTo(UPDATED_NAME_ARABIC);
    }

    @Test
    @Transactional
    void fullUpdatePackingWithPatch() throws Exception {
        // Initialize the database
        packingRepository.saveAndFlush(packing);

        int databaseSizeBeforeUpdate = packingRepository.findAll().size();

        // Update the packing using partial update
        Packing partialUpdatedPacking = new Packing();
        partialUpdatedPacking.setId(packing.getId());

        partialUpdatedPacking.nameEnglish(UPDATED_NAME_ENGLISH).nameArabic(UPDATED_NAME_ARABIC);

        restPackingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPacking.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPacking))
            )
            .andExpect(status().isOk());

        // Validate the Packing in the database
        List<Packing> packingList = packingRepository.findAll();
        assertThat(packingList).hasSize(databaseSizeBeforeUpdate);
        Packing testPacking = packingList.get(packingList.size() - 1);
        assertThat(testPacking.getNameEnglish()).isEqualTo(UPDATED_NAME_ENGLISH);
        assertThat(testPacking.getNameArabic()).isEqualTo(UPDATED_NAME_ARABIC);
    }

    @Test
    @Transactional
    void patchNonExistingPacking() throws Exception {
        int databaseSizeBeforeUpdate = packingRepository.findAll().size();
        packing.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPackingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, packing.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(packing))
            )
            .andExpect(status().isBadRequest());

        // Validate the Packing in the database
        List<Packing> packingList = packingRepository.findAll();
        assertThat(packingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPacking() throws Exception {
        int databaseSizeBeforeUpdate = packingRepository.findAll().size();
        packing.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(packing))
            )
            .andExpect(status().isBadRequest());

        // Validate the Packing in the database
        List<Packing> packingList = packingRepository.findAll();
        assertThat(packingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPacking() throws Exception {
        int databaseSizeBeforeUpdate = packingRepository.findAll().size();
        packing.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(packing)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Packing in the database
        List<Packing> packingList = packingRepository.findAll();
        assertThat(packingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePacking() throws Exception {
        // Initialize the database
        packingRepository.saveAndFlush(packing);

        int databaseSizeBeforeDelete = packingRepository.findAll().size();

        // Delete the packing
        restPackingMockMvc
            .perform(delete(ENTITY_API_URL_ID, packing.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Packing> packingList = packingRepository.findAll();
        assertThat(packingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
