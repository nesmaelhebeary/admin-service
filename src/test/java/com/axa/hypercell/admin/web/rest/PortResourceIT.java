package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.Port;
import com.axa.hypercell.admin.repository.PortRepository;
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
 * Integration tests for the {@link PortResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PortResourceIT {

    private static final Long DEFAULT_COUNTRY_ID = 1L;
    private static final Long UPDATED_COUNTRY_ID = 2L;

    private static final String DEFAULT_NAME_ENGLISH = "AAAAAAAAAA";
    private static final String UPDATED_NAME_ENGLISH = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_ARABIC = "AAAAAAAAAA";
    private static final String UPDATED_NAME_ARABIC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PortRepository portRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPortMockMvc;

    private Port port;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Port createEntity(EntityManager em) {
        Port port = new Port().countryId(DEFAULT_COUNTRY_ID).nameEnglish(DEFAULT_NAME_ENGLISH).nameArabic(DEFAULT_NAME_ARABIC);
        return port;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Port createUpdatedEntity(EntityManager em) {
        Port port = new Port().countryId(UPDATED_COUNTRY_ID).nameEnglish(UPDATED_NAME_ENGLISH).nameArabic(UPDATED_NAME_ARABIC);
        return port;
    }

    @BeforeEach
    public void initTest() {
        port = createEntity(em);
    }

    @Test
    @Transactional
    void createPort() throws Exception {
        int databaseSizeBeforeCreate = portRepository.findAll().size();
        // Create the Port
        restPortMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(port)))
            .andExpect(status().isCreated());

        // Validate the Port in the database
        List<Port> portList = portRepository.findAll();
        assertThat(portList).hasSize(databaseSizeBeforeCreate + 1);
        Port testPort = portList.get(portList.size() - 1);
        assertThat(testPort.getCountryId()).isEqualTo(DEFAULT_COUNTRY_ID);
        assertThat(testPort.getNameEnglish()).isEqualTo(DEFAULT_NAME_ENGLISH);
        assertThat(testPort.getNameArabic()).isEqualTo(DEFAULT_NAME_ARABIC);
    }

    @Test
    @Transactional
    void createPortWithExistingId() throws Exception {
        // Create the Port with an existing ID
        port.setId(1L);

        int databaseSizeBeforeCreate = portRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPortMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(port)))
            .andExpect(status().isBadRequest());

        // Validate the Port in the database
        List<Port> portList = portRepository.findAll();
        assertThat(portList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPorts() throws Exception {
        // Initialize the database
        portRepository.saveAndFlush(port);

        // Get all the portList
        restPortMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(port.getId().intValue())))
            .andExpect(jsonPath("$.[*].countryId").value(hasItem(DEFAULT_COUNTRY_ID.intValue())))
            .andExpect(jsonPath("$.[*].nameEnglish").value(hasItem(DEFAULT_NAME_ENGLISH)))
            .andExpect(jsonPath("$.[*].nameArabic").value(hasItem(DEFAULT_NAME_ARABIC)));
    }

    @Test
    @Transactional
    void getPort() throws Exception {
        // Initialize the database
        portRepository.saveAndFlush(port);

        // Get the port
        restPortMockMvc
            .perform(get(ENTITY_API_URL_ID, port.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(port.getId().intValue()))
            .andExpect(jsonPath("$.countryId").value(DEFAULT_COUNTRY_ID.intValue()))
            .andExpect(jsonPath("$.nameEnglish").value(DEFAULT_NAME_ENGLISH))
            .andExpect(jsonPath("$.nameArabic").value(DEFAULT_NAME_ARABIC));
    }

    @Test
    @Transactional
    void getNonExistingPort() throws Exception {
        // Get the port
        restPortMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPort() throws Exception {
        // Initialize the database
        portRepository.saveAndFlush(port);

        int databaseSizeBeforeUpdate = portRepository.findAll().size();

        // Update the port
        Port updatedPort = portRepository.findById(port.getId()).get();
        // Disconnect from session so that the updates on updatedPort are not directly saved in db
        em.detach(updatedPort);
        updatedPort.countryId(UPDATED_COUNTRY_ID).nameEnglish(UPDATED_NAME_ENGLISH).nameArabic(UPDATED_NAME_ARABIC);

        restPortMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPort.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPort))
            )
            .andExpect(status().isOk());

        // Validate the Port in the database
        List<Port> portList = portRepository.findAll();
        assertThat(portList).hasSize(databaseSizeBeforeUpdate);
        Port testPort = portList.get(portList.size() - 1);
        assertThat(testPort.getCountryId()).isEqualTo(UPDATED_COUNTRY_ID);
        assertThat(testPort.getNameEnglish()).isEqualTo(UPDATED_NAME_ENGLISH);
        assertThat(testPort.getNameArabic()).isEqualTo(UPDATED_NAME_ARABIC);
    }

    @Test
    @Transactional
    void putNonExistingPort() throws Exception {
        int databaseSizeBeforeUpdate = portRepository.findAll().size();
        port.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPortMockMvc
            .perform(
                put(ENTITY_API_URL_ID, port.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(port))
            )
            .andExpect(status().isBadRequest());

        // Validate the Port in the database
        List<Port> portList = portRepository.findAll();
        assertThat(portList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPort() throws Exception {
        int databaseSizeBeforeUpdate = portRepository.findAll().size();
        port.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPortMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(port))
            )
            .andExpect(status().isBadRequest());

        // Validate the Port in the database
        List<Port> portList = portRepository.findAll();
        assertThat(portList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPort() throws Exception {
        int databaseSizeBeforeUpdate = portRepository.findAll().size();
        port.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPortMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(port)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Port in the database
        List<Port> portList = portRepository.findAll();
        assertThat(portList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePortWithPatch() throws Exception {
        // Initialize the database
        portRepository.saveAndFlush(port);

        int databaseSizeBeforeUpdate = portRepository.findAll().size();

        // Update the port using partial update
        Port partialUpdatedPort = new Port();
        partialUpdatedPort.setId(port.getId());

        restPortMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPort.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPort))
            )
            .andExpect(status().isOk());

        // Validate the Port in the database
        List<Port> portList = portRepository.findAll();
        assertThat(portList).hasSize(databaseSizeBeforeUpdate);
        Port testPort = portList.get(portList.size() - 1);
        assertThat(testPort.getCountryId()).isEqualTo(DEFAULT_COUNTRY_ID);
        assertThat(testPort.getNameEnglish()).isEqualTo(DEFAULT_NAME_ENGLISH);
        assertThat(testPort.getNameArabic()).isEqualTo(DEFAULT_NAME_ARABIC);
    }

    @Test
    @Transactional
    void fullUpdatePortWithPatch() throws Exception {
        // Initialize the database
        portRepository.saveAndFlush(port);

        int databaseSizeBeforeUpdate = portRepository.findAll().size();

        // Update the port using partial update
        Port partialUpdatedPort = new Port();
        partialUpdatedPort.setId(port.getId());

        partialUpdatedPort.countryId(UPDATED_COUNTRY_ID).nameEnglish(UPDATED_NAME_ENGLISH).nameArabic(UPDATED_NAME_ARABIC);

        restPortMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPort.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPort))
            )
            .andExpect(status().isOk());

        // Validate the Port in the database
        List<Port> portList = portRepository.findAll();
        assertThat(portList).hasSize(databaseSizeBeforeUpdate);
        Port testPort = portList.get(portList.size() - 1);
        assertThat(testPort.getCountryId()).isEqualTo(UPDATED_COUNTRY_ID);
        assertThat(testPort.getNameEnglish()).isEqualTo(UPDATED_NAME_ENGLISH);
        assertThat(testPort.getNameArabic()).isEqualTo(UPDATED_NAME_ARABIC);
    }

    @Test
    @Transactional
    void patchNonExistingPort() throws Exception {
        int databaseSizeBeforeUpdate = portRepository.findAll().size();
        port.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPortMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, port.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(port))
            )
            .andExpect(status().isBadRequest());

        // Validate the Port in the database
        List<Port> portList = portRepository.findAll();
        assertThat(portList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPort() throws Exception {
        int databaseSizeBeforeUpdate = portRepository.findAll().size();
        port.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPortMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(port))
            )
            .andExpect(status().isBadRequest());

        // Validate the Port in the database
        List<Port> portList = portRepository.findAll();
        assertThat(portList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPort() throws Exception {
        int databaseSizeBeforeUpdate = portRepository.findAll().size();
        port.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPortMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(port)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Port in the database
        List<Port> portList = portRepository.findAll();
        assertThat(portList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePort() throws Exception {
        // Initialize the database
        portRepository.saveAndFlush(port);

        int databaseSizeBeforeDelete = portRepository.findAll().size();

        // Delete the port
        restPortMockMvc
            .perform(delete(ENTITY_API_URL_ID, port.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Port> portList = portRepository.findAll();
        assertThat(portList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
