package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.ProductsSectionIncludeList;
import com.axa.hypercell.admin.repository.ProductsSectionIncludeListRepository;
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
 * Integration tests for the {@link ProductsSectionIncludeListResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductsSectionIncludeListResourceIT {

    private static final Long DEFAULT_SECTION_ID = 1L;
    private static final Long UPDATED_SECTION_ID = 2L;

    private static final Long DEFAULT_OTHER_SECTION_ID = 1L;
    private static final Long UPDATED_OTHER_SECTION_ID = 2L;

    private static final String ENTITY_API_URL = "/api/products-section-include-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductsSectionIncludeListRepository productsSectionIncludeListRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductsSectionIncludeListMockMvc;

    private ProductsSectionIncludeList productsSectionIncludeList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductsSectionIncludeList createEntity(EntityManager em) {
        ProductsSectionIncludeList productsSectionIncludeList = new ProductsSectionIncludeList()
            .sectionId(DEFAULT_SECTION_ID)
            .otherSectionId(DEFAULT_OTHER_SECTION_ID);
        return productsSectionIncludeList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductsSectionIncludeList createUpdatedEntity(EntityManager em) {
        ProductsSectionIncludeList productsSectionIncludeList = new ProductsSectionIncludeList()
            .sectionId(UPDATED_SECTION_ID)
            .otherSectionId(UPDATED_OTHER_SECTION_ID);
        return productsSectionIncludeList;
    }

    @BeforeEach
    public void initTest() {
        productsSectionIncludeList = createEntity(em);
    }

    @Test
    @Transactional
    void createProductsSectionIncludeList() throws Exception {
        int databaseSizeBeforeCreate = productsSectionIncludeListRepository.findAll().size();
        // Create the ProductsSectionIncludeList
        restProductsSectionIncludeListMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productsSectionIncludeList))
            )
            .andExpect(status().isCreated());

        // Validate the ProductsSectionIncludeList in the database
        List<ProductsSectionIncludeList> productsSectionIncludeListList = productsSectionIncludeListRepository.findAll();
        assertThat(productsSectionIncludeListList).hasSize(databaseSizeBeforeCreate + 1);
        ProductsSectionIncludeList testProductsSectionIncludeList = productsSectionIncludeListList.get(
            productsSectionIncludeListList.size() - 1
        );
        assertThat(testProductsSectionIncludeList.getSectionId()).isEqualTo(DEFAULT_SECTION_ID);
        assertThat(testProductsSectionIncludeList.getOtherSectionId()).isEqualTo(DEFAULT_OTHER_SECTION_ID);
    }

    @Test
    @Transactional
    void createProductsSectionIncludeListWithExistingId() throws Exception {
        // Create the ProductsSectionIncludeList with an existing ID
        productsSectionIncludeList.setId(1L);

        int databaseSizeBeforeCreate = productsSectionIncludeListRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductsSectionIncludeListMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productsSectionIncludeList))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductsSectionIncludeList in the database
        List<ProductsSectionIncludeList> productsSectionIncludeListList = productsSectionIncludeListRepository.findAll();
        assertThat(productsSectionIncludeListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductsSectionIncludeLists() throws Exception {
        // Initialize the database
        productsSectionIncludeListRepository.saveAndFlush(productsSectionIncludeList);

        // Get all the productsSectionIncludeListList
        restProductsSectionIncludeListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productsSectionIncludeList.getId().intValue())))
            .andExpect(jsonPath("$.[*].sectionId").value(hasItem(DEFAULT_SECTION_ID.intValue())))
            .andExpect(jsonPath("$.[*].otherSectionId").value(hasItem(DEFAULT_OTHER_SECTION_ID.intValue())));
    }

    @Test
    @Transactional
    void getProductsSectionIncludeList() throws Exception {
        // Initialize the database
        productsSectionIncludeListRepository.saveAndFlush(productsSectionIncludeList);

        // Get the productsSectionIncludeList
        restProductsSectionIncludeListMockMvc
            .perform(get(ENTITY_API_URL_ID, productsSectionIncludeList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productsSectionIncludeList.getId().intValue()))
            .andExpect(jsonPath("$.sectionId").value(DEFAULT_SECTION_ID.intValue()))
            .andExpect(jsonPath("$.otherSectionId").value(DEFAULT_OTHER_SECTION_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingProductsSectionIncludeList() throws Exception {
        // Get the productsSectionIncludeList
        restProductsSectionIncludeListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductsSectionIncludeList() throws Exception {
        // Initialize the database
        productsSectionIncludeListRepository.saveAndFlush(productsSectionIncludeList);

        int databaseSizeBeforeUpdate = productsSectionIncludeListRepository.findAll().size();

        // Update the productsSectionIncludeList
        ProductsSectionIncludeList updatedProductsSectionIncludeList = productsSectionIncludeListRepository
            .findById(productsSectionIncludeList.getId())
            .get();
        // Disconnect from session so that the updates on updatedProductsSectionIncludeList are not directly saved in db
        em.detach(updatedProductsSectionIncludeList);
        updatedProductsSectionIncludeList.sectionId(UPDATED_SECTION_ID).otherSectionId(UPDATED_OTHER_SECTION_ID);

        restProductsSectionIncludeListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductsSectionIncludeList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductsSectionIncludeList))
            )
            .andExpect(status().isOk());

        // Validate the ProductsSectionIncludeList in the database
        List<ProductsSectionIncludeList> productsSectionIncludeListList = productsSectionIncludeListRepository.findAll();
        assertThat(productsSectionIncludeListList).hasSize(databaseSizeBeforeUpdate);
        ProductsSectionIncludeList testProductsSectionIncludeList = productsSectionIncludeListList.get(
            productsSectionIncludeListList.size() - 1
        );
        assertThat(testProductsSectionIncludeList.getSectionId()).isEqualTo(UPDATED_SECTION_ID);
        assertThat(testProductsSectionIncludeList.getOtherSectionId()).isEqualTo(UPDATED_OTHER_SECTION_ID);
    }

    @Test
    @Transactional
    void putNonExistingProductsSectionIncludeList() throws Exception {
        int databaseSizeBeforeUpdate = productsSectionIncludeListRepository.findAll().size();
        productsSectionIncludeList.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductsSectionIncludeListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productsSectionIncludeList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productsSectionIncludeList))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductsSectionIncludeList in the database
        List<ProductsSectionIncludeList> productsSectionIncludeListList = productsSectionIncludeListRepository.findAll();
        assertThat(productsSectionIncludeListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductsSectionIncludeList() throws Exception {
        int databaseSizeBeforeUpdate = productsSectionIncludeListRepository.findAll().size();
        productsSectionIncludeList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsSectionIncludeListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productsSectionIncludeList))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductsSectionIncludeList in the database
        List<ProductsSectionIncludeList> productsSectionIncludeListList = productsSectionIncludeListRepository.findAll();
        assertThat(productsSectionIncludeListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductsSectionIncludeList() throws Exception {
        int databaseSizeBeforeUpdate = productsSectionIncludeListRepository.findAll().size();
        productsSectionIncludeList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsSectionIncludeListMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productsSectionIncludeList))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductsSectionIncludeList in the database
        List<ProductsSectionIncludeList> productsSectionIncludeListList = productsSectionIncludeListRepository.findAll();
        assertThat(productsSectionIncludeListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductsSectionIncludeListWithPatch() throws Exception {
        // Initialize the database
        productsSectionIncludeListRepository.saveAndFlush(productsSectionIncludeList);

        int databaseSizeBeforeUpdate = productsSectionIncludeListRepository.findAll().size();

        // Update the productsSectionIncludeList using partial update
        ProductsSectionIncludeList partialUpdatedProductsSectionIncludeList = new ProductsSectionIncludeList();
        partialUpdatedProductsSectionIncludeList.setId(productsSectionIncludeList.getId());

        restProductsSectionIncludeListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductsSectionIncludeList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductsSectionIncludeList))
            )
            .andExpect(status().isOk());

        // Validate the ProductsSectionIncludeList in the database
        List<ProductsSectionIncludeList> productsSectionIncludeListList = productsSectionIncludeListRepository.findAll();
        assertThat(productsSectionIncludeListList).hasSize(databaseSizeBeforeUpdate);
        ProductsSectionIncludeList testProductsSectionIncludeList = productsSectionIncludeListList.get(
            productsSectionIncludeListList.size() - 1
        );
        assertThat(testProductsSectionIncludeList.getSectionId()).isEqualTo(DEFAULT_SECTION_ID);
        assertThat(testProductsSectionIncludeList.getOtherSectionId()).isEqualTo(DEFAULT_OTHER_SECTION_ID);
    }

    @Test
    @Transactional
    void fullUpdateProductsSectionIncludeListWithPatch() throws Exception {
        // Initialize the database
        productsSectionIncludeListRepository.saveAndFlush(productsSectionIncludeList);

        int databaseSizeBeforeUpdate = productsSectionIncludeListRepository.findAll().size();

        // Update the productsSectionIncludeList using partial update
        ProductsSectionIncludeList partialUpdatedProductsSectionIncludeList = new ProductsSectionIncludeList();
        partialUpdatedProductsSectionIncludeList.setId(productsSectionIncludeList.getId());

        partialUpdatedProductsSectionIncludeList.sectionId(UPDATED_SECTION_ID).otherSectionId(UPDATED_OTHER_SECTION_ID);

        restProductsSectionIncludeListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductsSectionIncludeList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductsSectionIncludeList))
            )
            .andExpect(status().isOk());

        // Validate the ProductsSectionIncludeList in the database
        List<ProductsSectionIncludeList> productsSectionIncludeListList = productsSectionIncludeListRepository.findAll();
        assertThat(productsSectionIncludeListList).hasSize(databaseSizeBeforeUpdate);
        ProductsSectionIncludeList testProductsSectionIncludeList = productsSectionIncludeListList.get(
            productsSectionIncludeListList.size() - 1
        );
        assertThat(testProductsSectionIncludeList.getSectionId()).isEqualTo(UPDATED_SECTION_ID);
        assertThat(testProductsSectionIncludeList.getOtherSectionId()).isEqualTo(UPDATED_OTHER_SECTION_ID);
    }

    @Test
    @Transactional
    void patchNonExistingProductsSectionIncludeList() throws Exception {
        int databaseSizeBeforeUpdate = productsSectionIncludeListRepository.findAll().size();
        productsSectionIncludeList.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductsSectionIncludeListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productsSectionIncludeList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productsSectionIncludeList))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductsSectionIncludeList in the database
        List<ProductsSectionIncludeList> productsSectionIncludeListList = productsSectionIncludeListRepository.findAll();
        assertThat(productsSectionIncludeListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductsSectionIncludeList() throws Exception {
        int databaseSizeBeforeUpdate = productsSectionIncludeListRepository.findAll().size();
        productsSectionIncludeList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsSectionIncludeListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productsSectionIncludeList))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductsSectionIncludeList in the database
        List<ProductsSectionIncludeList> productsSectionIncludeListList = productsSectionIncludeListRepository.findAll();
        assertThat(productsSectionIncludeListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductsSectionIncludeList() throws Exception {
        int databaseSizeBeforeUpdate = productsSectionIncludeListRepository.findAll().size();
        productsSectionIncludeList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsSectionIncludeListMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productsSectionIncludeList))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductsSectionIncludeList in the database
        List<ProductsSectionIncludeList> productsSectionIncludeListList = productsSectionIncludeListRepository.findAll();
        assertThat(productsSectionIncludeListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductsSectionIncludeList() throws Exception {
        // Initialize the database
        productsSectionIncludeListRepository.saveAndFlush(productsSectionIncludeList);

        int databaseSizeBeforeDelete = productsSectionIncludeListRepository.findAll().size();

        // Delete the productsSectionIncludeList
        restProductsSectionIncludeListMockMvc
            .perform(delete(ENTITY_API_URL_ID, productsSectionIncludeList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductsSectionIncludeList> productsSectionIncludeListList = productsSectionIncludeListRepository.findAll();
        assertThat(productsSectionIncludeListList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
