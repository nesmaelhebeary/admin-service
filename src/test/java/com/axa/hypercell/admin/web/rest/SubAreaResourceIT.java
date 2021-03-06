package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.AdminserviceApp;
import com.axa.hypercell.admin.domain.SubArea;
import com.axa.hypercell.admin.repository.SubAreaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SubAreaResource} REST controller.
 */
@SpringBootTest(classes = AdminserviceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class SubAreaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_CRESTA_ID = 1L;
    private static final Long UPDATED_CRESTA_ID = 2L;

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
        SubArea subArea = new SubArea()
            .name(DEFAULT_NAME)
            .crestaId(DEFAULT_CRESTA_ID);
        return subArea;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubArea createUpdatedEntity(EntityManager em) {
        SubArea subArea = new SubArea()
            .name(UPDATED_NAME)
            .crestaId(UPDATED_CRESTA_ID);
        return subArea;
    }

    @BeforeEach
    public void initTest() {
        subArea = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubArea() throws Exception {
        int databaseSizeBeforeCreate = subAreaRepository.findAll().size();
        // Create the SubArea
        restSubAreaMockMvc.perform(post("/api/sub-areas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subArea)))
            .andExpect(status().isCreated());

        // Validate the SubArea in the database
        List<SubArea> subAreaList = subAreaRepository.findAll();
        assertThat(subAreaList).hasSize(databaseSizeBeforeCreate + 1);
        SubArea testSubArea = subAreaList.get(subAreaList.size() - 1);
        assertThat(testSubArea.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSubArea.getCrestaId()).isEqualTo(DEFAULT_CRESTA_ID);
    }

    @Test
    @Transactional
    public void createSubAreaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = subAreaRepository.findAll().size();

        // Create the SubArea with an existing ID
        subArea.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubAreaMockMvc.perform(post("/api/sub-areas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subArea)))
            .andExpect(status().isBadRequest());

        // Validate the SubArea in the database
        List<SubArea> subAreaList = subAreaRepository.findAll();
        assertThat(subAreaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSubAreas() throws Exception {
        // Initialize the database
        subAreaRepository.saveAndFlush(subArea);

        // Get all the subAreaList
        restSubAreaMockMvc.perform(get("/api/sub-areas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subArea.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].crestaId").value(hasItem(DEFAULT_CRESTA_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getSubArea() throws Exception {
        // Initialize the database
        subAreaRepository.saveAndFlush(subArea);

        // Get the subArea
        restSubAreaMockMvc.perform(get("/api/sub-areas/{id}", subArea.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subArea.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.crestaId").value(DEFAULT_CRESTA_ID.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingSubArea() throws Exception {
        // Get the subArea
        restSubAreaMockMvc.perform(get("/api/sub-areas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubArea() throws Exception {
        // Initialize the database
        subAreaRepository.saveAndFlush(subArea);

        int databaseSizeBeforeUpdate = subAreaRepository.findAll().size();

        // Update the subArea
        SubArea updatedSubArea = subAreaRepository.findById(subArea.getId()).get();
        // Disconnect from session so that the updates on updatedSubArea are not directly saved in db
        em.detach(updatedSubArea);
        updatedSubArea
            .name(UPDATED_NAME)
            .crestaId(UPDATED_CRESTA_ID);

        restSubAreaMockMvc.perform(put("/api/sub-areas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSubArea)))
            .andExpect(status().isOk());

        // Validate the SubArea in the database
        List<SubArea> subAreaList = subAreaRepository.findAll();
        assertThat(subAreaList).hasSize(databaseSizeBeforeUpdate);
        SubArea testSubArea = subAreaList.get(subAreaList.size() - 1);
        assertThat(testSubArea.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSubArea.getCrestaId()).isEqualTo(UPDATED_CRESTA_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingSubArea() throws Exception {
        int databaseSizeBeforeUpdate = subAreaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubAreaMockMvc.perform(put("/api/sub-areas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(subArea)))
            .andExpect(status().isBadRequest());

        // Validate the SubArea in the database
        List<SubArea> subAreaList = subAreaRepository.findAll();
        assertThat(subAreaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSubArea() throws Exception {
        // Initialize the database
        subAreaRepository.saveAndFlush(subArea);

        int databaseSizeBeforeDelete = subAreaRepository.findAll().size();

        // Delete the subArea
        restSubAreaMockMvc.perform(delete("/api/sub-areas/{id}", subArea.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubArea> subAreaList = subAreaRepository.findAll();
        assertThat(subAreaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
