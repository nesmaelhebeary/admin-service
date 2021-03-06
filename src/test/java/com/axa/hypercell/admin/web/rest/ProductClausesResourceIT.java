package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.AdminserviceApp;
import com.axa.hypercell.admin.domain.ProductClauses;
import com.axa.hypercell.admin.repository.ProductClausesRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProductClausesResource} REST controller.
 */
@SpringBootTest(classes = AdminserviceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProductClausesResourceIT {

    private static final Long DEFAULT_CLAUSE_ID = 1L;
    private static final Long UPDATED_CLAUSE_ID = 2L;

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    @Autowired
    private ProductClausesRepository productClausesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductClausesMockMvc;

    private ProductClauses productClauses;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductClauses createEntity(EntityManager em) {
        ProductClauses productClauses = new ProductClauses()
            .clauseId(DEFAULT_CLAUSE_ID)
            .productId(DEFAULT_PRODUCT_ID);
        return productClauses;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductClauses createUpdatedEntity(EntityManager em) {
        ProductClauses productClauses = new ProductClauses()
            .clauseId(UPDATED_CLAUSE_ID)
            .productId(UPDATED_PRODUCT_ID);
        return productClauses;
    }

    @BeforeEach
    public void initTest() {
        productClauses = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductClauses() throws Exception {
        int databaseSizeBeforeCreate = productClausesRepository.findAll().size();
        // Create the ProductClauses
        restProductClausesMockMvc.perform(post("/api/product-clauses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productClauses)))
            .andExpect(status().isCreated());

        // Validate the ProductClauses in the database
        List<ProductClauses> productClausesList = productClausesRepository.findAll();
        assertThat(productClausesList).hasSize(databaseSizeBeforeCreate + 1);
        ProductClauses testProductClauses = productClausesList.get(productClausesList.size() - 1);
        assertThat(testProductClauses.getClauseId()).isEqualTo(DEFAULT_CLAUSE_ID);
        assertThat(testProductClauses.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
    }

    @Test
    @Transactional
    public void createProductClausesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productClausesRepository.findAll().size();

        // Create the ProductClauses with an existing ID
        productClauses.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductClausesMockMvc.perform(post("/api/product-clauses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productClauses)))
            .andExpect(status().isBadRequest());

        // Validate the ProductClauses in the database
        List<ProductClauses> productClausesList = productClausesRepository.findAll();
        assertThat(productClausesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllProductClauses() throws Exception {
        // Initialize the database
        productClausesRepository.saveAndFlush(productClauses);

        // Get all the productClausesList
        restProductClausesMockMvc.perform(get("/api/product-clauses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productClauses.getId().intValue())))
            .andExpect(jsonPath("$.[*].clauseId").value(hasItem(DEFAULT_CLAUSE_ID.intValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getProductClauses() throws Exception {
        // Initialize the database
        productClausesRepository.saveAndFlush(productClauses);

        // Get the productClauses
        restProductClausesMockMvc.perform(get("/api/product-clauses/{id}", productClauses.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productClauses.getId().intValue()))
            .andExpect(jsonPath("$.clauseId").value(DEFAULT_CLAUSE_ID.intValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingProductClauses() throws Exception {
        // Get the productClauses
        restProductClausesMockMvc.perform(get("/api/product-clauses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductClauses() throws Exception {
        // Initialize the database
        productClausesRepository.saveAndFlush(productClauses);

        int databaseSizeBeforeUpdate = productClausesRepository.findAll().size();

        // Update the productClauses
        ProductClauses updatedProductClauses = productClausesRepository.findById(productClauses.getId()).get();
        // Disconnect from session so that the updates on updatedProductClauses are not directly saved in db
        em.detach(updatedProductClauses);
        updatedProductClauses
            .clauseId(UPDATED_CLAUSE_ID)
            .productId(UPDATED_PRODUCT_ID);

        restProductClausesMockMvc.perform(put("/api/product-clauses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProductClauses)))
            .andExpect(status().isOk());

        // Validate the ProductClauses in the database
        List<ProductClauses> productClausesList = productClausesRepository.findAll();
        assertThat(productClausesList).hasSize(databaseSizeBeforeUpdate);
        ProductClauses testProductClauses = productClausesList.get(productClausesList.size() - 1);
        assertThat(testProductClauses.getClauseId()).isEqualTo(UPDATED_CLAUSE_ID);
        assertThat(testProductClauses.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingProductClauses() throws Exception {
        int databaseSizeBeforeUpdate = productClausesRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductClausesMockMvc.perform(put("/api/product-clauses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productClauses)))
            .andExpect(status().isBadRequest());

        // Validate the ProductClauses in the database
        List<ProductClauses> productClausesList = productClausesRepository.findAll();
        assertThat(productClausesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProductClauses() throws Exception {
        // Initialize the database
        productClausesRepository.saveAndFlush(productClauses);

        int databaseSizeBeforeDelete = productClausesRepository.findAll().size();

        // Delete the productClauses
        restProductClausesMockMvc.perform(delete("/api/product-clauses/{id}", productClauses.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductClauses> productClausesList = productClausesRepository.findAll();
        assertThat(productClausesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
