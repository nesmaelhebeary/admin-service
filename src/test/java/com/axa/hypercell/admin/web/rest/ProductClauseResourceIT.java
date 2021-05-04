package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.ProductClause;
import com.axa.hypercell.admin.repository.ProductClauseRepository;
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
 * Integration tests for the {@link ProductClauseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductClauseResourceIT {

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final Long DEFAULT_CLAUSE_ID = 1L;
    private static final Long UPDATED_CLAUSE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/product-clauses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductClauseRepository productClauseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductClauseMockMvc;

    private ProductClause productClause;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductClause createEntity(EntityManager em) {
        ProductClause productClause = new ProductClause().productId(DEFAULT_PRODUCT_ID).clauseId(DEFAULT_CLAUSE_ID);
        return productClause;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductClause createUpdatedEntity(EntityManager em) {
        ProductClause productClause = new ProductClause().productId(UPDATED_PRODUCT_ID).clauseId(UPDATED_CLAUSE_ID);
        return productClause;
    }

    @BeforeEach
    public void initTest() {
        productClause = createEntity(em);
    }

    @Test
    @Transactional
    void createProductClause() throws Exception {
        int databaseSizeBeforeCreate = productClauseRepository.findAll().size();
        // Create the ProductClause
        restProductClauseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productClause)))
            .andExpect(status().isCreated());

        // Validate the ProductClause in the database
        List<ProductClause> productClauseList = productClauseRepository.findAll();
        assertThat(productClauseList).hasSize(databaseSizeBeforeCreate + 1);
        ProductClause testProductClause = productClauseList.get(productClauseList.size() - 1);
        assertThat(testProductClause.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testProductClause.getClauseId()).isEqualTo(DEFAULT_CLAUSE_ID);
    }

    @Test
    @Transactional
    void createProductClauseWithExistingId() throws Exception {
        // Create the ProductClause with an existing ID
        productClause.setId(1L);

        int databaseSizeBeforeCreate = productClauseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductClauseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productClause)))
            .andExpect(status().isBadRequest());

        // Validate the ProductClause in the database
        List<ProductClause> productClauseList = productClauseRepository.findAll();
        assertThat(productClauseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductClauses() throws Exception {
        // Initialize the database
        productClauseRepository.saveAndFlush(productClause);

        // Get all the productClauseList
        restProductClauseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productClause.getId().intValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].clauseId").value(hasItem(DEFAULT_CLAUSE_ID.intValue())));
    }

    @Test
    @Transactional
    void getProductClause() throws Exception {
        // Initialize the database
        productClauseRepository.saveAndFlush(productClause);

        // Get the productClause
        restProductClauseMockMvc
            .perform(get(ENTITY_API_URL_ID, productClause.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productClause.getId().intValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.clauseId").value(DEFAULT_CLAUSE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingProductClause() throws Exception {
        // Get the productClause
        restProductClauseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductClause() throws Exception {
        // Initialize the database
        productClauseRepository.saveAndFlush(productClause);

        int databaseSizeBeforeUpdate = productClauseRepository.findAll().size();

        // Update the productClause
        ProductClause updatedProductClause = productClauseRepository.findById(productClause.getId()).get();
        // Disconnect from session so that the updates on updatedProductClause are not directly saved in db
        em.detach(updatedProductClause);
        updatedProductClause.productId(UPDATED_PRODUCT_ID).clauseId(UPDATED_CLAUSE_ID);

        restProductClauseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductClause.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductClause))
            )
            .andExpect(status().isOk());

        // Validate the ProductClause in the database
        List<ProductClause> productClauseList = productClauseRepository.findAll();
        assertThat(productClauseList).hasSize(databaseSizeBeforeUpdate);
        ProductClause testProductClause = productClauseList.get(productClauseList.size() - 1);
        assertThat(testProductClause.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProductClause.getClauseId()).isEqualTo(UPDATED_CLAUSE_ID);
    }

    @Test
    @Transactional
    void putNonExistingProductClause() throws Exception {
        int databaseSizeBeforeUpdate = productClauseRepository.findAll().size();
        productClause.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductClauseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productClause.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productClause))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductClause in the database
        List<ProductClause> productClauseList = productClauseRepository.findAll();
        assertThat(productClauseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductClause() throws Exception {
        int databaseSizeBeforeUpdate = productClauseRepository.findAll().size();
        productClause.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductClauseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productClause))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductClause in the database
        List<ProductClause> productClauseList = productClauseRepository.findAll();
        assertThat(productClauseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductClause() throws Exception {
        int databaseSizeBeforeUpdate = productClauseRepository.findAll().size();
        productClause.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductClauseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productClause)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductClause in the database
        List<ProductClause> productClauseList = productClauseRepository.findAll();
        assertThat(productClauseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductClauseWithPatch() throws Exception {
        // Initialize the database
        productClauseRepository.saveAndFlush(productClause);

        int databaseSizeBeforeUpdate = productClauseRepository.findAll().size();

        // Update the productClause using partial update
        ProductClause partialUpdatedProductClause = new ProductClause();
        partialUpdatedProductClause.setId(productClause.getId());

        partialUpdatedProductClause.productId(UPDATED_PRODUCT_ID);

        restProductClauseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductClause.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductClause))
            )
            .andExpect(status().isOk());

        // Validate the ProductClause in the database
        List<ProductClause> productClauseList = productClauseRepository.findAll();
        assertThat(productClauseList).hasSize(databaseSizeBeforeUpdate);
        ProductClause testProductClause = productClauseList.get(productClauseList.size() - 1);
        assertThat(testProductClause.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProductClause.getClauseId()).isEqualTo(DEFAULT_CLAUSE_ID);
    }

    @Test
    @Transactional
    void fullUpdateProductClauseWithPatch() throws Exception {
        // Initialize the database
        productClauseRepository.saveAndFlush(productClause);

        int databaseSizeBeforeUpdate = productClauseRepository.findAll().size();

        // Update the productClause using partial update
        ProductClause partialUpdatedProductClause = new ProductClause();
        partialUpdatedProductClause.setId(productClause.getId());

        partialUpdatedProductClause.productId(UPDATED_PRODUCT_ID).clauseId(UPDATED_CLAUSE_ID);

        restProductClauseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductClause.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductClause))
            )
            .andExpect(status().isOk());

        // Validate the ProductClause in the database
        List<ProductClause> productClauseList = productClauseRepository.findAll();
        assertThat(productClauseList).hasSize(databaseSizeBeforeUpdate);
        ProductClause testProductClause = productClauseList.get(productClauseList.size() - 1);
        assertThat(testProductClause.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProductClause.getClauseId()).isEqualTo(UPDATED_CLAUSE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingProductClause() throws Exception {
        int databaseSizeBeforeUpdate = productClauseRepository.findAll().size();
        productClause.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductClauseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productClause.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productClause))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductClause in the database
        List<ProductClause> productClauseList = productClauseRepository.findAll();
        assertThat(productClauseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductClause() throws Exception {
        int databaseSizeBeforeUpdate = productClauseRepository.findAll().size();
        productClause.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductClauseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productClause))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductClause in the database
        List<ProductClause> productClauseList = productClauseRepository.findAll();
        assertThat(productClauseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductClause() throws Exception {
        int databaseSizeBeforeUpdate = productClauseRepository.findAll().size();
        productClause.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductClauseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productClause))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductClause in the database
        List<ProductClause> productClauseList = productClauseRepository.findAll();
        assertThat(productClauseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductClause() throws Exception {
        // Initialize the database
        productClauseRepository.saveAndFlush(productClause);

        int databaseSizeBeforeDelete = productClauseRepository.findAll().size();

        // Delete the productClause
        restProductClauseMockMvc
            .perform(delete(ENTITY_API_URL_ID, productClause.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductClause> productClauseList = productClauseRepository.findAll();
        assertThat(productClauseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
