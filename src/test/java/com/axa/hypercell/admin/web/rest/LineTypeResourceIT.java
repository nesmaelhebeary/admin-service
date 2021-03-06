package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.AdminserviceApp;
import com.axa.hypercell.admin.domain.LineType;
import com.axa.hypercell.admin.repository.LineTypeRepository;

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
 * Integration tests for the {@link LineTypeResource} REST controller.
 */
@SpringBootTest(classes = AdminserviceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class LineTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

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
        LineType lineType = new LineType()
            .name(DEFAULT_NAME);
        return lineType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LineType createUpdatedEntity(EntityManager em) {
        LineType lineType = new LineType()
            .name(UPDATED_NAME);
        return lineType;
    }

    @BeforeEach
    public void initTest() {
        lineType = createEntity(em);
    }

    @Test
    @Transactional
    public void createLineType() throws Exception {
        int databaseSizeBeforeCreate = lineTypeRepository.findAll().size();
        // Create the LineType
        restLineTypeMockMvc.perform(post("/api/line-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lineType)))
            .andExpect(status().isCreated());

        // Validate the LineType in the database
        List<LineType> lineTypeList = lineTypeRepository.findAll();
        assertThat(lineTypeList).hasSize(databaseSizeBeforeCreate + 1);
        LineType testLineType = lineTypeList.get(lineTypeList.size() - 1);
        assertThat(testLineType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createLineTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lineTypeRepository.findAll().size();

        // Create the LineType with an existing ID
        lineType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLineTypeMockMvc.perform(post("/api/line-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lineType)))
            .andExpect(status().isBadRequest());

        // Validate the LineType in the database
        List<LineType> lineTypeList = lineTypeRepository.findAll();
        assertThat(lineTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllLineTypes() throws Exception {
        // Initialize the database
        lineTypeRepository.saveAndFlush(lineType);

        // Get all the lineTypeList
        restLineTypeMockMvc.perform(get("/api/line-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lineType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getLineType() throws Exception {
        // Initialize the database
        lineTypeRepository.saveAndFlush(lineType);

        // Get the lineType
        restLineTypeMockMvc.perform(get("/api/line-types/{id}", lineType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lineType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingLineType() throws Exception {
        // Get the lineType
        restLineTypeMockMvc.perform(get("/api/line-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLineType() throws Exception {
        // Initialize the database
        lineTypeRepository.saveAndFlush(lineType);

        int databaseSizeBeforeUpdate = lineTypeRepository.findAll().size();

        // Update the lineType
        LineType updatedLineType = lineTypeRepository.findById(lineType.getId()).get();
        // Disconnect from session so that the updates on updatedLineType are not directly saved in db
        em.detach(updatedLineType);
        updatedLineType
            .name(UPDATED_NAME);

        restLineTypeMockMvc.perform(put("/api/line-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedLineType)))
            .andExpect(status().isOk());

        // Validate the LineType in the database
        List<LineType> lineTypeList = lineTypeRepository.findAll();
        assertThat(lineTypeList).hasSize(databaseSizeBeforeUpdate);
        LineType testLineType = lineTypeList.get(lineTypeList.size() - 1);
        assertThat(testLineType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingLineType() throws Exception {
        int databaseSizeBeforeUpdate = lineTypeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLineTypeMockMvc.perform(put("/api/line-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(lineType)))
            .andExpect(status().isBadRequest());

        // Validate the LineType in the database
        List<LineType> lineTypeList = lineTypeRepository.findAll();
        assertThat(lineTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLineType() throws Exception {
        // Initialize the database
        lineTypeRepository.saveAndFlush(lineType);

        int databaseSizeBeforeDelete = lineTypeRepository.findAll().size();

        // Delete the lineType
        restLineTypeMockMvc.perform(delete("/api/line-types/{id}", lineType.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LineType> lineTypeList = lineTypeRepository.findAll();
        assertThat(lineTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
