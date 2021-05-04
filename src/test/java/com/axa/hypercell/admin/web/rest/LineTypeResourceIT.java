package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.LineType;
import com.axa.hypercell.admin.repository.LineTypeRepository;
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
 * Integration tests for the {@link LineTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LineTypeResourceIT {

    private static final String DEFAULT_NAME_EN = "AAAAAAAAAA";
    private static final String UPDATED_NAME_EN = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_AR = "AAAAAAAAAA";
    private static final String UPDATED_NAME_AR = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/line-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LineTypeRepository lineTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLineTypeMockMvc;

    private LineType lineType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LineType createEntity(EntityManager em) {
        LineType lineType = new LineType().nameEn(DEFAULT_NAME_EN).nameAr(DEFAULT_NAME_AR).description(DEFAULT_DESCRIPTION);
        return lineType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LineType createUpdatedEntity(EntityManager em) {
        LineType lineType = new LineType().nameEn(UPDATED_NAME_EN).nameAr(UPDATED_NAME_AR).description(UPDATED_DESCRIPTION);
        return lineType;
    }

    @BeforeEach
    public void initTest() {
        lineType = createEntity(em);
    }

    @Test
    @Transactional
    void createLineType() throws Exception {
        int databaseSizeBeforeCreate = lineTypeRepository.findAll().size();
        // Create the LineType
        restLineTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lineType)))
            .andExpect(status().isCreated());

        // Validate the LineType in the database
        List<LineType> lineTypeList = lineTypeRepository.findAll();
        assertThat(lineTypeList).hasSize(databaseSizeBeforeCreate + 1);
        LineType testLineType = lineTypeList.get(lineTypeList.size() - 1);
        assertThat(testLineType.getNameEn()).isEqualTo(DEFAULT_NAME_EN);
        assertThat(testLineType.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
        assertThat(testLineType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createLineTypeWithExistingId() throws Exception {
        // Create the LineType with an existing ID
        lineType.setId(1L);

        int databaseSizeBeforeCreate = lineTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLineTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lineType)))
            .andExpect(status().isBadRequest());

        // Validate the LineType in the database
        List<LineType> lineTypeList = lineTypeRepository.findAll();
        assertThat(lineTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLineTypes() throws Exception {
        // Initialize the database
        lineTypeRepository.saveAndFlush(lineType);

        // Get all the lineTypeList
        restLineTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lineType.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getLineType() throws Exception {
        // Initialize the database
        lineTypeRepository.saveAndFlush(lineType);

        // Get the lineType
        restLineTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, lineType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lineType.getId().intValue()))
            .andExpect(jsonPath("$.nameEn").value(DEFAULT_NAME_EN))
            .andExpect(jsonPath("$.nameAr").value(DEFAULT_NAME_AR))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingLineType() throws Exception {
        // Get the lineType
        restLineTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLineType() throws Exception {
        // Initialize the database
        lineTypeRepository.saveAndFlush(lineType);

        int databaseSizeBeforeUpdate = lineTypeRepository.findAll().size();

        // Update the lineType
        LineType updatedLineType = lineTypeRepository.findById(lineType.getId()).get();
        // Disconnect from session so that the updates on updatedLineType are not directly saved in db
        em.detach(updatedLineType);
        updatedLineType.nameEn(UPDATED_NAME_EN).nameAr(UPDATED_NAME_AR).description(UPDATED_DESCRIPTION);

        restLineTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLineType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLineType))
            )
            .andExpect(status().isOk());

        // Validate the LineType in the database
        List<LineType> lineTypeList = lineTypeRepository.findAll();
        assertThat(lineTypeList).hasSize(databaseSizeBeforeUpdate);
        LineType testLineType = lineTypeList.get(lineTypeList.size() - 1);
        assertThat(testLineType.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testLineType.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testLineType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingLineType() throws Exception {
        int databaseSizeBeforeUpdate = lineTypeRepository.findAll().size();
        lineType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLineTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lineType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lineType))
            )
            .andExpect(status().isBadRequest());

        // Validate the LineType in the database
        List<LineType> lineTypeList = lineTypeRepository.findAll();
        assertThat(lineTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLineType() throws Exception {
        int databaseSizeBeforeUpdate = lineTypeRepository.findAll().size();
        lineType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLineTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lineType))
            )
            .andExpect(status().isBadRequest());

        // Validate the LineType in the database
        List<LineType> lineTypeList = lineTypeRepository.findAll();
        assertThat(lineTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLineType() throws Exception {
        int databaseSizeBeforeUpdate = lineTypeRepository.findAll().size();
        lineType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLineTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lineType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LineType in the database
        List<LineType> lineTypeList = lineTypeRepository.findAll();
        assertThat(lineTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLineTypeWithPatch() throws Exception {
        // Initialize the database
        lineTypeRepository.saveAndFlush(lineType);

        int databaseSizeBeforeUpdate = lineTypeRepository.findAll().size();

        // Update the lineType using partial update
        LineType partialUpdatedLineType = new LineType();
        partialUpdatedLineType.setId(lineType.getId());

        partialUpdatedLineType.nameEn(UPDATED_NAME_EN);

        restLineTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLineType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLineType))
            )
            .andExpect(status().isOk());

        // Validate the LineType in the database
        List<LineType> lineTypeList = lineTypeRepository.findAll();
        assertThat(lineTypeList).hasSize(databaseSizeBeforeUpdate);
        LineType testLineType = lineTypeList.get(lineTypeList.size() - 1);
        assertThat(testLineType.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testLineType.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
        assertThat(testLineType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateLineTypeWithPatch() throws Exception {
        // Initialize the database
        lineTypeRepository.saveAndFlush(lineType);

        int databaseSizeBeforeUpdate = lineTypeRepository.findAll().size();

        // Update the lineType using partial update
        LineType partialUpdatedLineType = new LineType();
        partialUpdatedLineType.setId(lineType.getId());

        partialUpdatedLineType.nameEn(UPDATED_NAME_EN).nameAr(UPDATED_NAME_AR).description(UPDATED_DESCRIPTION);

        restLineTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLineType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLineType))
            )
            .andExpect(status().isOk());

        // Validate the LineType in the database
        List<LineType> lineTypeList = lineTypeRepository.findAll();
        assertThat(lineTypeList).hasSize(databaseSizeBeforeUpdate);
        LineType testLineType = lineTypeList.get(lineTypeList.size() - 1);
        assertThat(testLineType.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testLineType.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testLineType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingLineType() throws Exception {
        int databaseSizeBeforeUpdate = lineTypeRepository.findAll().size();
        lineType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLineTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lineType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lineType))
            )
            .andExpect(status().isBadRequest());

        // Validate the LineType in the database
        List<LineType> lineTypeList = lineTypeRepository.findAll();
        assertThat(lineTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLineType() throws Exception {
        int databaseSizeBeforeUpdate = lineTypeRepository.findAll().size();
        lineType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLineTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lineType))
            )
            .andExpect(status().isBadRequest());

        // Validate the LineType in the database
        List<LineType> lineTypeList = lineTypeRepository.findAll();
        assertThat(lineTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLineType() throws Exception {
        int databaseSizeBeforeUpdate = lineTypeRepository.findAll().size();
        lineType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLineTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(lineType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LineType in the database
        List<LineType> lineTypeList = lineTypeRepository.findAll();
        assertThat(lineTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLineType() throws Exception {
        // Initialize the database
        lineTypeRepository.saveAndFlush(lineType);

        int databaseSizeBeforeDelete = lineTypeRepository.findAll().size();

        // Delete the lineType
        restLineTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, lineType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LineType> lineTypeList = lineTypeRepository.findAll();
        assertThat(lineTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
