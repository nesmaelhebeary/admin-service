package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.AdminserviceApp;
import com.axa.hypercell.admin.domain.ProductTAndC;
import com.axa.hypercell.admin.repository.ProductTAndCRepository;

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
 * Integration tests for the {@link ProductTAndCResource} REST controller.
 */
@SpringBootTest(classes = AdminserviceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProductTAndCResourceIT {

    private static final Long DEFAULT_T_AND_C_ID = 1L;
    private static final Long UPDATED_T_AND_C_ID = 2L;

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    @Autowired
    private ProductTAndCRepository productTAndCRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductTAndCMockMvc;

    private ProductTAndC productTAndC;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductTAndC createEntity(EntityManager em) {
        ProductTAndC productTAndC = new ProductTAndC()
            .tAndCId(DEFAULT_T_AND_C_ID)
            .productId(DEFAULT_PRODUCT_ID);
        return productTAndC;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductTAndC createUpdatedEntity(EntityManager em) {
        ProductTAndC productTAndC = new ProductTAndC()
            .tAndCId(UPDATED_T_AND_C_ID)
            .productId(UPDATED_PRODUCT_ID);
        return productTAndC;
    }

    @BeforeEach
    public void initTest() {
        productTAndC = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductTAndC() throws Exception {
        int databaseSizeBeforeCreate = productTAndCRepository.findAll().size();
        // Create the ProductTAndC
        restProductTAndCMockMvc.perform(post("/api/product-t-and-cs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productTAndC)))
            .andExpect(status().isCreated());

        // Validate the ProductTAndC in the database
        List<ProductTAndC> productTAndCList = productTAndCRepository.findAll();
        assertThat(productTAndCList).hasSize(databaseSizeBeforeCreate + 1);
        ProductTAndC testProductTAndC = productTAndCList.get(productTAndCList.size() - 1);
        assertThat(testProductTAndC.gettAndCId()).isEqualTo(DEFAULT_T_AND_C_ID);
        assertThat(testProductTAndC.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
    }

    @Test
    @Transactional
    public void createProductTAndCWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productTAndCRepository.findAll().size();

        // Create the ProductTAndC with an existing ID
        productTAndC.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductTAndCMockMvc.perform(post("/api/product-t-and-cs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productTAndC)))
            .andExpect(status().isBadRequest());

        // Validate the ProductTAndC in the database
        List<ProductTAndC> productTAndCList = productTAndCRepository.findAll();
        assertThat(productTAndCList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllProductTAndCS() throws Exception {
        // Initialize the database
        productTAndCRepository.saveAndFlush(productTAndC);

        // Get all the productTAndCList
        restProductTAndCMockMvc.perform(get("/api/product-t-and-cs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productTAndC.getId().intValue())))
            .andExpect(jsonPath("$.[*].tAndCId").value(hasItem(DEFAULT_T_AND_C_ID.intValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())));
    }
    
    @Test
    @Transactional
    public void getProductTAndC() throws Exception {
        // Initialize the database
        productTAndCRepository.saveAndFlush(productTAndC);

        // Get the productTAndC
        restProductTAndCMockMvc.perform(get("/api/product-t-and-cs/{id}", productTAndC.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productTAndC.getId().intValue()))
            .andExpect(jsonPath("$.tAndCId").value(DEFAULT_T_AND_C_ID.intValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingProductTAndC() throws Exception {
        // Get the productTAndC
        restProductTAndCMockMvc.perform(get("/api/product-t-and-cs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductTAndC() throws Exception {
        // Initialize the database
        productTAndCRepository.saveAndFlush(productTAndC);

        int databaseSizeBeforeUpdate = productTAndCRepository.findAll().size();

        // Update the productTAndC
        ProductTAndC updatedProductTAndC = productTAndCRepository.findById(productTAndC.getId()).get();
        // Disconnect from session so that the updates on updatedProductTAndC are not directly saved in db
        em.detach(updatedProductTAndC);
        updatedProductTAndC
            .tAndCId(UPDATED_T_AND_C_ID)
            .productId(UPDATED_PRODUCT_ID);

        restProductTAndCMockMvc.perform(put("/api/product-t-and-cs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProductTAndC)))
            .andExpect(status().isOk());

        // Validate the ProductTAndC in the database
        List<ProductTAndC> productTAndCList = productTAndCRepository.findAll();
        assertThat(productTAndCList).hasSize(databaseSizeBeforeUpdate);
        ProductTAndC testProductTAndC = productTAndCList.get(productTAndCList.size() - 1);
        assertThat(testProductTAndC.gettAndCId()).isEqualTo(UPDATED_T_AND_C_ID);
        assertThat(testProductTAndC.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingProductTAndC() throws Exception {
        int databaseSizeBeforeUpdate = productTAndCRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductTAndCMockMvc.perform(put("/api/product-t-and-cs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productTAndC)))
            .andExpect(status().isBadRequest());

        // Validate the ProductTAndC in the database
        List<ProductTAndC> productTAndCList = productTAndCRepository.findAll();
        assertThat(productTAndCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProductTAndC() throws Exception {
        // Initialize the database
        productTAndCRepository.saveAndFlush(productTAndC);

        int databaseSizeBeforeDelete = productTAndCRepository.findAll().size();

        // Delete the productTAndC
        restProductTAndCMockMvc.perform(delete("/api/product-t-and-cs/{id}", productTAndC.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductTAndC> productTAndCList = productTAndCRepository.findAll();
        assertThat(productTAndCList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
