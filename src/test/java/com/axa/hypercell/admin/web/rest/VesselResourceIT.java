package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.Vessel;
import com.axa.hypercell.admin.repository.VesselRepository;
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
 * Integration tests for the {@link VesselResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VesselResourceIT {

    private static final String DEFAULT_NAME_EN = "AAAAAAAAAA";
    private static final String UPDATED_NAME_EN = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_AR = "AAAAAAAAAA";
    private static final String UPDATED_NAME_AR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vessels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VesselRepository vesselRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVesselMockMvc;

    private Vessel vessel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vessel createEntity(EntityManager em) {
        Vessel vessel = new Vessel().nameEn(DEFAULT_NAME_EN).nameAr(DEFAULT_NAME_AR);
        return vessel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vessel createUpdatedEntity(EntityManager em) {
        Vessel vessel = new Vessel().nameEn(UPDATED_NAME_EN).nameAr(UPDATED_NAME_AR);
        return vessel;
    }

    @BeforeEach
    public void initTest() {
        vessel = createEntity(em);
    }

    @Test
    @Transactional
    void createVessel() throws Exception {
        int databaseSizeBeforeCreate = vesselRepository.findAll().size();
        // Create the Vessel
        restVesselMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vessel)))
            .andExpect(status().isCreated());

        // Validate the Vessel in the database
        List<Vessel> vesselList = vesselRepository.findAll();
        assertThat(vesselList).hasSize(databaseSizeBeforeCreate + 1);
        Vessel testVessel = vesselList.get(vesselList.size() - 1);
        assertThat(testVessel.getNameEn()).isEqualTo(DEFAULT_NAME_EN);
        assertThat(testVessel.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
    }

    @Test
    @Transactional
    void createVesselWithExistingId() throws Exception {
        // Create the Vessel with an existing ID
        vessel.setId(1L);

        int databaseSizeBeforeCreate = vesselRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVesselMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vessel)))
            .andExpect(status().isBadRequest());

        // Validate the Vessel in the database
        List<Vessel> vesselList = vesselRepository.findAll();
        assertThat(vesselList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVessels() throws Exception {
        // Initialize the database
        vesselRepository.saveAndFlush(vessel);

        // Get all the vesselList
        restVesselMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vessel.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)));
    }

    @Test
    @Transactional
    void getVessel() throws Exception {
        // Initialize the database
        vesselRepository.saveAndFlush(vessel);

        // Get the vessel
        restVesselMockMvc
            .perform(get(ENTITY_API_URL_ID, vessel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vessel.getId().intValue()))
            .andExpect(jsonPath("$.nameEn").value(DEFAULT_NAME_EN))
            .andExpect(jsonPath("$.nameAr").value(DEFAULT_NAME_AR));
    }

    @Test
    @Transactional
    void getNonExistingVessel() throws Exception {
        // Get the vessel
        restVesselMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVessel() throws Exception {
        // Initialize the database
        vesselRepository.saveAndFlush(vessel);

        int databaseSizeBeforeUpdate = vesselRepository.findAll().size();

        // Update the vessel
        Vessel updatedVessel = vesselRepository.findById(vessel.getId()).get();
        // Disconnect from session so that the updates on updatedVessel are not directly saved in db
        em.detach(updatedVessel);
        updatedVessel.nameEn(UPDATED_NAME_EN).nameAr(UPDATED_NAME_AR);

        restVesselMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVessel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVessel))
            )
            .andExpect(status().isOk());

        // Validate the Vessel in the database
        List<Vessel> vesselList = vesselRepository.findAll();
        assertThat(vesselList).hasSize(databaseSizeBeforeUpdate);
        Vessel testVessel = vesselList.get(vesselList.size() - 1);
        assertThat(testVessel.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testVessel.getNameAr()).isEqualTo(UPDATED_NAME_AR);
    }

    @Test
    @Transactional
    void putNonExistingVessel() throws Exception {
        int databaseSizeBeforeUpdate = vesselRepository.findAll().size();
        vessel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVesselMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vessel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vessel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vessel in the database
        List<Vessel> vesselList = vesselRepository.findAll();
        assertThat(vesselList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVessel() throws Exception {
        int databaseSizeBeforeUpdate = vesselRepository.findAll().size();
        vessel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVesselMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vessel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vessel in the database
        List<Vessel> vesselList = vesselRepository.findAll();
        assertThat(vesselList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVessel() throws Exception {
        int databaseSizeBeforeUpdate = vesselRepository.findAll().size();
        vessel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVesselMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vessel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vessel in the database
        List<Vessel> vesselList = vesselRepository.findAll();
        assertThat(vesselList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVesselWithPatch() throws Exception {
        // Initialize the database
        vesselRepository.saveAndFlush(vessel);

        int databaseSizeBeforeUpdate = vesselRepository.findAll().size();

        // Update the vessel using partial update
        Vessel partialUpdatedVessel = new Vessel();
        partialUpdatedVessel.setId(vessel.getId());

        partialUpdatedVessel.nameAr(UPDATED_NAME_AR);

        restVesselMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVessel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVessel))
            )
            .andExpect(status().isOk());

        // Validate the Vessel in the database
        List<Vessel> vesselList = vesselRepository.findAll();
        assertThat(vesselList).hasSize(databaseSizeBeforeUpdate);
        Vessel testVessel = vesselList.get(vesselList.size() - 1);
        assertThat(testVessel.getNameEn()).isEqualTo(DEFAULT_NAME_EN);
        assertThat(testVessel.getNameAr()).isEqualTo(UPDATED_NAME_AR);
    }

    @Test
    @Transactional
    void fullUpdateVesselWithPatch() throws Exception {
        // Initialize the database
        vesselRepository.saveAndFlush(vessel);

        int databaseSizeBeforeUpdate = vesselRepository.findAll().size();

        // Update the vessel using partial update
        Vessel partialUpdatedVessel = new Vessel();
        partialUpdatedVessel.setId(vessel.getId());

        partialUpdatedVessel.nameEn(UPDATED_NAME_EN).nameAr(UPDATED_NAME_AR);

        restVesselMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVessel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVessel))
            )
            .andExpect(status().isOk());

        // Validate the Vessel in the database
        List<Vessel> vesselList = vesselRepository.findAll();
        assertThat(vesselList).hasSize(databaseSizeBeforeUpdate);
        Vessel testVessel = vesselList.get(vesselList.size() - 1);
        assertThat(testVessel.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testVessel.getNameAr()).isEqualTo(UPDATED_NAME_AR);
    }

    @Test
    @Transactional
    void patchNonExistingVessel() throws Exception {
        int databaseSizeBeforeUpdate = vesselRepository.findAll().size();
        vessel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVesselMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vessel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vessel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vessel in the database
        List<Vessel> vesselList = vesselRepository.findAll();
        assertThat(vesselList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVessel() throws Exception {
        int databaseSizeBeforeUpdate = vesselRepository.findAll().size();
        vessel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVesselMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vessel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vessel in the database
        List<Vessel> vesselList = vesselRepository.findAll();
        assertThat(vesselList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVessel() throws Exception {
        int databaseSizeBeforeUpdate = vesselRepository.findAll().size();
        vessel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVesselMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vessel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vessel in the database
        List<Vessel> vesselList = vesselRepository.findAll();
        assertThat(vesselList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVessel() throws Exception {
        // Initialize the database
        vesselRepository.saveAndFlush(vessel);

        int databaseSizeBeforeDelete = vesselRepository.findAll().size();

        // Delete the vessel
        restVesselMockMvc
            .perform(delete(ENTITY_API_URL_ID, vessel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vessel> vesselList = vesselRepository.findAll();
        assertThat(vesselList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
