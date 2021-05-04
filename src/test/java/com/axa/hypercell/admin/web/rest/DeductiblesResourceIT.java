package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.Deductibles;
import com.axa.hypercell.admin.repository.DeductiblesRepository;
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
 * Integration tests for the {@link DeductiblesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeductiblesResourceIT {

    private static final Long DEFAULT_SECTION_ID = 1L;
    private static final Long UPDATED_SECTION_ID = 2L;

    private static final String DEFAULT_TEXT_AR = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_AR = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT_EN = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_EN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/deductibles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeductiblesRepository deductiblesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeductiblesMockMvc;

    private Deductibles deductibles;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deductibles createEntity(EntityManager em) {
        Deductibles deductibles = new Deductibles().sectionId(DEFAULT_SECTION_ID).textAr(DEFAULT_TEXT_AR).textEn(DEFAULT_TEXT_EN);
        return deductibles;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deductibles createUpdatedEntity(EntityManager em) {
        Deductibles deductibles = new Deductibles().sectionId(UPDATED_SECTION_ID).textAr(UPDATED_TEXT_AR).textEn(UPDATED_TEXT_EN);
        return deductibles;
    }

    @BeforeEach
    public void initTest() {
        deductibles = createEntity(em);
    }

    @Test
    @Transactional
    void createDeductibles() throws Exception {
        int databaseSizeBeforeCreate = deductiblesRepository.findAll().size();
        // Create the Deductibles
        restDeductiblesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deductibles)))
            .andExpect(status().isCreated());

        // Validate the Deductibles in the database
        List<Deductibles> deductiblesList = deductiblesRepository.findAll();
        assertThat(deductiblesList).hasSize(databaseSizeBeforeCreate + 1);
        Deductibles testDeductibles = deductiblesList.get(deductiblesList.size() - 1);
        assertThat(testDeductibles.getSectionId()).isEqualTo(DEFAULT_SECTION_ID);
        assertThat(testDeductibles.getTextAr()).isEqualTo(DEFAULT_TEXT_AR);
        assertThat(testDeductibles.getTextEn()).isEqualTo(DEFAULT_TEXT_EN);
    }

    @Test
    @Transactional
    void createDeductiblesWithExistingId() throws Exception {
        // Create the Deductibles with an existing ID
        deductibles.setId(1L);

        int databaseSizeBeforeCreate = deductiblesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeductiblesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deductibles)))
            .andExpect(status().isBadRequest());

        // Validate the Deductibles in the database
        List<Deductibles> deductiblesList = deductiblesRepository.findAll();
        assertThat(deductiblesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDeductibles() throws Exception {
        // Initialize the database
        deductiblesRepository.saveAndFlush(deductibles);

        // Get all the deductiblesList
        restDeductiblesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deductibles.getId().intValue())))
            .andExpect(jsonPath("$.[*].sectionId").value(hasItem(DEFAULT_SECTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].textAr").value(hasItem(DEFAULT_TEXT_AR)))
            .andExpect(jsonPath("$.[*].textEn").value(hasItem(DEFAULT_TEXT_EN)));
    }

    @Test
    @Transactional
    void getDeductibles() throws Exception {
        // Initialize the database
        deductiblesRepository.saveAndFlush(deductibles);

        // Get the deductibles
        restDeductiblesMockMvc
            .perform(get(ENTITY_API_URL_ID, deductibles.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deductibles.getId().intValue()))
            .andExpect(jsonPath("$.sectionId").value(DEFAULT_SECTION_ID.intValue()))
            .andExpect(jsonPath("$.textAr").value(DEFAULT_TEXT_AR))
            .andExpect(jsonPath("$.textEn").value(DEFAULT_TEXT_EN));
    }

    @Test
    @Transactional
    void getNonExistingDeductibles() throws Exception {
        // Get the deductibles
        restDeductiblesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDeductibles() throws Exception {
        // Initialize the database
        deductiblesRepository.saveAndFlush(deductibles);

        int databaseSizeBeforeUpdate = deductiblesRepository.findAll().size();

        // Update the deductibles
        Deductibles updatedDeductibles = deductiblesRepository.findById(deductibles.getId()).get();
        // Disconnect from session so that the updates on updatedDeductibles are not directly saved in db
        em.detach(updatedDeductibles);
        updatedDeductibles.sectionId(UPDATED_SECTION_ID).textAr(UPDATED_TEXT_AR).textEn(UPDATED_TEXT_EN);

        restDeductiblesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDeductibles.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDeductibles))
            )
            .andExpect(status().isOk());

        // Validate the Deductibles in the database
        List<Deductibles> deductiblesList = deductiblesRepository.findAll();
        assertThat(deductiblesList).hasSize(databaseSizeBeforeUpdate);
        Deductibles testDeductibles = deductiblesList.get(deductiblesList.size() - 1);
        assertThat(testDeductibles.getSectionId()).isEqualTo(UPDATED_SECTION_ID);
        assertThat(testDeductibles.getTextAr()).isEqualTo(UPDATED_TEXT_AR);
        assertThat(testDeductibles.getTextEn()).isEqualTo(UPDATED_TEXT_EN);
    }

    @Test
    @Transactional
    void putNonExistingDeductibles() throws Exception {
        int databaseSizeBeforeUpdate = deductiblesRepository.findAll().size();
        deductibles.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeductiblesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deductibles.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deductibles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deductibles in the database
        List<Deductibles> deductiblesList = deductiblesRepository.findAll();
        assertThat(deductiblesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeductibles() throws Exception {
        int databaseSizeBeforeUpdate = deductiblesRepository.findAll().size();
        deductibles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeductiblesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deductibles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deductibles in the database
        List<Deductibles> deductiblesList = deductiblesRepository.findAll();
        assertThat(deductiblesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeductibles() throws Exception {
        int databaseSizeBeforeUpdate = deductiblesRepository.findAll().size();
        deductibles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeductiblesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deductibles)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deductibles in the database
        List<Deductibles> deductiblesList = deductiblesRepository.findAll();
        assertThat(deductiblesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeductiblesWithPatch() throws Exception {
        // Initialize the database
        deductiblesRepository.saveAndFlush(deductibles);

        int databaseSizeBeforeUpdate = deductiblesRepository.findAll().size();

        // Update the deductibles using partial update
        Deductibles partialUpdatedDeductibles = new Deductibles();
        partialUpdatedDeductibles.setId(deductibles.getId());

        partialUpdatedDeductibles.textAr(UPDATED_TEXT_AR).textEn(UPDATED_TEXT_EN);

        restDeductiblesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeductibles.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeductibles))
            )
            .andExpect(status().isOk());

        // Validate the Deductibles in the database
        List<Deductibles> deductiblesList = deductiblesRepository.findAll();
        assertThat(deductiblesList).hasSize(databaseSizeBeforeUpdate);
        Deductibles testDeductibles = deductiblesList.get(deductiblesList.size() - 1);
        assertThat(testDeductibles.getSectionId()).isEqualTo(DEFAULT_SECTION_ID);
        assertThat(testDeductibles.getTextAr()).isEqualTo(UPDATED_TEXT_AR);
        assertThat(testDeductibles.getTextEn()).isEqualTo(UPDATED_TEXT_EN);
    }

    @Test
    @Transactional
    void fullUpdateDeductiblesWithPatch() throws Exception {
        // Initialize the database
        deductiblesRepository.saveAndFlush(deductibles);

        int databaseSizeBeforeUpdate = deductiblesRepository.findAll().size();

        // Update the deductibles using partial update
        Deductibles partialUpdatedDeductibles = new Deductibles();
        partialUpdatedDeductibles.setId(deductibles.getId());

        partialUpdatedDeductibles.sectionId(UPDATED_SECTION_ID).textAr(UPDATED_TEXT_AR).textEn(UPDATED_TEXT_EN);

        restDeductiblesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeductibles.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeductibles))
            )
            .andExpect(status().isOk());

        // Validate the Deductibles in the database
        List<Deductibles> deductiblesList = deductiblesRepository.findAll();
        assertThat(deductiblesList).hasSize(databaseSizeBeforeUpdate);
        Deductibles testDeductibles = deductiblesList.get(deductiblesList.size() - 1);
        assertThat(testDeductibles.getSectionId()).isEqualTo(UPDATED_SECTION_ID);
        assertThat(testDeductibles.getTextAr()).isEqualTo(UPDATED_TEXT_AR);
        assertThat(testDeductibles.getTextEn()).isEqualTo(UPDATED_TEXT_EN);
    }

    @Test
    @Transactional
    void patchNonExistingDeductibles() throws Exception {
        int databaseSizeBeforeUpdate = deductiblesRepository.findAll().size();
        deductibles.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeductiblesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deductibles.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deductibles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deductibles in the database
        List<Deductibles> deductiblesList = deductiblesRepository.findAll();
        assertThat(deductiblesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeductibles() throws Exception {
        int databaseSizeBeforeUpdate = deductiblesRepository.findAll().size();
        deductibles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeductiblesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deductibles))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deductibles in the database
        List<Deductibles> deductiblesList = deductiblesRepository.findAll();
        assertThat(deductiblesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeductibles() throws Exception {
        int databaseSizeBeforeUpdate = deductiblesRepository.findAll().size();
        deductibles.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeductiblesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(deductibles))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deductibles in the database
        List<Deductibles> deductiblesList = deductiblesRepository.findAll();
        assertThat(deductiblesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeductibles() throws Exception {
        // Initialize the database
        deductiblesRepository.saveAndFlush(deductibles);

        int databaseSizeBeforeDelete = deductiblesRepository.findAll().size();

        // Delete the deductibles
        restDeductiblesMockMvc
            .perform(delete(ENTITY_API_URL_ID, deductibles.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Deductibles> deductiblesList = deductiblesRepository.findAll();
        assertThat(deductiblesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
