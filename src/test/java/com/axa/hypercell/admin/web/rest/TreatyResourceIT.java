package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.Treaty;
import com.axa.hypercell.admin.domain.enumeration.TreatyType;
import com.axa.hypercell.admin.repository.TreatyRepository;
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
 * Integration tests for the {@link TreatyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TreatyResourceIT {

    private static final TreatyType DEFAULT_TREATY_TYPE = TreatyType.Normal;
    private static final TreatyType UPDATED_TREATY_TYPE = TreatyType.Multinational;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TREATY_DOCUMENT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_TREATY_DOCUMENT_PATH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/treaties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TreatyRepository treatyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTreatyMockMvc;

    private Treaty treaty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Treaty createEntity(EntityManager em) {
        Treaty treaty = new Treaty()
            .treatyType(DEFAULT_TREATY_TYPE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .treatyDocumentPath(DEFAULT_TREATY_DOCUMENT_PATH);
        return treaty;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Treaty createUpdatedEntity(EntityManager em) {
        Treaty treaty = new Treaty()
            .treatyType(UPDATED_TREATY_TYPE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .treatyDocumentPath(UPDATED_TREATY_DOCUMENT_PATH);
        return treaty;
    }

    @BeforeEach
    public void initTest() {
        treaty = createEntity(em);
    }

    @Test
    @Transactional
    void createTreaty() throws Exception {
        int databaseSizeBeforeCreate = treatyRepository.findAll().size();
        // Create the Treaty
        restTreatyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(treaty)))
            .andExpect(status().isCreated());

        // Validate the Treaty in the database
        List<Treaty> treatyList = treatyRepository.findAll();
        assertThat(treatyList).hasSize(databaseSizeBeforeCreate + 1);
        Treaty testTreaty = treatyList.get(treatyList.size() - 1);
        assertThat(testTreaty.getTreatyType()).isEqualTo(DEFAULT_TREATY_TYPE);
        assertThat(testTreaty.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testTreaty.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testTreaty.getTreatyDocumentPath()).isEqualTo(DEFAULT_TREATY_DOCUMENT_PATH);
    }

    @Test
    @Transactional
    void createTreatyWithExistingId() throws Exception {
        // Create the Treaty with an existing ID
        treaty.setId(1L);

        int databaseSizeBeforeCreate = treatyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTreatyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(treaty)))
            .andExpect(status().isBadRequest());

        // Validate the Treaty in the database
        List<Treaty> treatyList = treatyRepository.findAll();
        assertThat(treatyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTreaties() throws Exception {
        // Initialize the database
        treatyRepository.saveAndFlush(treaty);

        // Get all the treatyList
        restTreatyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(treaty.getId().intValue())))
            .andExpect(jsonPath("$.[*].treatyType").value(hasItem(DEFAULT_TREATY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].treatyDocumentPath").value(hasItem(DEFAULT_TREATY_DOCUMENT_PATH)));
    }

    @Test
    @Transactional
    void getTreaty() throws Exception {
        // Initialize the database
        treatyRepository.saveAndFlush(treaty);

        // Get the treaty
        restTreatyMockMvc
            .perform(get(ENTITY_API_URL_ID, treaty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(treaty.getId().intValue()))
            .andExpect(jsonPath("$.treatyType").value(DEFAULT_TREATY_TYPE.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.treatyDocumentPath").value(DEFAULT_TREATY_DOCUMENT_PATH));
    }

    @Test
    @Transactional
    void getNonExistingTreaty() throws Exception {
        // Get the treaty
        restTreatyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTreaty() throws Exception {
        // Initialize the database
        treatyRepository.saveAndFlush(treaty);

        int databaseSizeBeforeUpdate = treatyRepository.findAll().size();

        // Update the treaty
        Treaty updatedTreaty = treatyRepository.findById(treaty.getId()).get();
        // Disconnect from session so that the updates on updatedTreaty are not directly saved in db
        em.detach(updatedTreaty);
        updatedTreaty
            .treatyType(UPDATED_TREATY_TYPE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .treatyDocumentPath(UPDATED_TREATY_DOCUMENT_PATH);

        restTreatyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTreaty.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTreaty))
            )
            .andExpect(status().isOk());

        // Validate the Treaty in the database
        List<Treaty> treatyList = treatyRepository.findAll();
        assertThat(treatyList).hasSize(databaseSizeBeforeUpdate);
        Treaty testTreaty = treatyList.get(treatyList.size() - 1);
        assertThat(testTreaty.getTreatyType()).isEqualTo(UPDATED_TREATY_TYPE);
        assertThat(testTreaty.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testTreaty.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testTreaty.getTreatyDocumentPath()).isEqualTo(UPDATED_TREATY_DOCUMENT_PATH);
    }

    @Test
    @Transactional
    void putNonExistingTreaty() throws Exception {
        int databaseSizeBeforeUpdate = treatyRepository.findAll().size();
        treaty.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTreatyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, treaty.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(treaty))
            )
            .andExpect(status().isBadRequest());

        // Validate the Treaty in the database
        List<Treaty> treatyList = treatyRepository.findAll();
        assertThat(treatyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTreaty() throws Exception {
        int databaseSizeBeforeUpdate = treatyRepository.findAll().size();
        treaty.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTreatyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(treaty))
            )
            .andExpect(status().isBadRequest());

        // Validate the Treaty in the database
        List<Treaty> treatyList = treatyRepository.findAll();
        assertThat(treatyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTreaty() throws Exception {
        int databaseSizeBeforeUpdate = treatyRepository.findAll().size();
        treaty.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTreatyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(treaty)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Treaty in the database
        List<Treaty> treatyList = treatyRepository.findAll();
        assertThat(treatyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTreatyWithPatch() throws Exception {
        // Initialize the database
        treatyRepository.saveAndFlush(treaty);

        int databaseSizeBeforeUpdate = treatyRepository.findAll().size();

        // Update the treaty using partial update
        Treaty partialUpdatedTreaty = new Treaty();
        partialUpdatedTreaty.setId(treaty.getId());

        partialUpdatedTreaty.treatyType(UPDATED_TREATY_TYPE).endDate(UPDATED_END_DATE);

        restTreatyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTreaty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTreaty))
            )
            .andExpect(status().isOk());

        // Validate the Treaty in the database
        List<Treaty> treatyList = treatyRepository.findAll();
        assertThat(treatyList).hasSize(databaseSizeBeforeUpdate);
        Treaty testTreaty = treatyList.get(treatyList.size() - 1);
        assertThat(testTreaty.getTreatyType()).isEqualTo(UPDATED_TREATY_TYPE);
        assertThat(testTreaty.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testTreaty.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testTreaty.getTreatyDocumentPath()).isEqualTo(DEFAULT_TREATY_DOCUMENT_PATH);
    }

    @Test
    @Transactional
    void fullUpdateTreatyWithPatch() throws Exception {
        // Initialize the database
        treatyRepository.saveAndFlush(treaty);

        int databaseSizeBeforeUpdate = treatyRepository.findAll().size();

        // Update the treaty using partial update
        Treaty partialUpdatedTreaty = new Treaty();
        partialUpdatedTreaty.setId(treaty.getId());

        partialUpdatedTreaty
            .treatyType(UPDATED_TREATY_TYPE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .treatyDocumentPath(UPDATED_TREATY_DOCUMENT_PATH);

        restTreatyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTreaty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTreaty))
            )
            .andExpect(status().isOk());

        // Validate the Treaty in the database
        List<Treaty> treatyList = treatyRepository.findAll();
        assertThat(treatyList).hasSize(databaseSizeBeforeUpdate);
        Treaty testTreaty = treatyList.get(treatyList.size() - 1);
        assertThat(testTreaty.getTreatyType()).isEqualTo(UPDATED_TREATY_TYPE);
        assertThat(testTreaty.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testTreaty.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testTreaty.getTreatyDocumentPath()).isEqualTo(UPDATED_TREATY_DOCUMENT_PATH);
    }

    @Test
    @Transactional
    void patchNonExistingTreaty() throws Exception {
        int databaseSizeBeforeUpdate = treatyRepository.findAll().size();
        treaty.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTreatyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, treaty.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(treaty))
            )
            .andExpect(status().isBadRequest());

        // Validate the Treaty in the database
        List<Treaty> treatyList = treatyRepository.findAll();
        assertThat(treatyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTreaty() throws Exception {
        int databaseSizeBeforeUpdate = treatyRepository.findAll().size();
        treaty.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTreatyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(treaty))
            )
            .andExpect(status().isBadRequest());

        // Validate the Treaty in the database
        List<Treaty> treatyList = treatyRepository.findAll();
        assertThat(treatyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTreaty() throws Exception {
        int databaseSizeBeforeUpdate = treatyRepository.findAll().size();
        treaty.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTreatyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(treaty)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Treaty in the database
        List<Treaty> treatyList = treatyRepository.findAll();
        assertThat(treatyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTreaty() throws Exception {
        // Initialize the database
        treatyRepository.saveAndFlush(treaty);

        int databaseSizeBeforeDelete = treatyRepository.findAll().size();

        // Delete the treaty
        restTreatyMockMvc
            .perform(delete(ENTITY_API_URL_ID, treaty.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Treaty> treatyList = treatyRepository.findAll();
        assertThat(treatyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
