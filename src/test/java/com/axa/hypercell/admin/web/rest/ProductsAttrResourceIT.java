package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.AdminserviceApp;
import com.axa.hypercell.admin.domain.ProductsAttr;
import com.axa.hypercell.admin.repository.ProductsAttrRepository;

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

import com.axa.hypercell.admin.domain.enumeration.AttributeName;
/**
 * Integration tests for the {@link ProductsAttrResource} REST controller.
 */
@SpringBootTest(classes = AdminserviceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProductsAttrResourceIT {

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final String DEFAULT_ATTRIBUTE_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTE_VALUE = "BBBBBBBBBB";

    private static final AttributeName DEFAULT_ATRIBUTE_NAME = AttributeName.TAX;
    private static final AttributeName UPDATED_ATRIBUTE_NAME = AttributeName.RATE;

    @Autowired
    private ProductsAttrRepository productsAttrRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductsAttrMockMvc;

    private ProductsAttr productsAttr;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductsAttr createEntity(EntityManager em) {
        ProductsAttr productsAttr = new ProductsAttr()
            .productId(DEFAULT_PRODUCT_ID)
            .attributeValue(DEFAULT_ATTRIBUTE_VALUE)
            .atributeName(DEFAULT_ATRIBUTE_NAME);
        return productsAttr;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductsAttr createUpdatedEntity(EntityManager em) {
        ProductsAttr productsAttr = new ProductsAttr()
            .productId(UPDATED_PRODUCT_ID)
            .attributeValue(UPDATED_ATTRIBUTE_VALUE)
            .atributeName(UPDATED_ATRIBUTE_NAME);
        return productsAttr;
    }

    @BeforeEach
    public void initTest() {
        productsAttr = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductsAttr() throws Exception {
        int databaseSizeBeforeCreate = productsAttrRepository.findAll().size();
        // Create the ProductsAttr
        restProductsAttrMockMvc.perform(post("/api/products-attrs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productsAttr)))
            .andExpect(status().isCreated());

        // Validate the ProductsAttr in the database
        List<ProductsAttr> productsAttrList = productsAttrRepository.findAll();
        assertThat(productsAttrList).hasSize(databaseSizeBeforeCreate + 1);
        ProductsAttr testProductsAttr = productsAttrList.get(productsAttrList.size() - 1);
        assertThat(testProductsAttr.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testProductsAttr.getAttributeValue()).isEqualTo(DEFAULT_ATTRIBUTE_VALUE);
        assertThat(testProductsAttr.getAtributeName()).isEqualTo(DEFAULT_ATRIBUTE_NAME);
    }

    @Test
    @Transactional
    public void createProductsAttrWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productsAttrRepository.findAll().size();

        // Create the ProductsAttr with an existing ID
        productsAttr.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductsAttrMockMvc.perform(post("/api/products-attrs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productsAttr)))
            .andExpect(status().isBadRequest());

        // Validate the ProductsAttr in the database
        List<ProductsAttr> productsAttrList = productsAttrRepository.findAll();
        assertThat(productsAttrList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllProductsAttrs() throws Exception {
        // Initialize the database
        productsAttrRepository.saveAndFlush(productsAttr);

        // Get all the productsAttrList
        restProductsAttrMockMvc.perform(get("/api/products-attrs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productsAttr.getId().intValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].attributeValue").value(hasItem(DEFAULT_ATTRIBUTE_VALUE)))
            .andExpect(jsonPath("$.[*].atributeName").value(hasItem(DEFAULT_ATRIBUTE_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getProductsAttr() throws Exception {
        // Initialize the database
        productsAttrRepository.saveAndFlush(productsAttr);

        // Get the productsAttr
        restProductsAttrMockMvc.perform(get("/api/products-attrs/{id}", productsAttr.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productsAttr.getId().intValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.attributeValue").value(DEFAULT_ATTRIBUTE_VALUE))
            .andExpect(jsonPath("$.atributeName").value(DEFAULT_ATRIBUTE_NAME.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingProductsAttr() throws Exception {
        // Get the productsAttr
        restProductsAttrMockMvc.perform(get("/api/products-attrs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductsAttr() throws Exception {
        // Initialize the database
        productsAttrRepository.saveAndFlush(productsAttr);

        int databaseSizeBeforeUpdate = productsAttrRepository.findAll().size();

        // Update the productsAttr
        ProductsAttr updatedProductsAttr = productsAttrRepository.findById(productsAttr.getId()).get();
        // Disconnect from session so that the updates on updatedProductsAttr are not directly saved in db
        em.detach(updatedProductsAttr);
        updatedProductsAttr
            .productId(UPDATED_PRODUCT_ID)
            .attributeValue(UPDATED_ATTRIBUTE_VALUE)
            .atributeName(UPDATED_ATRIBUTE_NAME);

        restProductsAttrMockMvc.perform(put("/api/products-attrs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProductsAttr)))
            .andExpect(status().isOk());

        // Validate the ProductsAttr in the database
        List<ProductsAttr> productsAttrList = productsAttrRepository.findAll();
        assertThat(productsAttrList).hasSize(databaseSizeBeforeUpdate);
        ProductsAttr testProductsAttr = productsAttrList.get(productsAttrList.size() - 1);
        assertThat(testProductsAttr.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProductsAttr.getAttributeValue()).isEqualTo(UPDATED_ATTRIBUTE_VALUE);
        assertThat(testProductsAttr.getAtributeName()).isEqualTo(UPDATED_ATRIBUTE_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingProductsAttr() throws Exception {
        int databaseSizeBeforeUpdate = productsAttrRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductsAttrMockMvc.perform(put("/api/products-attrs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productsAttr)))
            .andExpect(status().isBadRequest());

        // Validate the ProductsAttr in the database
        List<ProductsAttr> productsAttrList = productsAttrRepository.findAll();
        assertThat(productsAttrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProductsAttr() throws Exception {
        // Initialize the database
        productsAttrRepository.saveAndFlush(productsAttr);

        int databaseSizeBeforeDelete = productsAttrRepository.findAll().size();

        // Delete the productsAttr
        restProductsAttrMockMvc.perform(delete("/api/products-attrs/{id}", productsAttr.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductsAttr> productsAttrList = productsAttrRepository.findAll();
        assertThat(productsAttrList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
