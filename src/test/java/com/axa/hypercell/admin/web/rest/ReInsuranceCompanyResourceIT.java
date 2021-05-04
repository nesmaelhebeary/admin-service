package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.ReInsuranceCompany;
import com.axa.hypercell.admin.repository.ReInsuranceCompanyRepository;
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
 * Integration tests for the {@link ReInsuranceCompanyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReInsuranceCompanyResourceIT {

    private static final String DEFAULT_NAME_EN = "AAAAAAAAAA";
    private static final String UPDATED_NAME_EN = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_AR = "AAAAAAAAAA";
    private static final String UPDATED_NAME_AR = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_DISTRICT = "AAAAAAAAAA";
    private static final String UPDATED_DISTRICT = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/re-insurance-companies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReInsuranceCompanyRepository reInsuranceCompanyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReInsuranceCompanyMockMvc;

    private ReInsuranceCompany reInsuranceCompany;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReInsuranceCompany createEntity(EntityManager em) {
        ReInsuranceCompany reInsuranceCompany = new ReInsuranceCompany()
            .nameEn(DEFAULT_NAME_EN)
            .nameAr(DEFAULT_NAME_AR)
            .address(DEFAULT_ADDRESS)
            .district(DEFAULT_DISTRICT)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY);
        return reInsuranceCompany;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReInsuranceCompany createUpdatedEntity(EntityManager em) {
        ReInsuranceCompany reInsuranceCompany = new ReInsuranceCompany()
            .nameEn(UPDATED_NAME_EN)
            .nameAr(UPDATED_NAME_AR)
            .address(UPDATED_ADDRESS)
            .district(UPDATED_DISTRICT)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY);
        return reInsuranceCompany;
    }

    @BeforeEach
    public void initTest() {
        reInsuranceCompany = createEntity(em);
    }

    @Test
    @Transactional
    void createReInsuranceCompany() throws Exception {
        int databaseSizeBeforeCreate = reInsuranceCompanyRepository.findAll().size();
        // Create the ReInsuranceCompany
        restReInsuranceCompanyMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reInsuranceCompany))
            )
            .andExpect(status().isCreated());

        // Validate the ReInsuranceCompany in the database
        List<ReInsuranceCompany> reInsuranceCompanyList = reInsuranceCompanyRepository.findAll();
        assertThat(reInsuranceCompanyList).hasSize(databaseSizeBeforeCreate + 1);
        ReInsuranceCompany testReInsuranceCompany = reInsuranceCompanyList.get(reInsuranceCompanyList.size() - 1);
        assertThat(testReInsuranceCompany.getNameEn()).isEqualTo(DEFAULT_NAME_EN);
        assertThat(testReInsuranceCompany.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
        assertThat(testReInsuranceCompany.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testReInsuranceCompany.getDistrict()).isEqualTo(DEFAULT_DISTRICT);
        assertThat(testReInsuranceCompany.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testReInsuranceCompany.getCountry()).isEqualTo(DEFAULT_COUNTRY);
    }

    @Test
    @Transactional
    void createReInsuranceCompanyWithExistingId() throws Exception {
        // Create the ReInsuranceCompany with an existing ID
        reInsuranceCompany.setId(1L);

        int databaseSizeBeforeCreate = reInsuranceCompanyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReInsuranceCompanyMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reInsuranceCompany))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReInsuranceCompany in the database
        List<ReInsuranceCompany> reInsuranceCompanyList = reInsuranceCompanyRepository.findAll();
        assertThat(reInsuranceCompanyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReInsuranceCompanies() throws Exception {
        // Initialize the database
        reInsuranceCompanyRepository.saveAndFlush(reInsuranceCompany);

        // Get all the reInsuranceCompanyList
        restReInsuranceCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reInsuranceCompany.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].district").value(hasItem(DEFAULT_DISTRICT)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)));
    }

    @Test
    @Transactional
    void getReInsuranceCompany() throws Exception {
        // Initialize the database
        reInsuranceCompanyRepository.saveAndFlush(reInsuranceCompany);

        // Get the reInsuranceCompany
        restReInsuranceCompanyMockMvc
            .perform(get(ENTITY_API_URL_ID, reInsuranceCompany.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reInsuranceCompany.getId().intValue()))
            .andExpect(jsonPath("$.nameEn").value(DEFAULT_NAME_EN))
            .andExpect(jsonPath("$.nameAr").value(DEFAULT_NAME_AR))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.district").value(DEFAULT_DISTRICT))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY));
    }

    @Test
    @Transactional
    void getNonExistingReInsuranceCompany() throws Exception {
        // Get the reInsuranceCompany
        restReInsuranceCompanyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReInsuranceCompany() throws Exception {
        // Initialize the database
        reInsuranceCompanyRepository.saveAndFlush(reInsuranceCompany);

        int databaseSizeBeforeUpdate = reInsuranceCompanyRepository.findAll().size();

        // Update the reInsuranceCompany
        ReInsuranceCompany updatedReInsuranceCompany = reInsuranceCompanyRepository.findById(reInsuranceCompany.getId()).get();
        // Disconnect from session so that the updates on updatedReInsuranceCompany are not directly saved in db
        em.detach(updatedReInsuranceCompany);
        updatedReInsuranceCompany
            .nameEn(UPDATED_NAME_EN)
            .nameAr(UPDATED_NAME_AR)
            .address(UPDATED_ADDRESS)
            .district(UPDATED_DISTRICT)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY);

        restReInsuranceCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReInsuranceCompany.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReInsuranceCompany))
            )
            .andExpect(status().isOk());

        // Validate the ReInsuranceCompany in the database
        List<ReInsuranceCompany> reInsuranceCompanyList = reInsuranceCompanyRepository.findAll();
        assertThat(reInsuranceCompanyList).hasSize(databaseSizeBeforeUpdate);
        ReInsuranceCompany testReInsuranceCompany = reInsuranceCompanyList.get(reInsuranceCompanyList.size() - 1);
        assertThat(testReInsuranceCompany.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testReInsuranceCompany.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testReInsuranceCompany.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testReInsuranceCompany.getDistrict()).isEqualTo(UPDATED_DISTRICT);
        assertThat(testReInsuranceCompany.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testReInsuranceCompany.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void putNonExistingReInsuranceCompany() throws Exception {
        int databaseSizeBeforeUpdate = reInsuranceCompanyRepository.findAll().size();
        reInsuranceCompany.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReInsuranceCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reInsuranceCompany.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reInsuranceCompany))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReInsuranceCompany in the database
        List<ReInsuranceCompany> reInsuranceCompanyList = reInsuranceCompanyRepository.findAll();
        assertThat(reInsuranceCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReInsuranceCompany() throws Exception {
        int databaseSizeBeforeUpdate = reInsuranceCompanyRepository.findAll().size();
        reInsuranceCompany.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReInsuranceCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reInsuranceCompany))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReInsuranceCompany in the database
        List<ReInsuranceCompany> reInsuranceCompanyList = reInsuranceCompanyRepository.findAll();
        assertThat(reInsuranceCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReInsuranceCompany() throws Exception {
        int databaseSizeBeforeUpdate = reInsuranceCompanyRepository.findAll().size();
        reInsuranceCompany.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReInsuranceCompanyMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reInsuranceCompany))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReInsuranceCompany in the database
        List<ReInsuranceCompany> reInsuranceCompanyList = reInsuranceCompanyRepository.findAll();
        assertThat(reInsuranceCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReInsuranceCompanyWithPatch() throws Exception {
        // Initialize the database
        reInsuranceCompanyRepository.saveAndFlush(reInsuranceCompany);

        int databaseSizeBeforeUpdate = reInsuranceCompanyRepository.findAll().size();

        // Update the reInsuranceCompany using partial update
        ReInsuranceCompany partialUpdatedReInsuranceCompany = new ReInsuranceCompany();
        partialUpdatedReInsuranceCompany.setId(reInsuranceCompany.getId());

        partialUpdatedReInsuranceCompany
            .nameEn(UPDATED_NAME_EN)
            .nameAr(UPDATED_NAME_AR)
            .address(UPDATED_ADDRESS)
            .district(UPDATED_DISTRICT);

        restReInsuranceCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReInsuranceCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReInsuranceCompany))
            )
            .andExpect(status().isOk());

        // Validate the ReInsuranceCompany in the database
        List<ReInsuranceCompany> reInsuranceCompanyList = reInsuranceCompanyRepository.findAll();
        assertThat(reInsuranceCompanyList).hasSize(databaseSizeBeforeUpdate);
        ReInsuranceCompany testReInsuranceCompany = reInsuranceCompanyList.get(reInsuranceCompanyList.size() - 1);
        assertThat(testReInsuranceCompany.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testReInsuranceCompany.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testReInsuranceCompany.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testReInsuranceCompany.getDistrict()).isEqualTo(UPDATED_DISTRICT);
        assertThat(testReInsuranceCompany.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testReInsuranceCompany.getCountry()).isEqualTo(DEFAULT_COUNTRY);
    }

    @Test
    @Transactional
    void fullUpdateReInsuranceCompanyWithPatch() throws Exception {
        // Initialize the database
        reInsuranceCompanyRepository.saveAndFlush(reInsuranceCompany);

        int databaseSizeBeforeUpdate = reInsuranceCompanyRepository.findAll().size();

        // Update the reInsuranceCompany using partial update
        ReInsuranceCompany partialUpdatedReInsuranceCompany = new ReInsuranceCompany();
        partialUpdatedReInsuranceCompany.setId(reInsuranceCompany.getId());

        partialUpdatedReInsuranceCompany
            .nameEn(UPDATED_NAME_EN)
            .nameAr(UPDATED_NAME_AR)
            .address(UPDATED_ADDRESS)
            .district(UPDATED_DISTRICT)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY);

        restReInsuranceCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReInsuranceCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReInsuranceCompany))
            )
            .andExpect(status().isOk());

        // Validate the ReInsuranceCompany in the database
        List<ReInsuranceCompany> reInsuranceCompanyList = reInsuranceCompanyRepository.findAll();
        assertThat(reInsuranceCompanyList).hasSize(databaseSizeBeforeUpdate);
        ReInsuranceCompany testReInsuranceCompany = reInsuranceCompanyList.get(reInsuranceCompanyList.size() - 1);
        assertThat(testReInsuranceCompany.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testReInsuranceCompany.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testReInsuranceCompany.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testReInsuranceCompany.getDistrict()).isEqualTo(UPDATED_DISTRICT);
        assertThat(testReInsuranceCompany.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testReInsuranceCompany.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void patchNonExistingReInsuranceCompany() throws Exception {
        int databaseSizeBeforeUpdate = reInsuranceCompanyRepository.findAll().size();
        reInsuranceCompany.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReInsuranceCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reInsuranceCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reInsuranceCompany))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReInsuranceCompany in the database
        List<ReInsuranceCompany> reInsuranceCompanyList = reInsuranceCompanyRepository.findAll();
        assertThat(reInsuranceCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReInsuranceCompany() throws Exception {
        int databaseSizeBeforeUpdate = reInsuranceCompanyRepository.findAll().size();
        reInsuranceCompany.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReInsuranceCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reInsuranceCompany))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReInsuranceCompany in the database
        List<ReInsuranceCompany> reInsuranceCompanyList = reInsuranceCompanyRepository.findAll();
        assertThat(reInsuranceCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReInsuranceCompany() throws Exception {
        int databaseSizeBeforeUpdate = reInsuranceCompanyRepository.findAll().size();
        reInsuranceCompany.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReInsuranceCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reInsuranceCompany))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReInsuranceCompany in the database
        List<ReInsuranceCompany> reInsuranceCompanyList = reInsuranceCompanyRepository.findAll();
        assertThat(reInsuranceCompanyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReInsuranceCompany() throws Exception {
        // Initialize the database
        reInsuranceCompanyRepository.saveAndFlush(reInsuranceCompany);

        int databaseSizeBeforeDelete = reInsuranceCompanyRepository.findAll().size();

        // Delete the reInsuranceCompany
        restReInsuranceCompanyMockMvc
            .perform(delete(ENTITY_API_URL_ID, reInsuranceCompany.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReInsuranceCompany> reInsuranceCompanyList = reInsuranceCompanyRepository.findAll();
        assertThat(reInsuranceCompanyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
