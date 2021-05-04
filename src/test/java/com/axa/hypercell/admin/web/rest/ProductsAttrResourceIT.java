package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.ProductsAttr;
import com.axa.hypercell.admin.domain.enumeration.AttrType;
import com.axa.hypercell.admin.domain.enumeration.DataType;
import com.axa.hypercell.admin.repository.ProductsAttrRepository;
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
 * Integration tests for the {@link ProductsAttrResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductsAttrResourceIT {

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final String DEFAULT_ATTRIBUTE_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_ATTRIBUTE_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_ATRIBUTE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ATRIBUTE_NAME = "BBBBBBBBBB";

    private static final DataType DEFAULT_DATA_TYPE = DataType.STRING;
    private static final DataType UPDATED_DATA_TYPE = DataType.NUMBER;

    private static final Boolean DEFAULT_IS_MANDATORY_FOR_QUOTATION = false;
    private static final Boolean UPDATED_IS_MANDATORY_FOR_QUOTATION = true;

    private static final Boolean DEFAULT_IS_MANDATORY_FOR_POLICY = false;
    private static final Boolean UPDATED_IS_MANDATORY_FOR_POLICY = true;

    private static final AttrType DEFAULT_ATTR_TYPE = AttrType.Informative;
    private static final AttrType UPDATED_ATTR_TYPE = AttrType.BreakDown;

    private static final Long DEFAULT_LOOKUP_TYPE_ID = 1L;
    private static final Long UPDATED_LOOKUP_TYPE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/products-attrs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

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
            .atributeName(DEFAULT_ATRIBUTE_NAME)
            .dataType(DEFAULT_DATA_TYPE)
            .isMandatoryForQuotation(DEFAULT_IS_MANDATORY_FOR_QUOTATION)
            .isMandatoryForPolicy(DEFAULT_IS_MANDATORY_FOR_POLICY)
            .attrType(DEFAULT_ATTR_TYPE)
            .lookupTypeId(DEFAULT_LOOKUP_TYPE_ID);
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
            .atributeName(UPDATED_ATRIBUTE_NAME)
            .dataType(UPDATED_DATA_TYPE)
            .isMandatoryForQuotation(UPDATED_IS_MANDATORY_FOR_QUOTATION)
            .isMandatoryForPolicy(UPDATED_IS_MANDATORY_FOR_POLICY)
            .attrType(UPDATED_ATTR_TYPE)
            .lookupTypeId(UPDATED_LOOKUP_TYPE_ID);
        return productsAttr;
    }

    @BeforeEach
    public void initTest() {
        productsAttr = createEntity(em);
    }

    @Test
    @Transactional
    void createProductsAttr() throws Exception {
        int databaseSizeBeforeCreate = productsAttrRepository.findAll().size();
        // Create the ProductsAttr
        restProductsAttrMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productsAttr)))
            .andExpect(status().isCreated());

        // Validate the ProductsAttr in the database
        List<ProductsAttr> productsAttrList = productsAttrRepository.findAll();
        assertThat(productsAttrList).hasSize(databaseSizeBeforeCreate + 1);
        ProductsAttr testProductsAttr = productsAttrList.get(productsAttrList.size() - 1);
        assertThat(testProductsAttr.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testProductsAttr.getAttributeValue()).isEqualTo(DEFAULT_ATTRIBUTE_VALUE);
        assertThat(testProductsAttr.getAtributeName()).isEqualTo(DEFAULT_ATRIBUTE_NAME);
        assertThat(testProductsAttr.getDataType()).isEqualTo(DEFAULT_DATA_TYPE);
        assertThat(testProductsAttr.getIsMandatoryForQuotation()).isEqualTo(DEFAULT_IS_MANDATORY_FOR_QUOTATION);
        assertThat(testProductsAttr.getIsMandatoryForPolicy()).isEqualTo(DEFAULT_IS_MANDATORY_FOR_POLICY);
        assertThat(testProductsAttr.getAttrType()).isEqualTo(DEFAULT_ATTR_TYPE);
        assertThat(testProductsAttr.getLookupTypeId()).isEqualTo(DEFAULT_LOOKUP_TYPE_ID);
    }

    @Test
    @Transactional
    void createProductsAttrWithExistingId() throws Exception {
        // Create the ProductsAttr with an existing ID
        productsAttr.setId(1L);

        int databaseSizeBeforeCreate = productsAttrRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductsAttrMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productsAttr)))
            .andExpect(status().isBadRequest());

        // Validate the ProductsAttr in the database
        List<ProductsAttr> productsAttrList = productsAttrRepository.findAll();
        assertThat(productsAttrList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductsAttrs() throws Exception {
        // Initialize the database
        productsAttrRepository.saveAndFlush(productsAttr);

        // Get all the productsAttrList
        restProductsAttrMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productsAttr.getId().intValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].attributeValue").value(hasItem(DEFAULT_ATTRIBUTE_VALUE)))
            .andExpect(jsonPath("$.[*].atributeName").value(hasItem(DEFAULT_ATRIBUTE_NAME)))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isMandatoryForQuotation").value(hasItem(DEFAULT_IS_MANDATORY_FOR_QUOTATION.booleanValue())))
            .andExpect(jsonPath("$.[*].isMandatoryForPolicy").value(hasItem(DEFAULT_IS_MANDATORY_FOR_POLICY.booleanValue())))
            .andExpect(jsonPath("$.[*].attrType").value(hasItem(DEFAULT_ATTR_TYPE.toString())))
            .andExpect(jsonPath("$.[*].lookupTypeId").value(hasItem(DEFAULT_LOOKUP_TYPE_ID.intValue())));
    }

    @Test
    @Transactional
    void getProductsAttr() throws Exception {
        // Initialize the database
        productsAttrRepository.saveAndFlush(productsAttr);

        // Get the productsAttr
        restProductsAttrMockMvc
            .perform(get(ENTITY_API_URL_ID, productsAttr.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productsAttr.getId().intValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.attributeValue").value(DEFAULT_ATTRIBUTE_VALUE))
            .andExpect(jsonPath("$.atributeName").value(DEFAULT_ATRIBUTE_NAME))
            .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE.toString()))
            .andExpect(jsonPath("$.isMandatoryForQuotation").value(DEFAULT_IS_MANDATORY_FOR_QUOTATION.booleanValue()))
            .andExpect(jsonPath("$.isMandatoryForPolicy").value(DEFAULT_IS_MANDATORY_FOR_POLICY.booleanValue()))
            .andExpect(jsonPath("$.attrType").value(DEFAULT_ATTR_TYPE.toString()))
            .andExpect(jsonPath("$.lookupTypeId").value(DEFAULT_LOOKUP_TYPE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingProductsAttr() throws Exception {
        // Get the productsAttr
        restProductsAttrMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductsAttr() throws Exception {
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
            .atributeName(UPDATED_ATRIBUTE_NAME)
            .dataType(UPDATED_DATA_TYPE)
            .isMandatoryForQuotation(UPDATED_IS_MANDATORY_FOR_QUOTATION)
            .isMandatoryForPolicy(UPDATED_IS_MANDATORY_FOR_POLICY)
            .attrType(UPDATED_ATTR_TYPE)
            .lookupTypeId(UPDATED_LOOKUP_TYPE_ID);

        restProductsAttrMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductsAttr.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductsAttr))
            )
            .andExpect(status().isOk());

        // Validate the ProductsAttr in the database
        List<ProductsAttr> productsAttrList = productsAttrRepository.findAll();
        assertThat(productsAttrList).hasSize(databaseSizeBeforeUpdate);
        ProductsAttr testProductsAttr = productsAttrList.get(productsAttrList.size() - 1);
        assertThat(testProductsAttr.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProductsAttr.getAttributeValue()).isEqualTo(UPDATED_ATTRIBUTE_VALUE);
        assertThat(testProductsAttr.getAtributeName()).isEqualTo(UPDATED_ATRIBUTE_NAME);
        assertThat(testProductsAttr.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
        assertThat(testProductsAttr.getIsMandatoryForQuotation()).isEqualTo(UPDATED_IS_MANDATORY_FOR_QUOTATION);
        assertThat(testProductsAttr.getIsMandatoryForPolicy()).isEqualTo(UPDATED_IS_MANDATORY_FOR_POLICY);
        assertThat(testProductsAttr.getAttrType()).isEqualTo(UPDATED_ATTR_TYPE);
        assertThat(testProductsAttr.getLookupTypeId()).isEqualTo(UPDATED_LOOKUP_TYPE_ID);
    }

    @Test
    @Transactional
    void putNonExistingProductsAttr() throws Exception {
        int databaseSizeBeforeUpdate = productsAttrRepository.findAll().size();
        productsAttr.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductsAttrMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productsAttr.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productsAttr))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductsAttr in the database
        List<ProductsAttr> productsAttrList = productsAttrRepository.findAll();
        assertThat(productsAttrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductsAttr() throws Exception {
        int databaseSizeBeforeUpdate = productsAttrRepository.findAll().size();
        productsAttr.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsAttrMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productsAttr))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductsAttr in the database
        List<ProductsAttr> productsAttrList = productsAttrRepository.findAll();
        assertThat(productsAttrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductsAttr() throws Exception {
        int databaseSizeBeforeUpdate = productsAttrRepository.findAll().size();
        productsAttr.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsAttrMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productsAttr)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductsAttr in the database
        List<ProductsAttr> productsAttrList = productsAttrRepository.findAll();
        assertThat(productsAttrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductsAttrWithPatch() throws Exception {
        // Initialize the database
        productsAttrRepository.saveAndFlush(productsAttr);

        int databaseSizeBeforeUpdate = productsAttrRepository.findAll().size();

        // Update the productsAttr using partial update
        ProductsAttr partialUpdatedProductsAttr = new ProductsAttr();
        partialUpdatedProductsAttr.setId(productsAttr.getId());

        partialUpdatedProductsAttr
            .productId(UPDATED_PRODUCT_ID)
            .attributeValue(UPDATED_ATTRIBUTE_VALUE)
            .dataType(UPDATED_DATA_TYPE)
            .attrType(UPDATED_ATTR_TYPE)
            .lookupTypeId(UPDATED_LOOKUP_TYPE_ID);

        restProductsAttrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductsAttr.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductsAttr))
            )
            .andExpect(status().isOk());

        // Validate the ProductsAttr in the database
        List<ProductsAttr> productsAttrList = productsAttrRepository.findAll();
        assertThat(productsAttrList).hasSize(databaseSizeBeforeUpdate);
        ProductsAttr testProductsAttr = productsAttrList.get(productsAttrList.size() - 1);
        assertThat(testProductsAttr.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProductsAttr.getAttributeValue()).isEqualTo(UPDATED_ATTRIBUTE_VALUE);
        assertThat(testProductsAttr.getAtributeName()).isEqualTo(DEFAULT_ATRIBUTE_NAME);
        assertThat(testProductsAttr.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
        assertThat(testProductsAttr.getIsMandatoryForQuotation()).isEqualTo(DEFAULT_IS_MANDATORY_FOR_QUOTATION);
        assertThat(testProductsAttr.getIsMandatoryForPolicy()).isEqualTo(DEFAULT_IS_MANDATORY_FOR_POLICY);
        assertThat(testProductsAttr.getAttrType()).isEqualTo(UPDATED_ATTR_TYPE);
        assertThat(testProductsAttr.getLookupTypeId()).isEqualTo(UPDATED_LOOKUP_TYPE_ID);
    }

    @Test
    @Transactional
    void fullUpdateProductsAttrWithPatch() throws Exception {
        // Initialize the database
        productsAttrRepository.saveAndFlush(productsAttr);

        int databaseSizeBeforeUpdate = productsAttrRepository.findAll().size();

        // Update the productsAttr using partial update
        ProductsAttr partialUpdatedProductsAttr = new ProductsAttr();
        partialUpdatedProductsAttr.setId(productsAttr.getId());

        partialUpdatedProductsAttr
            .productId(UPDATED_PRODUCT_ID)
            .attributeValue(UPDATED_ATTRIBUTE_VALUE)
            .atributeName(UPDATED_ATRIBUTE_NAME)
            .dataType(UPDATED_DATA_TYPE)
            .isMandatoryForQuotation(UPDATED_IS_MANDATORY_FOR_QUOTATION)
            .isMandatoryForPolicy(UPDATED_IS_MANDATORY_FOR_POLICY)
            .attrType(UPDATED_ATTR_TYPE)
            .lookupTypeId(UPDATED_LOOKUP_TYPE_ID);

        restProductsAttrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductsAttr.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductsAttr))
            )
            .andExpect(status().isOk());

        // Validate the ProductsAttr in the database
        List<ProductsAttr> productsAttrList = productsAttrRepository.findAll();
        assertThat(productsAttrList).hasSize(databaseSizeBeforeUpdate);
        ProductsAttr testProductsAttr = productsAttrList.get(productsAttrList.size() - 1);
        assertThat(testProductsAttr.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testProductsAttr.getAttributeValue()).isEqualTo(UPDATED_ATTRIBUTE_VALUE);
        assertThat(testProductsAttr.getAtributeName()).isEqualTo(UPDATED_ATRIBUTE_NAME);
        assertThat(testProductsAttr.getDataType()).isEqualTo(UPDATED_DATA_TYPE);
        assertThat(testProductsAttr.getIsMandatoryForQuotation()).isEqualTo(UPDATED_IS_MANDATORY_FOR_QUOTATION);
        assertThat(testProductsAttr.getIsMandatoryForPolicy()).isEqualTo(UPDATED_IS_MANDATORY_FOR_POLICY);
        assertThat(testProductsAttr.getAttrType()).isEqualTo(UPDATED_ATTR_TYPE);
        assertThat(testProductsAttr.getLookupTypeId()).isEqualTo(UPDATED_LOOKUP_TYPE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingProductsAttr() throws Exception {
        int databaseSizeBeforeUpdate = productsAttrRepository.findAll().size();
        productsAttr.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductsAttrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productsAttr.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productsAttr))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductsAttr in the database
        List<ProductsAttr> productsAttrList = productsAttrRepository.findAll();
        assertThat(productsAttrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductsAttr() throws Exception {
        int databaseSizeBeforeUpdate = productsAttrRepository.findAll().size();
        productsAttr.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsAttrMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productsAttr))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductsAttr in the database
        List<ProductsAttr> productsAttrList = productsAttrRepository.findAll();
        assertThat(productsAttrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductsAttr() throws Exception {
        int databaseSizeBeforeUpdate = productsAttrRepository.findAll().size();
        productsAttr.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductsAttrMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(productsAttr))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductsAttr in the database
        List<ProductsAttr> productsAttrList = productsAttrRepository.findAll();
        assertThat(productsAttrList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductsAttr() throws Exception {
        // Initialize the database
        productsAttrRepository.saveAndFlush(productsAttr);

        int databaseSizeBeforeDelete = productsAttrRepository.findAll().size();

        // Delete the productsAttr
        restProductsAttrMockMvc
            .perform(delete(ENTITY_API_URL_ID, productsAttr.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductsAttr> productsAttrList = productsAttrRepository.findAll();
        assertThat(productsAttrList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
