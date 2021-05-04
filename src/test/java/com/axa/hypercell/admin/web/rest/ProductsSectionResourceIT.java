package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.ProductsSection;
import com.axa.hypercell.admin.domain.enumeration.DefaultSumUp;
import com.axa.hypercell.admin.domain.enumeration.Status;
import com.axa.hypercell.admin.repository.ProductsSectionRepository;
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
 * Integration tests for the {@link ProductsSectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductsSectionResourceIT {

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final String DEFAULT_NAME_EN = "AAAAAAAAAA";
    private static final String UPDATED_NAME_EN = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_AR = "AAAAAAAAAA";
    private static final String UPDATED_NAME_AR = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.Active;
    private static final Status UPDATED_STATUS = Status.InActive;

    private static final DefaultSumUp DEFAULT_DEFAULT_SUM_UP = DefaultSumUp.Included;
    private static final DefaultSumUp UPDATED_DEFAULT_SUM_UP = DefaultSumUp.Exceluded;

    private static final String ENTITY_API_URL = "/api/products-sections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductsSectionRepository productsSectionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductsSectionMockMvc;

    private ProductsSection productsSection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductsSection createEntity(EntityManager em) {
        ProductsSection productsSection = new ProductsSection()
            .productId(DEFAULT_PRODUCT_ID)
            .nameEn(DEFAULT_NAME_EN)
            .nameAr(DEFAULT_NAME_AR)
            .status(DEFAULT_STATUS)
            .defaultSumUp(DEFAULT_DEFAULT_SUM_UP);
        return productsSection;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductsSection createUpdatedEntity(EntityManager em) {
        ProductsSection productsSection = new ProductsSection()
            .productId(UPDATED_PRODUCT_ID)
            .nameEn(UPDATED_NAME_EN)
            .nameAr(UPDATED_NAME_AR)
            .status(UPDATED_STATUS)
            .defaultSumUp(UPDATED_DEFAULT_SUM_UP);
        return productsSection;
    }

    @BeforeEach
    public void initTest() {
        productsSection = createEntity(em);
    }

    @Test
    @Transactional
    void createProductsSection() throws Exception {
        int databaseSizeBeforeCreate = productsSectionRepository.findAll().size();
        // Create the ProductsSection
        restProductsSectionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productsSection))
            )
            .andExpect(status().isCreated());

        // Validate the ProductsSection in the database
        List<ProductsSection> productsSectionList = productsSectionRepository.findAll();
        assertThat(productsSectionList).hasSize(databaseSizeBeforeCreate + 1);
        ProductsSection testProductsSection = productsSectionList.get(productsSectionList.size() - 1);
        assertThat(testProductsSection.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testProductsSection.getNameEn()).isEqualTo(DEFAULT_NAME_EN);
        assertThat(testProductsSection.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
        assertThat(testProductsSection.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProductsSection.getDefaultSumUp()).isEqualTo(DEFAULT_DEFAULT_SUM_UP);
    }

    @Test
    @Transactional
    void createProductsSectionWithExistingId() throws Exception {
        // Create the ProductsSection with an existing ID
        productsSection.setId(1L);

        int databaseSizeBeforeCreate = productsSectionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductsSectionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productsSection))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductsSection in the database
        List<ProductsSection> productsSectionList = productsSectionRepository.findAll();
        assertThat(productsSectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductsSections() throws Exception {
        // Initialize the database
        productsSectionRepository.saveAndFlush(productsSection);

        // Get all the productsSectionList
        restProductsSectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productsSection.getId().intValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].defaultSumUp").value(hasItem(DEFAULT_DEFAULT_SUM_UP.toString())));
    }

    @Test
    @Transactional
    void getProductsSection() throws Exception {
        // Initialize the database
        productsSectionRepository.saveAndFlush(productsSection);

        // Get the productsSection
        restProductsSectionMockMvc
            .perform(get(ENTITY_API_URL_ID, productsSection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productsSection.getId().intValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.nameEn").value(DEFAULT_NAME_EN))
            .andExpect(jsonPath("$.nameAr").value(DEFAULT_NAME_AR))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.defaultSumUp").value(DEFAULT_DEFAULT_SUM_UP.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProductsSection() throws Exception {
        // Get the productsSection
        restProductsSectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductsSection() throws Exception {
        // Initialize the database
        productsSectionRepository.saveAndFlush(productsSection);

        int databaseSizeBeforeUpdate = productsSectionRepository.findAll().size();

        // Update the productsSection
        ProductsSection updatedProductsSection = productsSectionRepository.findById(productsSection.getId()).get();
        // Disconnect from session so that the updates on updatedProductsSection are not directly saved in db
        em.detach(updatedProductsSection);
        updatedProductsSection
            .productId(UPDATED_PRODUCT_ID)
            .nameEn(UPDATED_NAME_EN)
            .nameAr(UPDATED_NAME_AR)
            .status(UPDATED_STATUS)
            .defaultSumUp(UPDATED_DEFAULT_SUM_UP);

        restProductsSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductsSection.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductsSection))
            )
            .andExpect(status().isOk());

        // Validate the ProductsSection in the database
        List<ProductsSection> productsSectionList = productsSectionRepository.findAll();
        assertThat(productsSectionList).hasSize(databaseSizeBeforeUpdate);
        ProductsSection testProductsSection = productsSectionList.get(productsSectionList.size() - 1);
        assertThat(testProductsSection.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProductsSection.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testProductsSection.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testProductsSection.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProductsSection.getDefaultSumUp()).isEqualTo(UPDATED_DEFAULT_SUM_UP);
    }

    @Test
    @Transactional
    void putNonExistingProductsSection() throws Exception {
        int databaseSizeBeforeUpdate = productsSectionRepository.findAll().size();
        productsSection.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductsSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productsSection.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productsSection))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductsSection in the database
        List<ProductsSection> productsSectionList = productsSectionRepository.findAll();
        assertThat(productsSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductsSection() throws Exception {
        int databaseSizeBeforeUpdate = productsSectionRepository.findAll().size();
        productsSection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productsSection))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductsSection in the database
        List<ProductsSection> productsSectionList = productsSectionRepository.findAll();
        assertThat(productsSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductsSection() throws Exception {
        int databaseSizeBeforeUpdate = productsSectionRepository.findAll().size();
        productsSection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsSectionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productsSection))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductsSection in the database
        List<ProductsSection> productsSectionList = productsSectionRepository.findAll();
        assertThat(productsSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductsSectionWithPatch() throws Exception {
        // Initialize the database
        productsSectionRepository.saveAndFlush(productsSection);

        int databaseSizeBeforeUpdate = productsSectionRepository.findAll().size();

        // Update the productsSection using partial update
        ProductsSection partialUpdatedProductsSection = new ProductsSection();
        partialUpdatedProductsSection.setId(productsSection.getId());

        partialUpdatedProductsSection
            .productId(UPDATED_PRODUCT_ID)
            .nameEn(UPDATED_NAME_EN)
            .nameAr(UPDATED_NAME_AR)
            .defaultSumUp(UPDATED_DEFAULT_SUM_UP);

        restProductsSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductsSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductsSection))
            )
            .andExpect(status().isOk());

        // Validate the ProductsSection in the database
        List<ProductsSection> productsSectionList = productsSectionRepository.findAll();
        assertThat(productsSectionList).hasSize(databaseSizeBeforeUpdate);
        ProductsSection testProductsSection = productsSectionList.get(productsSectionList.size() - 1);
        assertThat(testProductsSection.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProductsSection.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testProductsSection.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testProductsSection.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProductsSection.getDefaultSumUp()).isEqualTo(UPDATED_DEFAULT_SUM_UP);
    }

    @Test
    @Transactional
    void fullUpdateProductsSectionWithPatch() throws Exception {
        // Initialize the database
        productsSectionRepository.saveAndFlush(productsSection);

        int databaseSizeBeforeUpdate = productsSectionRepository.findAll().size();

        // Update the productsSection using partial update
        ProductsSection partialUpdatedProductsSection = new ProductsSection();
        partialUpdatedProductsSection.setId(productsSection.getId());

        partialUpdatedProductsSection
            .productId(UPDATED_PRODUCT_ID)
            .nameEn(UPDATED_NAME_EN)
            .nameAr(UPDATED_NAME_AR)
            .status(UPDATED_STATUS)
            .defaultSumUp(UPDATED_DEFAULT_SUM_UP);

        restProductsSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductsSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductsSection))
            )
            .andExpect(status().isOk());

        // Validate the ProductsSection in the database
        List<ProductsSection> productsSectionList = productsSectionRepository.findAll();
        assertThat(productsSectionList).hasSize(databaseSizeBeforeUpdate);
        ProductsSection testProductsSection = productsSectionList.get(productsSectionList.size() - 1);
        assertThat(testProductsSection.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProductsSection.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testProductsSection.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testProductsSection.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProductsSection.getDefaultSumUp()).isEqualTo(UPDATED_DEFAULT_SUM_UP);
    }

    @Test
    @Transactional
    void patchNonExistingProductsSection() throws Exception {
        int databaseSizeBeforeUpdate = productsSectionRepository.findAll().size();
        productsSection.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductsSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productsSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productsSection))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductsSection in the database
        List<ProductsSection> productsSectionList = productsSectionRepository.findAll();
        assertThat(productsSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductsSection() throws Exception {
        int databaseSizeBeforeUpdate = productsSectionRepository.findAll().size();
        productsSection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productsSection))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductsSection in the database
        List<ProductsSection> productsSectionList = productsSectionRepository.findAll();
        assertThat(productsSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductsSection() throws Exception {
        int databaseSizeBeforeUpdate = productsSectionRepository.findAll().size();
        productsSection.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsSectionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productsSection))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductsSection in the database
        List<ProductsSection> productsSectionList = productsSectionRepository.findAll();
        assertThat(productsSectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductsSection() throws Exception {
        // Initialize the database
        productsSectionRepository.saveAndFlush(productsSection);

        int databaseSizeBeforeDelete = productsSectionRepository.findAll().size();

        // Delete the productsSection
        restProductsSectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, productsSection.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductsSection> productsSectionList = productsSectionRepository.findAll();
        assertThat(productsSectionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
