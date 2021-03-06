package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.AdminserviceApp;
import com.axa.hypercell.admin.domain.Treaty;
import com.axa.hypercell.admin.repository.TreatyRepository;

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
 * Integration tests for the {@link TreatyResource} REST controller.
 */
@SpringBootTest(classes = AdminserviceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TreatyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_LINE_TYPE_ID = 1L;
    private static final Long UPDATED_LINE_TYPE_ID = 2L;

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
            .name(DEFAULT_NAME)
            .lineTypeId(DEFAULT_LINE_TYPE_ID);
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
            .name(UPDATED_NAME)
            .lineTypeId(UPDATED_LINE_TYPE_ID);
        return treaty;
    }

    @BeforeEach
    public void initTest() {
        treaty = createEntity(em);
    }

    @Test
    @Transactional
    public void createTreaty() throws Exception {
        int databaseSizeBeforeCreate = treatyRepository.findAll().size();
        // Create the Treaty
        restTreatyMockMvc.perform(post("/api/treaties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(treaty)))
            .andExpect(status().isCreated());

        // Validate the Treaty in the database
        List<Treaty> treatyList = treatyRepository.findAll();
        assertThat(treatyList).hasSize(databaseSizeBeforeCreate + 1);
        Treaty testTreaty = treatyList.get(treatyList.size() - 1);
        assertThat(testTreaty.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTreaty.getLineTypeId()).isEqualTo(DEFAULT_LINE_TYPE_ID);
    }

    @Test
    @Transactional
    public void createTreatyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = treatyRepository.findAll().size();

        // Create the Treaty with an existing ID
        treaty.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTreatyMockMvc.perform(post("/api/treaties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(treaty)))
            .andExpect(status().isBadRequest());

        // Validate the Treaty in the database
        List<Treaty> treatyList = treatyRepository.findAll();
        assertThat(treatyList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTreaties() throws Exception {
        // Initialize the database
        treatyRepository.saveAndFlush(treaty);

        // Get all the treatyList
        restTreatyMockMvc.perform(get("/api/treaties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(treaty.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].lineTypeId").value(hasItem(DEFAULT_LINE_TYPE_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getTreaty() throws Exception {
        // Initialize the database
        treatyRepository.saveAndFlush(treaty);

        // Get the treaty
        restTreatyMockMvc.perform(get("/api/treaties/{id}", treaty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(treaty.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.lineTypeId").value(DEFAULT_LINE_TYPE_ID.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingTreaty() throws Exception {
        // Get the treaty
        restTreatyMockMvc.perform(get("/api/treaties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTreaty() throws Exception {
        // Initialize the database
        treatyRepository.saveAndFlush(treaty);

        int databaseSizeBeforeUpdate = treatyRepository.findAll().size();

        // Update the treaty
        Treaty updatedTreaty = treatyRepository.findById(treaty.getId()).get();
        // Disconnect from session so that the updates on updatedTreaty are not directly saved in db
        em.detach(updatedTreaty);
        updatedTreaty
            .name(UPDATED_NAME)
            .lineTypeId(UPDATED_LINE_TYPE_ID);

        restTreatyMockMvc.perform(put("/api/treaties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTreaty)))
            .andExpect(status().isOk());

        // Validate the Treaty in the database
        List<Treaty> treatyList = treatyRepository.findAll();
        assertThat(treatyList).hasSize(databaseSizeBeforeUpdate);
        Treaty testTreaty = treatyList.get(treatyList.size() - 1);
        assertThat(testTreaty.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTreaty.getLineTypeId()).isEqualTo(UPDATED_LINE_TYPE_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingTreaty() throws Exception {
        int databaseSizeBeforeUpdate = treatyRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTreatyMockMvc.perform(put("/api/treaties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(treaty)))
            .andExpect(status().isBadRequest());

        // Validate the Treaty in the database
        List<Treaty> treatyList = treatyRepository.findAll();
        assertThat(treatyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTreaty() throws Exception {
        // Initialize the database
        treatyRepository.saveAndFlush(treaty);

        int databaseSizeBeforeDelete = treatyRepository.findAll().size();

        // Delete the treaty
        restTreatyMockMvc.perform(delete("/api/treaties/{id}", treaty.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Treaty> treatyList = treatyRepository.findAll();
        assertThat(treatyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
