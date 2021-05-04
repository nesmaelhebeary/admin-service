package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.Commodity;
import com.axa.hypercell.admin.repository.CommodityRepository;
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
 * Integration tests for the {@link CommodityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommodityResourceIT {

    private static final String DEFAULT_NAME_ENGLISH = "AAAAAAAAAA";
    private static final String UPDATED_NAME_ENGLISH = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_ARABIC = "AAAAAAAAAA";
    private static final String UPDATED_NAME_ARABIC = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/commodities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommodityRepository commodityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommodityMockMvc;

    private Commodity commodity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commodity createEntity(EntityManager em) {
        Commodity commodity = new Commodity().nameEnglish(DEFAULT_NAME_ENGLISH).nameArabic(DEFAULT_NAME_ARABIC);
        return commodity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commodity createUpdatedEntity(EntityManager em) {
        Commodity commodity = new Commodity().nameEnglish(UPDATED_NAME_ENGLISH).nameArabic(UPDATED_NAME_ARABIC);
        return commodity;
    }

    @BeforeEach
    public void initTest() {
        commodity = createEntity(em);
    }

    @Test
    @Transactional
    void createCommodity() throws Exception {
        int databaseSizeBeforeCreate = commodityRepository.findAll().size();
        // Create the Commodity
        restCommodityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commodity)))
            .andExpect(status().isCreated());

        // Validate the Commodity in the database
        List<Commodity> commodityList = commodityRepository.findAll();
        assertThat(commodityList).hasSize(databaseSizeBeforeCreate + 1);
        Commodity testCommodity = commodityList.get(commodityList.size() - 1);
        assertThat(testCommodity.getNameEnglish()).isEqualTo(DEFAULT_NAME_ENGLISH);
        assertThat(testCommodity.getNameArabic()).isEqualTo(DEFAULT_NAME_ARABIC);
    }

    @Test
    @Transactional
    void createCommodityWithExistingId() throws Exception {
        // Create the Commodity with an existing ID
        commodity.setId(1L);

        int databaseSizeBeforeCreate = commodityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommodityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commodity)))
            .andExpect(status().isBadRequest());

        // Validate the Commodity in the database
        List<Commodity> commodityList = commodityRepository.findAll();
        assertThat(commodityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCommodities() throws Exception {
        // Initialize the database
        commodityRepository.saveAndFlush(commodity);

        // Get all the commodityList
        restCommodityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commodity.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameEnglish").value(hasItem(DEFAULT_NAME_ENGLISH)))
            .andExpect(jsonPath("$.[*].nameArabic").value(hasItem(DEFAULT_NAME_ARABIC)));
    }

    @Test
    @Transactional
    void getCommodity() throws Exception {
        // Initialize the database
        commodityRepository.saveAndFlush(commodity);

        // Get the commodity
        restCommodityMockMvc
            .perform(get(ENTITY_API_URL_ID, commodity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commodity.getId().intValue()))
            .andExpect(jsonPath("$.nameEnglish").value(DEFAULT_NAME_ENGLISH))
            .andExpect(jsonPath("$.nameArabic").value(DEFAULT_NAME_ARABIC));
    }

    @Test
    @Transactional
    void getNonExistingCommodity() throws Exception {
        // Get the commodity
        restCommodityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCommodity() throws Exception {
        // Initialize the database
        commodityRepository.saveAndFlush(commodity);

        int databaseSizeBeforeUpdate = commodityRepository.findAll().size();

        // Update the commodity
        Commodity updatedCommodity = commodityRepository.findById(commodity.getId()).get();
        // Disconnect from session so that the updates on updatedCommodity are not directly saved in db
        em.detach(updatedCommodity);
        updatedCommodity.nameEnglish(UPDATED_NAME_ENGLISH).nameArabic(UPDATED_NAME_ARABIC);

        restCommodityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCommodity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCommodity))
            )
            .andExpect(status().isOk());

        // Validate the Commodity in the database
        List<Commodity> commodityList = commodityRepository.findAll();
        assertThat(commodityList).hasSize(databaseSizeBeforeUpdate);
        Commodity testCommodity = commodityList.get(commodityList.size() - 1);
        assertThat(testCommodity.getNameEnglish()).isEqualTo(UPDATED_NAME_ENGLISH);
        assertThat(testCommodity.getNameArabic()).isEqualTo(UPDATED_NAME_ARABIC);
    }

    @Test
    @Transactional
    void putNonExistingCommodity() throws Exception {
        int databaseSizeBeforeUpdate = commodityRepository.findAll().size();
        commodity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommodityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commodity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commodity))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commodity in the database
        List<Commodity> commodityList = commodityRepository.findAll();
        assertThat(commodityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommodity() throws Exception {
        int databaseSizeBeforeUpdate = commodityRepository.findAll().size();
        commodity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommodityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(commodity))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commodity in the database
        List<Commodity> commodityList = commodityRepository.findAll();
        assertThat(commodityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommodity() throws Exception {
        int databaseSizeBeforeUpdate = commodityRepository.findAll().size();
        commodity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommodityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(commodity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commodity in the database
        List<Commodity> commodityList = commodityRepository.findAll();
        assertThat(commodityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommodityWithPatch() throws Exception {
        // Initialize the database
        commodityRepository.saveAndFlush(commodity);

        int databaseSizeBeforeUpdate = commodityRepository.findAll().size();

        // Update the commodity using partial update
        Commodity partialUpdatedCommodity = new Commodity();
        partialUpdatedCommodity.setId(commodity.getId());

        partialUpdatedCommodity.nameEnglish(UPDATED_NAME_ENGLISH).nameArabic(UPDATED_NAME_ARABIC);

        restCommodityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommodity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommodity))
            )
            .andExpect(status().isOk());

        // Validate the Commodity in the database
        List<Commodity> commodityList = commodityRepository.findAll();
        assertThat(commodityList).hasSize(databaseSizeBeforeUpdate);
        Commodity testCommodity = commodityList.get(commodityList.size() - 1);
        assertThat(testCommodity.getNameEnglish()).isEqualTo(UPDATED_NAME_ENGLISH);
        assertThat(testCommodity.getNameArabic()).isEqualTo(UPDATED_NAME_ARABIC);
    }

    @Test
    @Transactional
    void fullUpdateCommodityWithPatch() throws Exception {
        // Initialize the database
        commodityRepository.saveAndFlush(commodity);

        int databaseSizeBeforeUpdate = commodityRepository.findAll().size();

        // Update the commodity using partial update
        Commodity partialUpdatedCommodity = new Commodity();
        partialUpdatedCommodity.setId(commodity.getId());

        partialUpdatedCommodity.nameEnglish(UPDATED_NAME_ENGLISH).nameArabic(UPDATED_NAME_ARABIC);

        restCommodityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommodity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCommodity))
            )
            .andExpect(status().isOk());

        // Validate the Commodity in the database
        List<Commodity> commodityList = commodityRepository.findAll();
        assertThat(commodityList).hasSize(databaseSizeBeforeUpdate);
        Commodity testCommodity = commodityList.get(commodityList.size() - 1);
        assertThat(testCommodity.getNameEnglish()).isEqualTo(UPDATED_NAME_ENGLISH);
        assertThat(testCommodity.getNameArabic()).isEqualTo(UPDATED_NAME_ARABIC);
    }

    @Test
    @Transactional
    void patchNonExistingCommodity() throws Exception {
        int databaseSizeBeforeUpdate = commodityRepository.findAll().size();
        commodity.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommodityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commodity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commodity))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commodity in the database
        List<Commodity> commodityList = commodityRepository.findAll();
        assertThat(commodityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommodity() throws Exception {
        int databaseSizeBeforeUpdate = commodityRepository.findAll().size();
        commodity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommodityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(commodity))
            )
            .andExpect(status().isBadRequest());

        // Validate the Commodity in the database
        List<Commodity> commodityList = commodityRepository.findAll();
        assertThat(commodityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommodity() throws Exception {
        int databaseSizeBeforeUpdate = commodityRepository.findAll().size();
        commodity.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommodityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(commodity))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Commodity in the database
        List<Commodity> commodityList = commodityRepository.findAll();
        assertThat(commodityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommodity() throws Exception {
        // Initialize the database
        commodityRepository.saveAndFlush(commodity);

        int databaseSizeBeforeDelete = commodityRepository.findAll().size();

        // Delete the commodity
        restCommodityMockMvc
            .perform(delete(ENTITY_API_URL_ID, commodity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Commodity> commodityList = commodityRepository.findAll();
        assertThat(commodityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
