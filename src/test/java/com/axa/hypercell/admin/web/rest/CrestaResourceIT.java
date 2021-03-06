package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.AdminserviceApp;
import com.axa.hypercell.admin.domain.Cresta;
import com.axa.hypercell.admin.repository.CrestaRepository;

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
 * Integration tests for the {@link CrestaResource} REST controller.
 */
@SpringBootTest(classes = AdminserviceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CrestaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private CrestaRepository crestaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrestaMockMvc;

    private Cresta cresta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cresta createEntity(EntityManager em) {
        Cresta cresta = new Cresta()
            .name(DEFAULT_NAME);
        return cresta;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cresta createUpdatedEntity(EntityManager em) {
        Cresta cresta = new Cresta()
            .name(UPDATED_NAME);
        return cresta;
    }

    @BeforeEach
    public void initTest() {
        cresta = createEntity(em);
    }

    @Test
    @Transactional
    public void createCresta() throws Exception {
        int databaseSizeBeforeCreate = crestaRepository.findAll().size();
        // Create the Cresta
        restCrestaMockMvc.perform(post("/api/crestas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cresta)))
            .andExpect(status().isCreated());

        // Validate the Cresta in the database
        List<Cresta> crestaList = crestaRepository.findAll();
        assertThat(crestaList).hasSize(databaseSizeBeforeCreate + 1);
        Cresta testCresta = crestaList.get(crestaList.size() - 1);
        assertThat(testCresta.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createCrestaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = crestaRepository.findAll().size();

        // Create the Cresta with an existing ID
        cresta.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrestaMockMvc.perform(post("/api/crestas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cresta)))
            .andExpect(status().isBadRequest());

        // Validate the Cresta in the database
        List<Cresta> crestaList = crestaRepository.findAll();
        assertThat(crestaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCrestas() throws Exception {
        // Initialize the database
        crestaRepository.saveAndFlush(cresta);

        // Get all the crestaList
        restCrestaMockMvc.perform(get("/api/crestas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cresta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getCresta() throws Exception {
        // Initialize the database
        crestaRepository.saveAndFlush(cresta);

        // Get the cresta
        restCrestaMockMvc.perform(get("/api/crestas/{id}", cresta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cresta.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingCresta() throws Exception {
        // Get the cresta
        restCrestaMockMvc.perform(get("/api/crestas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCresta() throws Exception {
        // Initialize the database
        crestaRepository.saveAndFlush(cresta);

        int databaseSizeBeforeUpdate = crestaRepository.findAll().size();

        // Update the cresta
        Cresta updatedCresta = crestaRepository.findById(cresta.getId()).get();
        // Disconnect from session so that the updates on updatedCresta are not directly saved in db
        em.detach(updatedCresta);
        updatedCresta
            .name(UPDATED_NAME);

        restCrestaMockMvc.perform(put("/api/crestas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCresta)))
            .andExpect(status().isOk());

        // Validate the Cresta in the database
        List<Cresta> crestaList = crestaRepository.findAll();
        assertThat(crestaList).hasSize(databaseSizeBeforeUpdate);
        Cresta testCresta = crestaList.get(crestaList.size() - 1);
        assertThat(testCresta.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingCresta() throws Exception {
        int databaseSizeBeforeUpdate = crestaRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrestaMockMvc.perform(put("/api/crestas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(cresta)))
            .andExpect(status().isBadRequest());

        // Validate the Cresta in the database
        List<Cresta> crestaList = crestaRepository.findAll();
        assertThat(crestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCresta() throws Exception {
        // Initialize the database
        crestaRepository.saveAndFlush(cresta);

        int databaseSizeBeforeDelete = crestaRepository.findAll().size();

        // Delete the cresta
        restCrestaMockMvc.perform(delete("/api/crestas/{id}", cresta.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cresta> crestaList = crestaRepository.findAll();
        assertThat(crestaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
