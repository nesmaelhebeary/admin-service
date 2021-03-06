package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.AdminserviceApp;
import com.axa.hypercell.admin.domain.NaceCode;
import com.axa.hypercell.admin.repository.NaceCodeRepository;

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
 * Integration tests for the {@link NaceCodeResource} REST controller.
 */
@SpringBootTest(classes = AdminserviceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class NaceCodeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    @Autowired
    private NaceCodeRepository naceCodeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNaceCodeMockMvc;

    private NaceCode naceCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NaceCode createEntity(EntityManager em) {
        NaceCode naceCode = new NaceCode()
            .name(DEFAULT_NAME)
            .category(DEFAULT_CATEGORY);
        return naceCode;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NaceCode createUpdatedEntity(EntityManager em) {
        NaceCode naceCode = new NaceCode()
            .name(UPDATED_NAME)
            .category(UPDATED_CATEGORY);
        return naceCode;
    }

    @BeforeEach
    public void initTest() {
        naceCode = createEntity(em);
    }

    @Test
    @Transactional
    public void createNaceCode() throws Exception {
        int databaseSizeBeforeCreate = naceCodeRepository.findAll().size();
        // Create the NaceCode
        restNaceCodeMockMvc.perform(post("/api/nace-codes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(naceCode)))
            .andExpect(status().isCreated());

        // Validate the NaceCode in the database
        List<NaceCode> naceCodeList = naceCodeRepository.findAll();
        assertThat(naceCodeList).hasSize(databaseSizeBeforeCreate + 1);
        NaceCode testNaceCode = naceCodeList.get(naceCodeList.size() - 1);
        assertThat(testNaceCode.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testNaceCode.getCategory()).isEqualTo(DEFAULT_CATEGORY);
    }

    @Test
    @Transactional
    public void createNaceCodeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = naceCodeRepository.findAll().size();

        // Create the NaceCode with an existing ID
        naceCode.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNaceCodeMockMvc.perform(post("/api/nace-codes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(naceCode)))
            .andExpect(status().isBadRequest());

        // Validate the NaceCode in the database
        List<NaceCode> naceCodeList = naceCodeRepository.findAll();
        assertThat(naceCodeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllNaceCodes() throws Exception {
        // Initialize the database
        naceCodeRepository.saveAndFlush(naceCode);

        // Get all the naceCodeList
        restNaceCodeMockMvc.perform(get("/api/nace-codes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(naceCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)));
    }
    
    @Test
    @Transactional
    public void getNaceCode() throws Exception {
        // Initialize the database
        naceCodeRepository.saveAndFlush(naceCode);

        // Get the naceCode
        restNaceCodeMockMvc.perform(get("/api/nace-codes/{id}", naceCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(naceCode.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY));
    }
    @Test
    @Transactional
    public void getNonExistingNaceCode() throws Exception {
        // Get the naceCode
        restNaceCodeMockMvc.perform(get("/api/nace-codes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNaceCode() throws Exception {
        // Initialize the database
        naceCodeRepository.saveAndFlush(naceCode);

        int databaseSizeBeforeUpdate = naceCodeRepository.findAll().size();

        // Update the naceCode
        NaceCode updatedNaceCode = naceCodeRepository.findById(naceCode.getId()).get();
        // Disconnect from session so that the updates on updatedNaceCode are not directly saved in db
        em.detach(updatedNaceCode);
        updatedNaceCode
            .name(UPDATED_NAME)
            .category(UPDATED_CATEGORY);

        restNaceCodeMockMvc.perform(put("/api/nace-codes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedNaceCode)))
            .andExpect(status().isOk());

        // Validate the NaceCode in the database
        List<NaceCode> naceCodeList = naceCodeRepository.findAll();
        assertThat(naceCodeList).hasSize(databaseSizeBeforeUpdate);
        NaceCode testNaceCode = naceCodeList.get(naceCodeList.size() - 1);
        assertThat(testNaceCode.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNaceCode.getCategory()).isEqualTo(UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    public void updateNonExistingNaceCode() throws Exception {
        int databaseSizeBeforeUpdate = naceCodeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNaceCodeMockMvc.perform(put("/api/nace-codes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(naceCode)))
            .andExpect(status().isBadRequest());

        // Validate the NaceCode in the database
        List<NaceCode> naceCodeList = naceCodeRepository.findAll();
        assertThat(naceCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNaceCode() throws Exception {
        // Initialize the database
        naceCodeRepository.saveAndFlush(naceCode);

        int databaseSizeBeforeDelete = naceCodeRepository.findAll().size();

        // Delete the naceCode
        restNaceCodeMockMvc.perform(delete("/api/nace-codes/{id}", naceCode.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NaceCode> naceCodeList = naceCodeRepository.findAll();
        assertThat(naceCodeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
