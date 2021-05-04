package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.ProductExtensions;
import com.axa.hypercell.admin.repository.ProductExtensionsRepository;
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
 * Integration tests for the {@link ProductExtensionsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductExtensionsResourceIT {

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final Long DEFAULT_EXTENSION_ID = 1L;
    private static final Long UPDATED_EXTENSION_ID = 2L;

    private static final String ENTITY_API_URL = "/api/product-extensions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductExtensionsRepository productExtensionsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductExtensionsMockMvc;

    private ProductExtensions productExtensions;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductExtensions createEntity(EntityManager em) {
        ProductExtensions productExtensions = new ProductExtensions().productId(DEFAULT_PRODUCT_ID).extensionId(DEFAULT_EXTENSION_ID);
        return productExtensions;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductExtensions createUpdatedEntity(EntityManager em) {
        ProductExtensions productExtensions = new ProductExtensions().productId(UPDATED_PRODUCT_ID).extensionId(UPDATED_EXTENSION_ID);
        return productExtensions;
    }

    @BeforeEach
    public void initTest() {
        productExtensions = createEntity(em);
    }

    @Test
    @Transactional
    void createProductExtensions() throws Exception {
        int databaseSizeBeforeCreate = productExtensionsRepository.findAll().size();
        // Create the ProductExtensions
        restProductExtensionsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productExtensions))
            )
            .andExpect(status().isCreated());

        // Validate the ProductExtensions in the database
        List<ProductExtensions> productExtensionsList = productExtensionsRepository.findAll();
        assertThat(productExtensionsList).hasSize(databaseSizeBeforeCreate + 1);
        ProductExtensions testProductExtensions = productExtensionsList.get(productExtensionsList.size() - 1);
        assertThat(testProductExtensions.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testProductExtensions.getExtensionId()).isEqualTo(DEFAULT_EXTENSION_ID);
    }

    @Test
    @Transactional
    void createProductExtensionsWithExistingId() throws Exception {
        // Create the ProductExtensions with an existing ID
        productExtensions.setId(1L);

        int databaseSizeBeforeCreate = productExtensionsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductExtensionsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productExtensions))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductExtensions in the database
        List<ProductExtensions> productExtensionsList = productExtensionsRepository.findAll();
        assertThat(productExtensionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductExtensions() throws Exception {
        // Initialize the database
        productExtensionsRepository.saveAndFlush(productExtensions);

        // Get all the productExtensionsList
        restProductExtensionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productExtensions.getId().intValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].extensionId").value(hasItem(DEFAULT_EXTENSION_ID.intValue())));
    }

    @Test
    @Transactional
    void getProductExtensions() throws Exception {
        // Initialize the database
        productExtensionsRepository.saveAndFlush(productExtensions);

        // Get the productExtensions
        restProductExtensionsMockMvc
            .perform(get(ENTITY_API_URL_ID, productExtensions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productExtensions.getId().intValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.extensionId").value(DEFAULT_EXTENSION_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingProductExtensions() throws Exception {
        // Get the productExtensions
        restProductExtensionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductExtensions() throws Exception {
        // Initialize the database
        productExtensionsRepository.saveAndFlush(productExtensions);

        int databaseSizeBeforeUpdate = productExtensionsRepository.findAll().size();

        // Update the productExtensions
        ProductExtensions updatedProductExtensions = productExtensionsRepository.findById(productExtensions.getId()).get();
        // Disconnect from session so that the updates on updatedProductExtensions are not directly saved in db
        em.detach(updatedProductExtensions);
        updatedProductExtensions.productId(UPDATED_PRODUCT_ID).extensionId(UPDATED_EXTENSION_ID);

        restProductExtensionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductExtensions.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductExtensions))
            )
            .andExpect(status().isOk());

        // Validate the ProductExtensions in the database
        List<ProductExtensions> productExtensionsList = productExtensionsRepository.findAll();
        assertThat(productExtensionsList).hasSize(databaseSizeBeforeUpdate);
        ProductExtensions testProductExtensions = productExtensionsList.get(productExtensionsList.size() - 1);
        assertThat(testProductExtensions.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProductExtensions.getExtensionId()).isEqualTo(UPDATED_EXTENSION_ID);
    }

    @Test
    @Transactional
    void putNonExistingProductExtensions() throws Exception {
        int databaseSizeBeforeUpdate = productExtensionsRepository.findAll().size();
        productExtensions.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductExtensionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productExtensions.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productExtensions))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductExtensions in the database
        List<ProductExtensions> productExtensionsList = productExtensionsRepository.findAll();
        assertThat(productExtensionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductExtensions() throws Exception {
        int databaseSizeBeforeUpdate = productExtensionsRepository.findAll().size();
        productExtensions.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductExtensionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productExtensions))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductExtensions in the database
        List<ProductExtensions> productExtensionsList = productExtensionsRepository.findAll();
        assertThat(productExtensionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductExtensions() throws Exception {
        int databaseSizeBeforeUpdate = productExtensionsRepository.findAll().size();
        productExtensions.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductExtensionsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productExtensions))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductExtensions in the database
        List<ProductExtensions> productExtensionsList = productExtensionsRepository.findAll();
        assertThat(productExtensionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductExtensionsWithPatch() throws Exception {
        // Initialize the database
        productExtensionsRepository.saveAndFlush(productExtensions);

        int databaseSizeBeforeUpdate = productExtensionsRepository.findAll().size();

        // Update the productExtensions using partial update
        ProductExtensions partialUpdatedProductExtensions = new ProductExtensions();
        partialUpdatedProductExtensions.setId(productExtensions.getId());

        restProductExtensionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductExtensions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductExtensions))
            )
            .andExpect(status().isOk());

        // Validate the ProductExtensions in the database
        List<ProductExtensions> productExtensionsList = productExtensionsRepository.findAll();
        assertThat(productExtensionsList).hasSize(databaseSizeBeforeUpdate);
        ProductExtensions testProductExtensions = productExtensionsList.get(productExtensionsList.size() - 1);
        assertThat(testProductExtensions.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testProductExtensions.getExtensionId()).isEqualTo(DEFAULT_EXTENSION_ID);
    }

    @Test
    @Transactional
    void fullUpdateProductExtensionsWithPatch() throws Exception {
        // Initialize the database
        productExtensionsRepository.saveAndFlush(productExtensions);

        int databaseSizeBeforeUpdate = productExtensionsRepository.findAll().size();

        // Update the productExtensions using partial update
        ProductExtensions partialUpdatedProductExtensions = new ProductExtensions();
        partialUpdatedProductExtensions.setId(productExtensions.getId());

        partialUpdatedProductExtensions.productId(UPDATED_PRODUCT_ID).extensionId(UPDATED_EXTENSION_ID);

        restProductExtensionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductExtensions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductExtensions))
            )
            .andExpect(status().isOk());

        // Validate the ProductExtensions in the database
        List<ProductExtensions> productExtensionsList = productExtensionsRepository.findAll();
        assertThat(productExtensionsList).hasSize(databaseSizeBeforeUpdate);
        ProductExtensions testProductExtensions = productExtensionsList.get(productExtensionsList.size() - 1);
        assertThat(testProductExtensions.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProductExtensions.getExtensionId()).isEqualTo(UPDATED_EXTENSION_ID);
    }

    @Test
    @Transactional
    void patchNonExistingProductExtensions() throws Exception {
        int databaseSizeBeforeUpdate = productExtensionsRepository.findAll().size();
        productExtensions.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductExtensionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productExtensions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productExtensions))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductExtensions in the database
        List<ProductExtensions> productExtensionsList = productExtensionsRepository.findAll();
        assertThat(productExtensionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductExtensions() throws Exception {
        int databaseSizeBeforeUpdate = productExtensionsRepository.findAll().size();
        productExtensions.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductExtensionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productExtensions))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductExtensions in the database
        List<ProductExtensions> productExtensionsList = productExtensionsRepository.findAll();
        assertThat(productExtensionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductExtensions() throws Exception {
        int databaseSizeBeforeUpdate = productExtensionsRepository.findAll().size();
        productExtensions.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductExtensionsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productExtensions))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductExtensions in the database
        List<ProductExtensions> productExtensionsList = productExtensionsRepository.findAll();
        assertThat(productExtensionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductExtensions() throws Exception {
        // Initialize the database
        productExtensionsRepository.saveAndFlush(productExtensions);

        int databaseSizeBeforeDelete = productExtensionsRepository.findAll().size();

        // Delete the productExtensions
        restProductExtensionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, productExtensions.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductExtensions> productExtensionsList = productExtensionsRepository.findAll();
        assertThat(productExtensionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
