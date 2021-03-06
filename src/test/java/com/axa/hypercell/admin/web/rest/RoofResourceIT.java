package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.AdminserviceApp;
import com.axa.hypercell.admin.domain.Roof;
import com.axa.hypercell.admin.repository.RoofRepository;

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
 * Integration tests for the {@link RoofResource} REST controller.
 */
@SpringBootTest(classes = AdminserviceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RoofResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

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
        Roof roof = new Roof()
            .name(DEFAULT_NAME);
        return roof;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Roof createUpdatedEntity(EntityManager em) {
        Roof roof = new Roof()
            .name(UPDATED_NAME);
        return roof;
    }

    @BeforeEach
    public void initTest() {
        roof = createEntity(em);
    }

    @Test
    @Transactional
    public void createRoof() throws Exception {
        int databaseSizeBeforeCreate = roofRepository.findAll().size();
        // Create the Roof
        restRoofMockMvc.perform(post("/api/roofs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(roof)))
            .andExpect(status().isCreated());

        // Validate the Roof in the database
        List<Roof> roofList = roofRepository.findAll();
        assertThat(roofList).hasSize(databaseSizeBeforeCreate + 1);
        Roof testRoof = roofList.get(roofList.size() - 1);
        assertThat(testRoof.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createRoofWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = roofRepository.findAll().size();

        // Create the Roof with an existing ID
        roof.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoofMockMvc.perform(post("/api/roofs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(roof)))
            .andExpect(status().isBadRequest());

        // Validate the Roof in the database
        List<Roof> roofList = roofRepository.findAll();
        assertThat(roofList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRoofs() throws Exception {
        // Initialize the database
        roofRepository.saveAndFlush(roof);

        // Get all the roofList
        restRoofMockMvc.perform(get("/api/roofs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roof.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getRoof() throws Exception {
        // Initialize the database
        roofRepository.saveAndFlush(roof);

        // Get the roof
        restRoofMockMvc.perform(get("/api/roofs/{id}", roof.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roof.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingRoof() throws Exception {
        // Get the roof
        restRoofMockMvc.perform(get("/api/roofs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoof() throws Exception {
        // Initialize the database
        roofRepository.saveAndFlush(roof);

        int databaseSizeBeforeUpdate = roofRepository.findAll().size();

        // Update the roof
        Roof updatedRoof = roofRepository.findById(roof.getId()).get();
        // Disconnect from session so that the updates on updatedRoof are not directly saved in db
        em.detach(updatedRoof);
        updatedRoof
            .name(UPDATED_NAME);

        restRoofMockMvc.perform(put("/api/roofs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRoof)))
            .andExpect(status().isOk());

        // Validate the Roof in the database
        List<Roof> roofList = roofRepository.findAll();
        assertThat(roofList).hasSize(databaseSizeBeforeUpdate);
        Roof testRoof = roofList.get(roofList.size() - 1);
        assertThat(testRoof.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingRoof() throws Exception {
        int databaseSizeBeforeUpdate = roofRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoofMockMvc.perform(put("/api/roofs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(roof)))
            .andExpect(status().isBadRequest());

        // Validate the Roof in the database
        List<Roof> roofList = roofRepository.findAll();
        assertThat(roofList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRoof() throws Exception {
        // Initialize the database
        roofRepository.saveAndFlush(roof);

        int databaseSizeBeforeDelete = roofRepository.findAll().size();

        // Delete the roof
        restRoofMockMvc.perform(delete("/api/roofs/{id}", roof.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Roof> roofList = roofRepository.findAll();
        assertThat(roofList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
