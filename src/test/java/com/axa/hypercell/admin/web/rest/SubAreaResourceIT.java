package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.SubArea;
import com.axa.hypercell.admin.repository.SubAreaRepository;
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
 * Integration tests for the {@link SubAreaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubAreaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Long DEFAULT_CRESTA_ID = 1L;
    private static final Long UPDATED_CRESTA_ID = 2L;

    private static final String ENTITY_API_URL = "/api/sub-areas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubAreaRepository subAreaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubAreaMockMvc;

    private SubArea subArea;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubArea createEntity(EntityManager em) {
        SubArea subArea = new SubArea().name(DEFAULT_NAME).code(DEFAULT_CODE).crestaId(DEFAULT_CRESTA_ID);
        return subArea;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubArea createUpdatedEntity(EntityManager em) {
        SubArea subArea = new SubArea().name(UPDATED_NAME).code(UPDATED_CODE).crestaId(UPDATED_CRESTA_ID);
        return subArea;
    }

    @BeforeEach
    public void initTest() {
        subArea = createEntity(em);
    }

    @Test
    @Transactional
    void createSubArea() throws Exception {
        int databaseSizeBeforeCreate = subAreaRepository.findAll().size();
        // Create the SubArea
        restSubAreaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subArea)))
            .andExpect(status().isCreated());

        // Validate the SubArea in the database
        List<SubArea> subAreaList = subAreaRepository.findAll();
        assertThat(subAreaList).hasSize(databaseSizeBeforeCreate + 1);
        SubArea testSubArea = subAreaList.get(subAreaList.size() - 1);
        assertThat(testSubArea.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSubArea.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSubArea.getCrestaId()).isEqualTo(DEFAULT_CRESTA_ID);
    }

    @Test
    @Transactional
    void createSubAreaWithExistingId() throws Exception {
        // Create the SubArea with an existing ID
        subArea.setId(1L);

        int databaseSizeBeforeCreate = subAreaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubAreaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subArea)))
            .andExpect(status().isBadRequest());

        // Validate the SubArea in the database
        List<SubArea> subAreaList = subAreaRepository.findAll();
        assertThat(subAreaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSubAreas() throws Exception {
        // Initialize the database
        subAreaRepository.saveAndFlush(subArea);

        // Get all the subAreaList
        restSubAreaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subArea.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].crestaId").value(hasItem(DEFAULT_CRESTA_ID.intValue())));
    }

    @Test
    @Transactional
    void getSubArea() throws Exception {
        // Initialize the database
        subAreaRepository.saveAndFlush(subArea);

        // Get the subArea
        restSubAreaMockMvc
            .perform(get(ENTITY_API_URL_ID, subArea.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subArea.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.crestaId").value(DEFAULT_CRESTA_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSubArea() throws Exception {
        // Get the subArea
        restSubAreaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSubArea() throws Exception {
        // Initialize the database
        subAreaRepository.saveAndFlush(subArea);

        int databaseSizeBeforeUpdate = subAreaRepository.findAll().size();

        // Update the subArea
        SubArea updatedSubArea = subAreaRepository.findById(subArea.getId()).get();
        // Disconnect from session so that the updates on updatedSubArea are not directly saved in db
        em.detach(updatedSubArea);
        updatedSubArea.name(UPDATED_NAME).code(UPDATED_CODE).crestaId(UPDATED_CRESTA_ID);

        restSubAreaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSubArea.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSubArea))
            )
            .andExpect(status().isOk());

        // Validate the SubArea in the database
        List<SubArea> subAreaList = subAreaRepository.findAll();
        assertThat(subAreaList).hasSize(databaseSizeBeforeUpdate);
        SubArea testSubArea = subAreaList.get(subAreaList.size() - 1);
        assertThat(testSubArea.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubArea.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSubArea.getCrestaId()).isEqualTo(UPDATED_CRESTA_ID);
    }

    @Test
    @Transactional
    void putNonExistingSubArea() throws Exception {
        int databaseSizeBeforeUpdate = subAreaRepository.findAll().size();
        subArea.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubAreaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subArea.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subArea))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubArea in the database
        List<SubArea> subAreaList = subAreaRepository.findAll();
        assertThat(subAreaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubArea() throws Exception {
        int databaseSizeBeforeUpdate = subAreaRepository.findAll().size();
        subArea.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubAreaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subArea))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubArea in the database
        List<SubArea> subAreaList = subAreaRepository.findAll();
        assertThat(subAreaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubArea() throws Exception {
        int databaseSizeBeforeUpdate = subAreaRepository.findAll().size();
        subArea.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubAreaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subArea)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubArea in the database
        List<SubArea> subAreaList = subAreaRepository.findAll();
        assertThat(subAreaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubAreaWithPatch() throws Exception {
        // Initialize the database
        subAreaRepository.saveAndFlush(subArea);

        int databaseSizeBeforeUpdate = subAreaRepository.findAll().size();

        // Update the subArea using partial update
        SubArea partialUpdatedSubArea = new SubArea();
        partialUpdatedSubArea.setId(subArea.getId());

        restSubAreaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubArea.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubArea))
            )
            .andExpect(status().isOk());

        // Validate the SubArea in the database
        List<SubArea> subAreaList = subAreaRepository.findAll();
        assertThat(subAreaList).hasSize(databaseSizeBeforeUpdate);
        SubArea testSubArea = subAreaList.get(subAreaList.size() - 1);
        assertThat(testSubArea.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSubArea.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testSubArea.getCrestaId()).isEqualTo(DEFAULT_CRESTA_ID);
    }

    @Test
    @Transactional
    void fullUpdateSubAreaWithPatch() throws Exception {
        // Initialize the database
        subAreaRepository.saveAndFlush(subArea);

        int databaseSizeBeforeUpdate = subAreaRepository.findAll().size();

        // Update the subArea using partial update
        SubArea partialUpdatedSubArea = new SubArea();
        partialUpdatedSubArea.setId(subArea.getId());

        partialUpdatedSubArea.name(UPDATED_NAME).code(UPDATED_CODE).crestaId(UPDATED_CRESTA_ID);

        restSubAreaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubArea.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubArea))
            )
            .andExpect(status().isOk());

        // Validate the SubArea in the database
        List<SubArea> subAreaList = subAreaRepository.findAll();
        assertThat(subAreaList).hasSize(databaseSizeBeforeUpdate);
        SubArea testSubArea = subAreaList.get(subAreaList.size() - 1);
        assertThat(testSubArea.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubArea.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testSubArea.getCrestaId()).isEqualTo(UPDATED_CRESTA_ID);
    }

    @Test
    @Transactional
    void patchNonExistingSubArea() throws Exception {
        int databaseSizeBeforeUpdate = subAreaRepository.findAll().size();
        subArea.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubAreaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subArea.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subArea))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubArea in the database
        List<SubArea> subAreaList = subAreaRepository.findAll();
        assertThat(subAreaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubArea() throws Exception {
        int databaseSizeBeforeUpdate = subAreaRepository.findAll().size();
        subArea.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubAreaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subArea))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubArea in the database
        List<SubArea> subAreaList = subAreaRepository.findAll();
        assertThat(subAreaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubArea() throws Exception {
        int databaseSizeBeforeUpdate = subAreaRepository.findAll().size();
        subArea.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubAreaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(subArea)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubArea in the database
        List<SubArea> subAreaList = subAreaRepository.findAll();
        assertThat(subAreaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubArea() throws Exception {
        // Initialize the database
        subAreaRepository.saveAndFlush(subArea);

        int databaseSizeBeforeDelete = subAreaRepository.findAll().size();

        // Delete the subArea
        restSubAreaMockMvc
            .perform(delete(ENTITY_API_URL_ID, subArea.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubArea> subAreaList = subAreaRepository.findAll();
        assertThat(subAreaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
