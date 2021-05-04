package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.ProductsCommissionSchema;
import com.axa.hypercell.admin.repository.ProductsCommissionSchemaRepository;
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
 * Integration tests for the {@link ProductsCommissionSchemaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductsCommissionSchemaResourceIT {

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final String DEFAULT_NAME_EN = "AAAAAAAAAA";
    private static final String UPDATED_NAME_EN = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_AR = "AAAAAAAAAA";
    private static final String UPDATED_NAME_AR = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DISPLAY_IN_TEMPLATE = false;
    private static final Boolean UPDATED_DISPLAY_IN_TEMPLATE = true;

    private static final String ENTITY_API_URL = "/api/products-commission-schemas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductsCommissionSchemaRepository productsCommissionSchemaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductsCommissionSchemaMockMvc;

    private ProductsCommissionSchema productsCommissionSchema;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductsCommissionSchema createEntity(EntityManager em) {
        ProductsCommissionSchema productsCommissionSchema = new ProductsCommissionSchema()
            .productId(DEFAULT_PRODUCT_ID)
            .nameEn(DEFAULT_NAME_EN)
            .nameAr(DEFAULT_NAME_AR)
            .displayInTemplate(DEFAULT_DISPLAY_IN_TEMPLATE);
        return productsCommissionSchema;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductsCommissionSchema createUpdatedEntity(EntityManager em) {
        ProductsCommissionSchema productsCommissionSchema = new ProductsCommissionSchema()
            .productId(UPDATED_PRODUCT_ID)
            .nameEn(UPDATED_NAME_EN)
            .nameAr(UPDATED_NAME_AR)
            .displayInTemplate(UPDATED_DISPLAY_IN_TEMPLATE);
        return productsCommissionSchema;
    }

    @BeforeEach
    public void initTest() {
        productsCommissionSchema = createEntity(em);
    }

    @Test
    @Transactional
    void createProductsCommissionSchema() throws Exception {
        int databaseSizeBeforeCreate = productsCommissionSchemaRepository.findAll().size();
        // Create the ProductsCommissionSchema
        restProductsCommissionSchemaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productsCommissionSchema))
            )
            .andExpect(status().isCreated());

        // Validate the ProductsCommissionSchema in the database
        List<ProductsCommissionSchema> productsCommissionSchemaList = productsCommissionSchemaRepository.findAll();
        assertThat(productsCommissionSchemaList).hasSize(databaseSizeBeforeCreate + 1);
        ProductsCommissionSchema testProductsCommissionSchema = productsCommissionSchemaList.get(productsCommissionSchemaList.size() - 1);
        assertThat(testProductsCommissionSchema.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testProductsCommissionSchema.getNameEn()).isEqualTo(DEFAULT_NAME_EN);
        assertThat(testProductsCommissionSchema.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
        assertThat(testProductsCommissionSchema.getDisplayInTemplate()).isEqualTo(DEFAULT_DISPLAY_IN_TEMPLATE);
    }

    @Test
    @Transactional
    void createProductsCommissionSchemaWithExistingId() throws Exception {
        // Create the ProductsCommissionSchema with an existing ID
        productsCommissionSchema.setId(1L);

        int databaseSizeBeforeCreate = productsCommissionSchemaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductsCommissionSchemaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productsCommissionSchema))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductsCommissionSchema in the database
        List<ProductsCommissionSchema> productsCommissionSchemaList = productsCommissionSchemaRepository.findAll();
        assertThat(productsCommissionSchemaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductsCommissionSchemas() throws Exception {
        // Initialize the database
        productsCommissionSchemaRepository.saveAndFlush(productsCommissionSchema);

        // Get all the productsCommissionSchemaList
        restProductsCommissionSchemaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productsCommissionSchema.getId().intValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameAr").value(hasItem(DEFAULT_NAME_AR)))
            .andExpect(jsonPath("$.[*].displayInTemplate").value(hasItem(DEFAULT_DISPLAY_IN_TEMPLATE.booleanValue())));
    }

    @Test
    @Transactional
    void getProductsCommissionSchema() throws Exception {
        // Initialize the database
        productsCommissionSchemaRepository.saveAndFlush(productsCommissionSchema);

        // Get the productsCommissionSchema
        restProductsCommissionSchemaMockMvc
            .perform(get(ENTITY_API_URL_ID, productsCommissionSchema.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productsCommissionSchema.getId().intValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.nameEn").value(DEFAULT_NAME_EN))
            .andExpect(jsonPath("$.nameAr").value(DEFAULT_NAME_AR))
            .andExpect(jsonPath("$.displayInTemplate").value(DEFAULT_DISPLAY_IN_TEMPLATE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingProductsCommissionSchema() throws Exception {
        // Get the productsCommissionSchema
        restProductsCommissionSchemaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductsCommissionSchema() throws Exception {
        // Initialize the database
        productsCommissionSchemaRepository.saveAndFlush(productsCommissionSchema);

        int databaseSizeBeforeUpdate = productsCommissionSchemaRepository.findAll().size();

        // Update the productsCommissionSchema
        ProductsCommissionSchema updatedProductsCommissionSchema = productsCommissionSchemaRepository
            .findById(productsCommissionSchema.getId())
            .get();
        // Disconnect from session so that the updates on updatedProductsCommissionSchema are not directly saved in db
        em.detach(updatedProductsCommissionSchema);
        updatedProductsCommissionSchema
            .productId(UPDATED_PRODUCT_ID)
            .nameEn(UPDATED_NAME_EN)
            .nameAr(UPDATED_NAME_AR)
            .displayInTemplate(UPDATED_DISPLAY_IN_TEMPLATE);

        restProductsCommissionSchemaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductsCommissionSchema.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductsCommissionSchema))
            )
            .andExpect(status().isOk());

        // Validate the ProductsCommissionSchema in the database
        List<ProductsCommissionSchema> productsCommissionSchemaList = productsCommissionSchemaRepository.findAll();
        assertThat(productsCommissionSchemaList).hasSize(databaseSizeBeforeUpdate);
        ProductsCommissionSchema testProductsCommissionSchema = productsCommissionSchemaList.get(productsCommissionSchemaList.size() - 1);
        assertThat(testProductsCommissionSchema.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProductsCommissionSchema.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testProductsCommissionSchema.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testProductsCommissionSchema.getDisplayInTemplate()).isEqualTo(UPDATED_DISPLAY_IN_TEMPLATE);
    }

    @Test
    @Transactional
    void putNonExistingProductsCommissionSchema() throws Exception {
        int databaseSizeBeforeUpdate = productsCommissionSchemaRepository.findAll().size();
        productsCommissionSchema.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductsCommissionSchemaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productsCommissionSchema.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productsCommissionSchema))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductsCommissionSchema in the database
        List<ProductsCommissionSchema> productsCommissionSchemaList = productsCommissionSchemaRepository.findAll();
        assertThat(productsCommissionSchemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductsCommissionSchema() throws Exception {
        int databaseSizeBeforeUpdate = productsCommissionSchemaRepository.findAll().size();
        productsCommissionSchema.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsCommissionSchemaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productsCommissionSchema))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductsCommissionSchema in the database
        List<ProductsCommissionSchema> productsCommissionSchemaList = productsCommissionSchemaRepository.findAll();
        assertThat(productsCommissionSchemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductsCommissionSchema() throws Exception {
        int databaseSizeBeforeUpdate = productsCommissionSchemaRepository.findAll().size();
        productsCommissionSchema.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsCommissionSchemaMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productsCommissionSchema))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductsCommissionSchema in the database
        List<ProductsCommissionSchema> productsCommissionSchemaList = productsCommissionSchemaRepository.findAll();
        assertThat(productsCommissionSchemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductsCommissionSchemaWithPatch() throws Exception {
        // Initialize the database
        productsCommissionSchemaRepository.saveAndFlush(productsCommissionSchema);

        int databaseSizeBeforeUpdate = productsCommissionSchemaRepository.findAll().size();

        // Update the productsCommissionSchema using partial update
        ProductsCommissionSchema partialUpdatedProductsCommissionSchema = new ProductsCommissionSchema();
        partialUpdatedProductsCommissionSchema.setId(productsCommissionSchema.getId());

        partialUpdatedProductsCommissionSchema.productId(UPDATED_PRODUCT_ID).nameEn(UPDATED_NAME_EN);

        restProductsCommissionSchemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductsCommissionSchema.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductsCommissionSchema))
            )
            .andExpect(status().isOk());

        // Validate the ProductsCommissionSchema in the database
        List<ProductsCommissionSchema> productsCommissionSchemaList = productsCommissionSchemaRepository.findAll();
        assertThat(productsCommissionSchemaList).hasSize(databaseSizeBeforeUpdate);
        ProductsCommissionSchema testProductsCommissionSchema = productsCommissionSchemaList.get(productsCommissionSchemaList.size() - 1);
        assertThat(testProductsCommissionSchema.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProductsCommissionSchema.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testProductsCommissionSchema.getNameAr()).isEqualTo(DEFAULT_NAME_AR);
        assertThat(testProductsCommissionSchema.getDisplayInTemplate()).isEqualTo(DEFAULT_DISPLAY_IN_TEMPLATE);
    }

    @Test
    @Transactional
    void fullUpdateProductsCommissionSchemaWithPatch() throws Exception {
        // Initialize the database
        productsCommissionSchemaRepository.saveAndFlush(productsCommissionSchema);

        int databaseSizeBeforeUpdate = productsCommissionSchemaRepository.findAll().size();

        // Update the productsCommissionSchema using partial update
        ProductsCommissionSchema partialUpdatedProductsCommissionSchema = new ProductsCommissionSchema();
        partialUpdatedProductsCommissionSchema.setId(productsCommissionSchema.getId());

        partialUpdatedProductsCommissionSchema
            .productId(UPDATED_PRODUCT_ID)
            .nameEn(UPDATED_NAME_EN)
            .nameAr(UPDATED_NAME_AR)
            .displayInTemplate(UPDATED_DISPLAY_IN_TEMPLATE);

        restProductsCommissionSchemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductsCommissionSchema.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductsCommissionSchema))
            )
            .andExpect(status().isOk());

        // Validate the ProductsCommissionSchema in the database
        List<ProductsCommissionSchema> productsCommissionSchemaList = productsCommissionSchemaRepository.findAll();
        assertThat(productsCommissionSchemaList).hasSize(databaseSizeBeforeUpdate);
        ProductsCommissionSchema testProductsCommissionSchema = productsCommissionSchemaList.get(productsCommissionSchemaList.size() - 1);
        assertThat(testProductsCommissionSchema.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProductsCommissionSchema.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testProductsCommissionSchema.getNameAr()).isEqualTo(UPDATED_NAME_AR);
        assertThat(testProductsCommissionSchema.getDisplayInTemplate()).isEqualTo(UPDATED_DISPLAY_IN_TEMPLATE);
    }

    @Test
    @Transactional
    void patchNonExistingProductsCommissionSchema() throws Exception {
        int databaseSizeBeforeUpdate = productsCommissionSchemaRepository.findAll().size();
        productsCommissionSchema.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductsCommissionSchemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productsCommissionSchema.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productsCommissionSchema))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductsCommissionSchema in the database
        List<ProductsCommissionSchema> productsCommissionSchemaList = productsCommissionSchemaRepository.findAll();
        assertThat(productsCommissionSchemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductsCommissionSchema() throws Exception {
        int databaseSizeBeforeUpdate = productsCommissionSchemaRepository.findAll().size();
        productsCommissionSchema.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsCommissionSchemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productsCommissionSchema))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductsCommissionSchema in the database
        List<ProductsCommissionSchema> productsCommissionSchemaList = productsCommissionSchemaRepository.findAll();
        assertThat(productsCommissionSchemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductsCommissionSchema() throws Exception {
        int databaseSizeBeforeUpdate = productsCommissionSchemaRepository.findAll().size();
        productsCommissionSchema.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsCommissionSchemaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productsCommissionSchema))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductsCommissionSchema in the database
        List<ProductsCommissionSchema> productsCommissionSchemaList = productsCommissionSchemaRepository.findAll();
        assertThat(productsCommissionSchemaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductsCommissionSchema() throws Exception {
        // Initialize the database
        productsCommissionSchemaRepository.saveAndFlush(productsCommissionSchema);

        int databaseSizeBeforeDelete = productsCommissionSchemaRepository.findAll().size();

        // Delete the productsCommissionSchema
        restProductsCommissionSchemaMockMvc
            .perform(delete(ENTITY_API_URL_ID, productsCommissionSchema.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductsCommissionSchema> productsCommissionSchemaList = productsCommissionSchemaRepository.findAll();
        assertThat(productsCommissionSchemaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
