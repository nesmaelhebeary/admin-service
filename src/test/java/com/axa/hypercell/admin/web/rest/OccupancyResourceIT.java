package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.AdminserviceApp;
import com.axa.hypercell.admin.domain.Occupancy;
import com.axa.hypercell.admin.repository.OccupancyRepository;

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
 * Integration tests for the {@link OccupancyResource} REST controller.
 */
@SpringBootTest(classes = AdminserviceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class OccupancyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private OccupancyRepository occupancyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOccupancyMockMvc;

    private Occupancy occupancy;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Occupancy createEntity(EntityManager em) {
        Occupancy occupancy = new Occupancy()
            .name(DEFAULT_NAME);
        return occupancy;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Occupancy createUpdatedEntity(EntityManager em) {
        Occupancy occupancy = new Occupancy()
            .name(UPDATED_NAME);
        return occupancy;
    }

    @BeforeEach
    public void initTest() {
        occupancy = createEntity(em);
    }

    @Test
    @Transactional
    public void createOccupancy() throws Exception {
        int databaseSizeBeforeCreate = occupancyRepository.findAll().size();
        // Create the Occupancy
        restOccupancyMockMvc.perform(post("/api/occupancies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(occupancy)))
            .andExpect(status().isCreated());

        // Validate the Occupancy in the database
        List<Occupancy> occupancyList = occupancyRepository.findAll();
        assertThat(occupancyList).hasSize(databaseSizeBeforeCreate + 1);
        Occupancy testOccupancy = occupancyList.get(occupancyList.size() - 1);
        assertThat(testOccupancy.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createOccupancyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = occupancyRepository.findAll().size();

        // Create the Occupancy with an existing ID
        occupancy.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOccupancyMockMvc.perform(post("/api/occupancies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(occupancy)))
            .andExpect(status().isBadRequest());

        // Validate the Occupancy in the database
        List<Occupancy> occupancyList = occupancyRepository.findAll();
        assertThat(occupancyList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllOccupancies() throws Exception {
        // Initialize the database
        occupancyRepository.saveAndFlush(occupancy);

        // Get all the occupancyList
        restOccupancyMockMvc.perform(get("/api/occupancies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(occupancy.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getOccupancy() throws Exception {
        // Initialize the database
        occupancyRepository.saveAndFlush(occupancy);

        // Get the occupancy
        restOccupancyMockMvc.perform(get("/api/occupancies/{id}", occupancy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(occupancy.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingOccupancy() throws Exception {
        // Get the occupancy
        restOccupancyMockMvc.perform(get("/api/occupancies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOccupancy() throws Exception {
        // Initialize the database
        occupancyRepository.saveAndFlush(occupancy);

        int databaseSizeBeforeUpdate = occupancyRepository.findAll().size();

        // Update the occupancy
        Occupancy updatedOccupancy = occupancyRepository.findById(occupancy.getId()).get();
        // Disconnect from session so that the updates on updatedOccupancy are not directly saved in db
        em.detach(updatedOccupancy);
        updatedOccupancy
            .name(UPDATED_NAME);

        restOccupancyMockMvc.perform(put("/api/occupancies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedOccupancy)))
            .andExpect(status().isOk());

        // Validate the Occupancy in the database
        List<Occupancy> occupancyList = occupancyRepository.findAll();
        assertThat(occupancyList).hasSize(databaseSizeBeforeUpdate);
        Occupancy testOccupancy = occupancyList.get(occupancyList.size() - 1);
        assertThat(testOccupancy.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingOccupancy() throws Exception {
        int databaseSizeBeforeUpdate = occupancyRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOccupancyMockMvc.perform(put("/api/occupancies")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(occupancy)))
            .andExpect(status().isBadRequest());

        // Validate the Occupancy in the database
        List<Occupancy> occupancyList = occupancyRepository.findAll();
        assertThat(occupancyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOccupancy() throws Exception {
        // Initialize the database
        occupancyRepository.saveAndFlush(occupancy);

        int databaseSizeBeforeDelete = occupancyRepository.findAll().size();

        // Delete the occupancy
        restOccupancyMockMvc.perform(delete("/api/occupancies/{id}", occupancy.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Occupancy> occupancyList = occupancyRepository.findAll();
        assertThat(occupancyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
