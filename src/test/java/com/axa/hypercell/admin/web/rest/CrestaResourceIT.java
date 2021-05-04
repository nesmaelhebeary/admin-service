package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.Cresta;
import com.axa.hypercell.admin.repository.CrestaRepository;
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
 * Integration tests for the {@link CrestaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CrestaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/crestas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

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
        Cresta cresta = new Cresta().name(DEFAULT_NAME).shortName(DEFAULT_SHORT_NAME).code(DEFAULT_CODE);
        return cresta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cresta createUpdatedEntity(EntityManager em) {
        Cresta cresta = new Cresta().name(UPDATED_NAME).shortName(UPDATED_SHORT_NAME).code(UPDATED_CODE);
        return cresta;
    }

    @BeforeEach
    public void initTest() {
        cresta = createEntity(em);
    }

    @Test
    @Transactional
    void createCresta() throws Exception {
        int databaseSizeBeforeCreate = crestaRepository.findAll().size();
        // Create the Cresta
        restCrestaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cresta)))
            .andExpect(status().isCreated());

        // Validate the Cresta in the database
        List<Cresta> crestaList = crestaRepository.findAll();
        assertThat(crestaList).hasSize(databaseSizeBeforeCreate + 1);
        Cresta testCresta = crestaList.get(crestaList.size() - 1);
        assertThat(testCresta.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCresta.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testCresta.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createCrestaWithExistingId() throws Exception {
        // Create the Cresta with an existing ID
        cresta.setId(1L);

        int databaseSizeBeforeCreate = crestaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrestaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cresta)))
            .andExpect(status().isBadRequest());

        // Validate the Cresta in the database
        List<Cresta> crestaList = crestaRepository.findAll();
        assertThat(crestaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCrestas() throws Exception {
        // Initialize the database
        crestaRepository.saveAndFlush(cresta);

        // Get all the crestaList
        restCrestaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cresta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getCresta() throws Exception {
        // Initialize the database
        crestaRepository.saveAndFlush(cresta);

        // Get the cresta
        restCrestaMockMvc
            .perform(get(ENTITY_API_URL_ID, cresta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cresta.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getNonExistingCresta() throws Exception {
        // Get the cresta
        restCrestaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCresta() throws Exception {
        // Initialize the database
        crestaRepository.saveAndFlush(cresta);

        int databaseSizeBeforeUpdate = crestaRepository.findAll().size();

        // Update the cresta
        Cresta updatedCresta = crestaRepository.findById(cresta.getId()).get();
        // Disconnect from session so that the updates on updatedCresta are not directly saved in db
        em.detach(updatedCresta);
        updatedCresta.name(UPDATED_NAME).shortName(UPDATED_SHORT_NAME).code(UPDATED_CODE);

        restCrestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCresta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCresta))
            )
            .andExpect(status().isOk());

        // Validate the Cresta in the database
        List<Cresta> crestaList = crestaRepository.findAll();
        assertThat(crestaList).hasSize(databaseSizeBeforeUpdate);
        Cresta testCresta = crestaList.get(crestaList.size() - 1);
        assertThat(testCresta.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCresta.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testCresta.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingCresta() throws Exception {
        int databaseSizeBeforeUpdate = crestaRepository.findAll().size();
        cresta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cresta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cresta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cresta in the database
        List<Cresta> crestaList = crestaRepository.findAll();
        assertThat(crestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCresta() throws Exception {
        int databaseSizeBeforeUpdate = crestaRepository.findAll().size();
        cresta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cresta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cresta in the database
        List<Cresta> crestaList = crestaRepository.findAll();
        assertThat(crestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCresta() throws Exception {
        int databaseSizeBeforeUpdate = crestaRepository.findAll().size();
        cresta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrestaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cresta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cresta in the database
        List<Cresta> crestaList = crestaRepository.findAll();
        assertThat(crestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCrestaWithPatch() throws Exception {
        // Initialize the database
        crestaRepository.saveAndFlush(cresta);

        int databaseSizeBeforeUpdate = crestaRepository.findAll().size();

        // Update the cresta using partial update
        Cresta partialUpdatedCresta = new Cresta();
        partialUpdatedCresta.setId(cresta.getId());

        partialUpdatedCresta.shortName(UPDATED_SHORT_NAME).code(UPDATED_CODE);

        restCrestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCresta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCresta))
            )
            .andExpect(status().isOk());

        // Validate the Cresta in the database
        List<Cresta> crestaList = crestaRepository.findAll();
        assertThat(crestaList).hasSize(databaseSizeBeforeUpdate);
        Cresta testCresta = crestaList.get(crestaList.size() - 1);
        assertThat(testCresta.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCresta.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testCresta.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void fullUpdateCrestaWithPatch() throws Exception {
        // Initialize the database
        crestaRepository.saveAndFlush(cresta);

        int databaseSizeBeforeUpdate = crestaRepository.findAll().size();

        // Update the cresta using partial update
        Cresta partialUpdatedCresta = new Cresta();
        partialUpdatedCresta.setId(cresta.getId());

        partialUpdatedCresta.name(UPDATED_NAME).shortName(UPDATED_SHORT_NAME).code(UPDATED_CODE);

        restCrestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCresta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCresta))
            )
            .andExpect(status().isOk());

        // Validate the Cresta in the database
        List<Cresta> crestaList = crestaRepository.findAll();
        assertThat(crestaList).hasSize(databaseSizeBeforeUpdate);
        Cresta testCresta = crestaList.get(crestaList.size() - 1);
        assertThat(testCresta.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCresta.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testCresta.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingCresta() throws Exception {
        int databaseSizeBeforeUpdate = crestaRepository.findAll().size();
        cresta.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cresta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cresta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cresta in the database
        List<Cresta> crestaList = crestaRepository.findAll();
        assertThat(crestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCresta() throws Exception {
        int databaseSizeBeforeUpdate = crestaRepository.findAll().size();
        cresta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cresta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cresta in the database
        List<Cresta> crestaList = crestaRepository.findAll();
        assertThat(crestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCresta() throws Exception {
        int databaseSizeBeforeUpdate = crestaRepository.findAll().size();
        cresta.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrestaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cresta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cresta in the database
        List<Cresta> crestaList = crestaRepository.findAll();
        assertThat(crestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCresta() throws Exception {
        // Initialize the database
        crestaRepository.saveAndFlush(cresta);

        int databaseSizeBeforeDelete = crestaRepository.findAll().size();

        // Delete the cresta
        restCrestaMockMvc
            .perform(delete(ENTITY_API_URL_ID, cresta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cresta> crestaList = crestaRepository.findAll();
        assertThat(crestaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
