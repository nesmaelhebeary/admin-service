package com.axa.hypercell.admin.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axa.hypercell.admin.IntegrationTest;
import com.axa.hypercell.admin.domain.TreatyDetails;
import com.axa.hypercell.admin.domain.enumeration.ClassificationType;
import com.axa.hypercell.admin.domain.enumeration.RiskType;
import com.axa.hypercell.admin.repository.TreatyDetailsRepository;
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
 * Integration tests for the {@link TreatyDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TreatyDetailsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_TREATY_ID = 1L;
    private static final Long UPDATED_TREATY_ID = 2L;

    private static final Double DEFAULT_MAXIMUM_LIMIT = 1D;
    private static final Double UPDATED_MAXIMUM_LIMIT = 2D;

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final Double DEFAULT_MIN_LIMIT = 1D;
    private static final Double UPDATED_MIN_LIMIT = 2D;

    private static final Double DEFAULT_RETAINED_AMOUNT = 1D;
    private static final Double UPDATED_RETAINED_AMOUNT = 2D;

    private static final Double DEFAULT_CEDED_AMOUNT = 1D;
    private static final Double UPDATED_CEDED_AMOUNT = 2D;

    private static final Double DEFAULT_RETAINED_PERCENATGE = 1D;
    private static final Double UPDATED_RETAINED_PERCENATGE = 2D;

    private static final Double DEFAULT_CEDED_PERCENATGE = 1D;
    private static final Double UPDATED_CEDED_PERCENATGE = 2D;

    private static final Double DEFAULT_SURPLUS = 1D;
    private static final Double UPDATED_SURPLUS = 2D;

    private static final ClassificationType DEFAULT_CLASSIFICATION_TYPE = ClassificationType.None;
    private static final ClassificationType UPDATED_CLASSIFICATION_TYPE = ClassificationType.NaceCode;

    private static final String DEFAULT_NACE_CODE_CLASSIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_NACE_CODE_CLASSIFICATION = "BBBBBBBBBB";

    private static final String DEFAULT_OTHER_CLASSIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_OTHER_CLASSIFICATION = "BBBBBBBBBB";

    private static final RiskType DEFAULT_RISK_TYPE = RiskType.AnyoneRisk;
    private static final RiskType UPDATED_RISK_TYPE = RiskType.AnyoneAccumulatedRisk;

    private static final String ENTITY_API_URL = "/api/treaty-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TreatyDetailsRepository treatyDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTreatyDetailsMockMvc;

    private TreatyDetails treatyDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TreatyDetails createEntity(EntityManager em) {
        TreatyDetails treatyDetails = new TreatyDetails()
            .name(DEFAULT_NAME)
            .treatyId(DEFAULT_TREATY_ID)
            .maximumLimit(DEFAULT_MAXIMUM_LIMIT)
            .productId(DEFAULT_PRODUCT_ID)
            .minLimit(DEFAULT_MIN_LIMIT)
            .retainedAmount(DEFAULT_RETAINED_AMOUNT)
            .cededAmount(DEFAULT_CEDED_AMOUNT)
            .retainedPercenatge(DEFAULT_RETAINED_PERCENATGE)
            .cededPercenatge(DEFAULT_CEDED_PERCENATGE)
            .surplus(DEFAULT_SURPLUS)
            .classificationType(DEFAULT_CLASSIFICATION_TYPE)
            .naceCodeClassification(DEFAULT_NACE_CODE_CLASSIFICATION)
            .otherClassification(DEFAULT_OTHER_CLASSIFICATION)
            .riskType(DEFAULT_RISK_TYPE);
        return treatyDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TreatyDetails createUpdatedEntity(EntityManager em) {
        TreatyDetails treatyDetails = new TreatyDetails()
            .name(UPDATED_NAME)
            .treatyId(UPDATED_TREATY_ID)
            .maximumLimit(UPDATED_MAXIMUM_LIMIT)
            .productId(UPDATED_PRODUCT_ID)
            .minLimit(UPDATED_MIN_LIMIT)
            .retainedAmount(UPDATED_RETAINED_AMOUNT)
            .cededAmount(UPDATED_CEDED_AMOUNT)
            .retainedPercenatge(UPDATED_RETAINED_PERCENATGE)
            .cededPercenatge(UPDATED_CEDED_PERCENATGE)
            .surplus(UPDATED_SURPLUS)
            .classificationType(UPDATED_CLASSIFICATION_TYPE)
            .naceCodeClassification(UPDATED_NACE_CODE_CLASSIFICATION)
            .otherClassification(UPDATED_OTHER_CLASSIFICATION)
            .riskType(UPDATED_RISK_TYPE);
        return treatyDetails;
    }

    @BeforeEach
    public void initTest() {
        treatyDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createTreatyDetails() throws Exception {
        int databaseSizeBeforeCreate = treatyDetailsRepository.findAll().size();
        // Create the TreatyDetails
        restTreatyDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(treatyDetails)))
            .andExpect(status().isCreated());

        // Validate the TreatyDetails in the database
        List<TreatyDetails> treatyDetailsList = treatyDetailsRepository.findAll();
        assertThat(treatyDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        TreatyDetails testTreatyDetails = treatyDetailsList.get(treatyDetailsList.size() - 1);
        assertThat(testTreatyDetails.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTreatyDetails.getTreatyId()).isEqualTo(DEFAULT_TREATY_ID);
        assertThat(testTreatyDetails.getMaximumLimit()).isEqualTo(DEFAULT_MAXIMUM_LIMIT);
        assertThat(testTreatyDetails.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testTreatyDetails.getMinLimit()).isEqualTo(DEFAULT_MIN_LIMIT);
        assertThat(testTreatyDetails.getRetainedAmount()).isEqualTo(DEFAULT_RETAINED_AMOUNT);
        assertThat(testTreatyDetails.getCededAmount()).isEqualTo(DEFAULT_CEDED_AMOUNT);
        assertThat(testTreatyDetails.getRetainedPercenatge()).isEqualTo(DEFAULT_RETAINED_PERCENATGE);
        assertThat(testTreatyDetails.getCededPercenatge()).isEqualTo(DEFAULT_CEDED_PERCENATGE);
        assertThat(testTreatyDetails.getSurplus()).isEqualTo(DEFAULT_SURPLUS);
        assertThat(testTreatyDetails.getClassificationType()).isEqualTo(DEFAULT_CLASSIFICATION_TYPE);
        assertThat(testTreatyDetails.getNaceCodeClassification()).isEqualTo(DEFAULT_NACE_CODE_CLASSIFICATION);
        assertThat(testTreatyDetails.getOtherClassification()).isEqualTo(DEFAULT_OTHER_CLASSIFICATION);
        assertThat(testTreatyDetails.getRiskType()).isEqualTo(DEFAULT_RISK_TYPE);
    }

    @Test
    @Transactional
    void createTreatyDetailsWithExistingId() throws Exception {
        // Create the TreatyDetails with an existing ID
        treatyDetails.setId(1L);

        int databaseSizeBeforeCreate = treatyDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTreatyDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(treatyDetails)))
            .andExpect(status().isBadRequest());

        // Validate the TreatyDetails in the database
        List<TreatyDetails> treatyDetailsList = treatyDetailsRepository.findAll();
        assertThat(treatyDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTreatyDetails() throws Exception {
        // Initialize the database
        treatyDetailsRepository.saveAndFlush(treatyDetails);

        // Get all the treatyDetailsList
        restTreatyDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(treatyDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].treatyId").value(hasItem(DEFAULT_TREATY_ID.intValue())))
            .andExpect(jsonPath("$.[*].maximumLimit").value(hasItem(DEFAULT_MAXIMUM_LIMIT.doubleValue())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].minLimit").value(hasItem(DEFAULT_MIN_LIMIT.doubleValue())))
            .andExpect(jsonPath("$.[*].retainedAmount").value(hasItem(DEFAULT_RETAINED_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].cededAmount").value(hasItem(DEFAULT_CEDED_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].retainedPercenatge").value(hasItem(DEFAULT_RETAINED_PERCENATGE.doubleValue())))
            .andExpect(jsonPath("$.[*].cededPercenatge").value(hasItem(DEFAULT_CEDED_PERCENATGE.doubleValue())))
            .andExpect(jsonPath("$.[*].surplus").value(hasItem(DEFAULT_SURPLUS.doubleValue())))
            .andExpect(jsonPath("$.[*].classificationType").value(hasItem(DEFAULT_CLASSIFICATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].naceCodeClassification").value(hasItem(DEFAULT_NACE_CODE_CLASSIFICATION)))
            .andExpect(jsonPath("$.[*].otherClassification").value(hasItem(DEFAULT_OTHER_CLASSIFICATION)))
            .andExpect(jsonPath("$.[*].riskType").value(hasItem(DEFAULT_RISK_TYPE.toString())));
    }

    @Test
    @Transactional
    void getTreatyDetails() throws Exception {
        // Initialize the database
        treatyDetailsRepository.saveAndFlush(treatyDetails);

        // Get the treatyDetails
        restTreatyDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, treatyDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(treatyDetails.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.treatyId").value(DEFAULT_TREATY_ID.intValue()))
            .andExpect(jsonPath("$.maximumLimit").value(DEFAULT_MAXIMUM_LIMIT.doubleValue()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.minLimit").value(DEFAULT_MIN_LIMIT.doubleValue()))
            .andExpect(jsonPath("$.retainedAmount").value(DEFAULT_RETAINED_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.cededAmount").value(DEFAULT_CEDED_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.retainedPercenatge").value(DEFAULT_RETAINED_PERCENATGE.doubleValue()))
            .andExpect(jsonPath("$.cededPercenatge").value(DEFAULT_CEDED_PERCENATGE.doubleValue()))
            .andExpect(jsonPath("$.surplus").value(DEFAULT_SURPLUS.doubleValue()))
            .andExpect(jsonPath("$.classificationType").value(DEFAULT_CLASSIFICATION_TYPE.toString()))
            .andExpect(jsonPath("$.naceCodeClassification").value(DEFAULT_NACE_CODE_CLASSIFICATION))
            .andExpect(jsonPath("$.otherClassification").value(DEFAULT_OTHER_CLASSIFICATION))
            .andExpect(jsonPath("$.riskType").value(DEFAULT_RISK_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTreatyDetails() throws Exception {
        // Get the treatyDetails
        restTreatyDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTreatyDetails() throws Exception {
        // Initialize the database
        treatyDetailsRepository.saveAndFlush(treatyDetails);

        int databaseSizeBeforeUpdate = treatyDetailsRepository.findAll().size();

        // Update the treatyDetails
        TreatyDetails updatedTreatyDetails = treatyDetailsRepository.findById(treatyDetails.getId()).get();
        // Disconnect from session so that the updates on updatedTreatyDetails are not directly saved in db
        em.detach(updatedTreatyDetails);
        updatedTreatyDetails
            .name(UPDATED_NAME)
            .treatyId(UPDATED_TREATY_ID)
            .maximumLimit(UPDATED_MAXIMUM_LIMIT)
            .productId(UPDATED_PRODUCT_ID)
            .minLimit(UPDATED_MIN_LIMIT)
            .retainedAmount(UPDATED_RETAINED_AMOUNT)
            .cededAmount(UPDATED_CEDED_AMOUNT)
            .retainedPercenatge(UPDATED_RETAINED_PERCENATGE)
            .cededPercenatge(UPDATED_CEDED_PERCENATGE)
            .surplus(UPDATED_SURPLUS)
            .classificationType(UPDATED_CLASSIFICATION_TYPE)
            .naceCodeClassification(UPDATED_NACE_CODE_CLASSIFICATION)
            .otherClassification(UPDATED_OTHER_CLASSIFICATION)
            .riskType(UPDATED_RISK_TYPE);

        restTreatyDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTreatyDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTreatyDetails))
            )
            .andExpect(status().isOk());

        // Validate the TreatyDetails in the database
        List<TreatyDetails> treatyDetailsList = treatyDetailsRepository.findAll();
        assertThat(treatyDetailsList).hasSize(databaseSizeBeforeUpdate);
        TreatyDetails testTreatyDetails = treatyDetailsList.get(treatyDetailsList.size() - 1);
        assertThat(testTreatyDetails.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTreatyDetails.getTreatyId()).isEqualTo(UPDATED_TREATY_ID);
        assertThat(testTreatyDetails.getMaximumLimit()).isEqualTo(UPDATED_MAXIMUM_LIMIT);
        assertThat(testTreatyDetails.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testTreatyDetails.getMinLimit()).isEqualTo(UPDATED_MIN_LIMIT);
        assertThat(testTreatyDetails.getRetainedAmount()).isEqualTo(UPDATED_RETAINED_AMOUNT);
        assertThat(testTreatyDetails.getCededAmount()).isEqualTo(UPDATED_CEDED_AMOUNT);
        assertThat(testTreatyDetails.getRetainedPercenatge()).isEqualTo(UPDATED_RETAINED_PERCENATGE);
        assertThat(testTreatyDetails.getCededPercenatge()).isEqualTo(UPDATED_CEDED_PERCENATGE);
        assertThat(testTreatyDetails.getSurplus()).isEqualTo(UPDATED_SURPLUS);
        assertThat(testTreatyDetails.getClassificationType()).isEqualTo(UPDATED_CLASSIFICATION_TYPE);
        assertThat(testTreatyDetails.getNaceCodeClassification()).isEqualTo(UPDATED_NACE_CODE_CLASSIFICATION);
        assertThat(testTreatyDetails.getOtherClassification()).isEqualTo(UPDATED_OTHER_CLASSIFICATION);
        assertThat(testTreatyDetails.getRiskType()).isEqualTo(UPDATED_RISK_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingTreatyDetails() throws Exception {
        int databaseSizeBeforeUpdate = treatyDetailsRepository.findAll().size();
        treatyDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTreatyDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, treatyDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(treatyDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TreatyDetails in the database
        List<TreatyDetails> treatyDetailsList = treatyDetailsRepository.findAll();
        assertThat(treatyDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTreatyDetails() throws Exception {
        int databaseSizeBeforeUpdate = treatyDetailsRepository.findAll().size();
        treatyDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTreatyDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(treatyDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TreatyDetails in the database
        List<TreatyDetails> treatyDetailsList = treatyDetailsRepository.findAll();
        assertThat(treatyDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTreatyDetails() throws Exception {
        int databaseSizeBeforeUpdate = treatyDetailsRepository.findAll().size();
        treatyDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTreatyDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(treatyDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TreatyDetails in the database
        List<TreatyDetails> treatyDetailsList = treatyDetailsRepository.findAll();
        assertThat(treatyDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTreatyDetailsWithPatch() throws Exception {
        // Initialize the database
        treatyDetailsRepository.saveAndFlush(treatyDetails);

        int databaseSizeBeforeUpdate = treatyDetailsRepository.findAll().size();

        // Update the treatyDetails using partial update
        TreatyDetails partialUpdatedTreatyDetails = new TreatyDetails();
        partialUpdatedTreatyDetails.setId(treatyDetails.getId());

        partialUpdatedTreatyDetails
            .name(UPDATED_NAME)
            .maximumLimit(UPDATED_MAXIMUM_LIMIT)
            .retainedPercenatge(UPDATED_RETAINED_PERCENATGE)
            .cededPercenatge(UPDATED_CEDED_PERCENATGE)
            .surplus(UPDATED_SURPLUS);

        restTreatyDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTreatyDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTreatyDetails))
            )
            .andExpect(status().isOk());

        // Validate the TreatyDetails in the database
        List<TreatyDetails> treatyDetailsList = treatyDetailsRepository.findAll();
        assertThat(treatyDetailsList).hasSize(databaseSizeBeforeUpdate);
        TreatyDetails testTreatyDetails = treatyDetailsList.get(treatyDetailsList.size() - 1);
        assertThat(testTreatyDetails.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTreatyDetails.getTreatyId()).isEqualTo(DEFAULT_TREATY_ID);
        assertThat(testTreatyDetails.getMaximumLimit()).isEqualTo(UPDATED_MAXIMUM_LIMIT);
        assertThat(testTreatyDetails.getProductId()).isEqualTo(DEFAULT_PRODUCT_ID);
        assertThat(testTreatyDetails.getMinLimit()).isEqualTo(DEFAULT_MIN_LIMIT);
        assertThat(testTreatyDetails.getRetainedAmount()).isEqualTo(DEFAULT_RETAINED_AMOUNT);
        assertThat(testTreatyDetails.getCededAmount()).isEqualTo(DEFAULT_CEDED_AMOUNT);
        assertThat(testTreatyDetails.getRetainedPercenatge()).isEqualTo(UPDATED_RETAINED_PERCENATGE);
        assertThat(testTreatyDetails.getCededPercenatge()).isEqualTo(UPDATED_CEDED_PERCENATGE);
        assertThat(testTreatyDetails.getSurplus()).isEqualTo(UPDATED_SURPLUS);
        assertThat(testTreatyDetails.getClassificationType()).isEqualTo(DEFAULT_CLASSIFICATION_TYPE);
        assertThat(testTreatyDetails.getNaceCodeClassification()).isEqualTo(DEFAULT_NACE_CODE_CLASSIFICATION);
        assertThat(testTreatyDetails.getOtherClassification()).isEqualTo(DEFAULT_OTHER_CLASSIFICATION);
        assertThat(testTreatyDetails.getRiskType()).isEqualTo(DEFAULT_RISK_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateTreatyDetailsWithPatch() throws Exception {
        // Initialize the database
        treatyDetailsRepository.saveAndFlush(treatyDetails);

        int databaseSizeBeforeUpdate = treatyDetailsRepository.findAll().size();

        // Update the treatyDetails using partial update
        TreatyDetails partialUpdatedTreatyDetails = new TreatyDetails();
        partialUpdatedTreatyDetails.setId(treatyDetails.getId());

        partialUpdatedTreatyDetails
            .name(UPDATED_NAME)
            .treatyId(UPDATED_TREATY_ID)
            .maximumLimit(UPDATED_MAXIMUM_LIMIT)
            .productId(UPDATED_PRODUCT_ID)
            .minLimit(UPDATED_MIN_LIMIT)
            .retainedAmount(UPDATED_RETAINED_AMOUNT)
            .cededAmount(UPDATED_CEDED_AMOUNT)
            .retainedPercenatge(UPDATED_RETAINED_PERCENATGE)
            .cededPercenatge(UPDATED_CEDED_PERCENATGE)
            .surplus(UPDATED_SURPLUS)
            .classificationType(UPDATED_CLASSIFICATION_TYPE)
            .naceCodeClassification(UPDATED_NACE_CODE_CLASSIFICATION)
            .otherClassification(UPDATED_OTHER_CLASSIFICATION)
            .riskType(UPDATED_RISK_TYPE);

        restTreatyDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTreatyDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTreatyDetails))
            )
            .andExpect(status().isOk());

        // Validate the TreatyDetails in the database
        List<TreatyDetails> treatyDetailsList = treatyDetailsRepository.findAll();
        assertThat(treatyDetailsList).hasSize(databaseSizeBeforeUpdate);
        TreatyDetails testTreatyDetails = treatyDetailsList.get(treatyDetailsList.size() - 1);
        assertThat(testTreatyDetails.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTreatyDetails.getTreatyId()).isEqualTo(UPDATED_TREATY_ID);
        assertThat(testTreatyDetails.getMaximumLimit()).isEqualTo(UPDATED_MAXIMUM_LIMIT);
        assertThat(testTreatyDetails.getProductId()).isEqualTo(UPDATED_PRODUCT_ID);
        assertThat(testTreatyDetails.getMinLimit()).isEqualTo(UPDATED_MIN_LIMIT);
        assertThat(testTreatyDetails.getRetainedAmount()).isEqualTo(UPDATED_RETAINED_AMOUNT);
        assertThat(testTreatyDetails.getCededAmount()).isEqualTo(UPDATED_CEDED_AMOUNT);
        assertThat(testTreatyDetails.getRetainedPercenatge()).isEqualTo(UPDATED_RETAINED_PERCENATGE);
        assertThat(testTreatyDetails.getCededPercenatge()).isEqualTo(UPDATED_CEDED_PERCENATGE);
        assertThat(testTreatyDetails.getSurplus()).isEqualTo(UPDATED_SURPLUS);
        assertThat(testTreatyDetails.getClassificationType()).isEqualTo(UPDATED_CLASSIFICATION_TYPE);
        assertThat(testTreatyDetails.getNaceCodeClassification()).isEqualTo(UPDATED_NACE_CODE_CLASSIFICATION);
        assertThat(testTreatyDetails.getOtherClassification()).isEqualTo(UPDATED_OTHER_CLASSIFICATION);
        assertThat(testTreatyDetails.getRiskType()).isEqualTo(UPDATED_RISK_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingTreatyDetails() throws Exception {
        int databaseSizeBeforeUpdate = treatyDetailsRepository.findAll().size();
        treatyDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTreatyDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, treatyDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(treatyDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TreatyDetails in the database
        List<TreatyDetails> treatyDetailsList = treatyDetailsRepository.findAll();
        assertThat(treatyDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTreatyDetails() throws Exception {
        int databaseSizeBeforeUpdate = treatyDetailsRepository.findAll().size();
        treatyDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTreatyDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(treatyDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TreatyDetails in the database
        List<TreatyDetails> treatyDetailsList = treatyDetailsRepository.findAll();
        assertThat(treatyDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTreatyDetails() throws Exception {
        int databaseSizeBeforeUpdate = treatyDetailsRepository.findAll().size();
        treatyDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTreatyDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(treatyDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TreatyDetails in the database
        List<TreatyDetails> treatyDetailsList = treatyDetailsRepository.findAll();
        assertThat(treatyDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTreatyDetails() throws Exception {
        // Initialize the database
        treatyDetailsRepository.saveAndFlush(treatyDetails);

        int databaseSizeBeforeDelete = treatyDetailsRepository.findAll().size();

        // Delete the treatyDetails
        restTreatyDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, treatyDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TreatyDetails> treatyDetailsList = treatyDetailsRepository.findAll();
        assertThat(treatyDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
