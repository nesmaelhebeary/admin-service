package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.Occupancy;
import com.axa.hypercell.admin.repository.OccupancyRepository;
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
 * Integration tests for the {@link OccupancyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OccupancyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LINE_TYPE_ID = "AAAAAAAAAA";
    private static final String UPDATED_LINE_TYPE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_LINE_TYPE_OCCUPANCY = "AAAAAAAAAA";
    private static final String UPDATED_LINE_TYPE_OCCUPANCY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/occupancies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

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
            .name(DEFAULT_NAME)
            .shortName(DEFAULT_SHORT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .lineTypeId(DEFAULT_LINE_TYPE_ID)
            .lineTypeOccupancy(DEFAULT_LINE_TYPE_OCCUPANCY);
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
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .description(UPDATED_DESCRIPTION)
            .lineTypeId(UPDATED_LINE_TYPE_ID)
            .lineTypeOccupancy(UPDATED_LINE_TYPE_OCCUPANCY);
        return occupancy;
    }

    @BeforeEach
    public void initTest() {
        occupancy = createEntity(em);
    }

    @Test
    @Transactional
    void createOccupancy() throws Exception {
        int databaseSizeBeforeCreate = occupancyRepository.findAll().size();
        // Create the Occupancy
        restOccupancyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(occupancy)))
            .andExpect(status().isCreated());

        // Validate the Occupancy in the database
        List<Occupancy> occupancyList = occupancyRepository.findAll();
        assertThat(occupancyList).hasSize(databaseSizeBeforeCreate + 1);
        Occupancy testOccupancy = occupancyList.get(occupancyList.size() - 1);
        assertThat(testOccupancy.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOccupancy.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testOccupancy.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOccupancy.getLineTypeId()).isEqualTo(DEFAULT_LINE_TYPE_ID);
        assertThat(testOccupancy.getLineTypeOccupancy()).isEqualTo(DEFAULT_LINE_TYPE_OCCUPANCY);
    }

    @Test
    @Transactional
    void createOccupancyWithExistingId() throws Exception {
        // Create the Occupancy with an existing ID
        occupancy.setId(1L);

        int databaseSizeBeforeCreate = occupancyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOccupancyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(occupancy)))
            .andExpect(status().isBadRequest());

        // Validate the Occupancy in the database
        List<Occupancy> occupancyList = occupancyRepository.findAll();
        assertThat(occupancyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOccupancies() throws Exception {
        // Initialize the database
        occupancyRepository.saveAndFlush(occupancy);

        // Get all the occupancyList
        restOccupancyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(occupancy.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].lineTypeId").value(hasItem(DEFAULT_LINE_TYPE_ID)))
            .andExpect(jsonPath("$.[*].lineTypeOccupancy").value(hasItem(DEFAULT_LINE_TYPE_OCCUPANCY)));
    }

    @Test
    @Transactional
    void getOccupancy() throws Exception {
        // Initialize the database
        occupancyRepository.saveAndFlush(occupancy);

        // Get the occupancy
        restOccupancyMockMvc
            .perform(get(ENTITY_API_URL_ID, occupancy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(occupancy.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.lineTypeId").value(DEFAULT_LINE_TYPE_ID))
            .andExpect(jsonPath("$.lineTypeOccupancy").value(DEFAULT_LINE_TYPE_OCCUPANCY));
    }

    @Test
    @Transactional
    void getNonExistingOccupancy() throws Exception {
        // Get the occupancy
        restOccupancyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOccupancy() throws Exception {
        // Initialize the database
        occupancyRepository.saveAndFlush(occupancy);

        int databaseSizeBeforeUpdate = occupancyRepository.findAll().size();

        // Update the occupancy
        Occupancy updatedOccupancy = occupancyRepository.findById(occupancy.getId()).get();
        // Disconnect from session so that the updates on updatedOccupancy are not directly saved in db
        em.detach(updatedOccupancy);
        updatedOccupancy
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .description(UPDATED_DESCRIPTION)
            .lineTypeId(UPDATED_LINE_TYPE_ID)
            .lineTypeOccupancy(UPDATED_LINE_TYPE_OCCUPANCY);

        restOccupancyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOccupancy.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOccupancy))
            )
            .andExpect(status().isOk());

        // Validate the Occupancy in the database
        List<Occupancy> occupancyList = occupancyRepository.findAll();
        assertThat(occupancyList).hasSize(databaseSizeBeforeUpdate);
        Occupancy testOccupancy = occupancyList.get(occupancyList.size() - 1);
        assertThat(testOccupancy.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOccupancy.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testOccupancy.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOccupancy.getLineTypeId()).isEqualTo(UPDATED_LINE_TYPE_ID);
        assertThat(testOccupancy.getLineTypeOccupancy()).isEqualTo(UPDATED_LINE_TYPE_OCCUPANCY);
    }

    @Test
    @Transactional
    void putNonExistingOccupancy() throws Exception {
        int databaseSizeBeforeUpdate = occupancyRepository.findAll().size();
        occupancy.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOccupancyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, occupancy.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(occupancy))
            )
            .andExpect(status().isBadRequest());

        // Validate the Occupancy in the database
        List<Occupancy> occupancyList = occupancyRepository.findAll();
        assertThat(occupancyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOccupancy() throws Exception {
        int databaseSizeBeforeUpdate = occupancyRepository.findAll().size();
        occupancy.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOccupancyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(occupancy))
            )
            .andExpect(status().isBadRequest());

        // Validate the Occupancy in the database
        List<Occupancy> occupancyList = occupancyRepository.findAll();
        assertThat(occupancyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOccupancy() throws Exception {
        int databaseSizeBeforeUpdate = occupancyRepository.findAll().size();
        occupancy.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOccupancyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(occupancy)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Occupancy in the database
        List<Occupancy> occupancyList = occupancyRepository.findAll();
        assertThat(occupancyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOccupancyWithPatch() throws Exception {
        // Initialize the database
        occupancyRepository.saveAndFlush(occupancy);

        int databaseSizeBeforeUpdate = occupancyRepository.findAll().size();

        // Update the occupancy using partial update
        Occupancy partialUpdatedOccupancy = new Occupancy();
        partialUpdatedOccupancy.setId(occupancy.getId());

        restOccupancyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOccupancy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOccupancy))
            )
            .andExpect(status().isOk());

        // Validate the Occupancy in the database
        List<Occupancy> occupancyList = occupancyRepository.findAll();
        assertThat(occupancyList).hasSize(databaseSizeBeforeUpdate);
        Occupancy testOccupancy = occupancyList.get(occupancyList.size() - 1);
        assertThat(testOccupancy.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOccupancy.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testOccupancy.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOccupancy.getLineTypeId()).isEqualTo(DEFAULT_LINE_TYPE_ID);
        assertThat(testOccupancy.getLineTypeOccupancy()).isEqualTo(DEFAULT_LINE_TYPE_OCCUPANCY);
    }

    @Test
    @Transactional
    void fullUpdateOccupancyWithPatch() throws Exception {
        // Initialize the database
        occupancyRepository.saveAndFlush(occupancy);

        int databaseSizeBeforeUpdate = occupancyRepository.findAll().size();

        // Update the occupancy using partial update
        Occupancy partialUpdatedOccupancy = new Occupancy();
        partialUpdatedOccupancy.setId(occupancy.getId());

        partialUpdatedOccupancy
            .name(UPDATED_NAME)
            .shortName(UPDATED_SHORT_NAME)
            .description(UPDATED_DESCRIPTION)
            .lineTypeId(UPDATED_LINE_TYPE_ID)
            .lineTypeOccupancy(UPDATED_LINE_TYPE_OCCUPANCY);

        restOccupancyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOccupancy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOccupancy))
            )
            .andExpect(status().isOk());

        // Validate the Occupancy in the database
        List<Occupancy> occupancyList = occupancyRepository.findAll();
        assertThat(occupancyList).hasSize(databaseSizeBeforeUpdate);
        Occupancy testOccupancy = occupancyList.get(occupancyList.size() - 1);
        assertThat(testOccupancy.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOccupancy.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testOccupancy.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOccupancy.getLineTypeId()).isEqualTo(UPDATED_LINE_TYPE_ID);
        assertThat(testOccupancy.getLineTypeOccupancy()).isEqualTo(UPDATED_LINE_TYPE_OCCUPANCY);
    }

    @Test
    @Transactional
    void patchNonExistingOccupancy() throws Exception {
        int databaseSizeBeforeUpdate = occupancyRepository.findAll().size();
        occupancy.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOccupancyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, occupancy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(occupancy))
            )
            .andExpect(status().isBadRequest());

        // Validate the Occupancy in the database
        List<Occupancy> occupancyList = occupancyRepository.findAll();
        assertThat(occupancyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOccupancy() throws Exception {
        int databaseSizeBeforeUpdate = occupancyRepository.findAll().size();
        occupancy.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOccupancyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(occupancy))
            )
            .andExpect(status().isBadRequest());

        // Validate the Occupancy in the database
        List<Occupancy> occupancyList = occupancyRepository.findAll();
        assertThat(occupancyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOccupancy() throws Exception {
        int databaseSizeBeforeUpdate = occupancyRepository.findAll().size();
        occupancy.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOccupancyMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(occupancy))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Occupancy in the database
        List<Occupancy> occupancyList = occupancyRepository.findAll();
        assertThat(occupancyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOccupancy() throws Exception {
        // Initialize the database
        occupancyRepository.saveAndFlush(occupancy);

        int databaseSizeBeforeDelete = occupancyRepository.findAll().size();

        // Delete the occupancy
        restOccupancyMockMvc
            .perform(delete(ENTITY_API_URL_ID, occupancy.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Occupancy> occupancyList = occupancyRepository.findAll();
        assertThat(occupancyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
