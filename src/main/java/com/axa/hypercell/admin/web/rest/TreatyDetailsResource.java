package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.TreatyDetails;
import com.axa.hypercell.admin.repository.TreatyDetailsRepository;
import com.axa.hypercell.admin.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.axa.hypercell.admin.domain.TreatyDetails}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TreatyDetailsResource {

    private final Logger log = LoggerFactory.getLogger(TreatyDetailsResource.class);

    private static final String ENTITY_NAME = "adminserviceTreatyDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TreatyDetailsRepository treatyDetailsRepository;

    public TreatyDetailsResource(TreatyDetailsRepository treatyDetailsRepository) {
        this.treatyDetailsRepository = treatyDetailsRepository;
    }

    /**
     * {@code POST  /treaty-details} : Create a new treatyDetails.
     *
     * @param treatyDetails the treatyDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new treatyDetails, or with status {@code 400 (Bad Request)} if the treatyDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/treaty-details")
    public ResponseEntity<TreatyDetails> createTreatyDetails(@RequestBody TreatyDetails treatyDetails) throws URISyntaxException {
        log.debug("REST request to save TreatyDetails : {}", treatyDetails);
        if (treatyDetails.getId() != null) {
            throw new BadRequestAlertException("A new treatyDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TreatyDetails result = treatyDetailsRepository.save(treatyDetails);
        return ResponseEntity
            .created(new URI("/api/treaty-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /treaty-details/:id} : Updates an existing treatyDetails.
     *
     * @param id the id of the treatyDetails to save.
     * @param treatyDetails the treatyDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated treatyDetails,
     * or with status {@code 400 (Bad Request)} if the treatyDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the treatyDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/treaty-details/{id}")
    public ResponseEntity<TreatyDetails> updateTreatyDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TreatyDetails treatyDetails
    ) throws URISyntaxException {
        log.debug("REST request to update TreatyDetails : {}, {}", id, treatyDetails);
        if (treatyDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, treatyDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!treatyDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TreatyDetails result = treatyDetailsRepository.save(treatyDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, treatyDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /treaty-details/:id} : Partial updates given fields of an existing treatyDetails, field will ignore if it is null
     *
     * @param id the id of the treatyDetails to save.
     * @param treatyDetails the treatyDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated treatyDetails,
     * or with status {@code 400 (Bad Request)} if the treatyDetails is not valid,
     * or with status {@code 404 (Not Found)} if the treatyDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the treatyDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/treaty-details/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TreatyDetails> partialUpdateTreatyDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TreatyDetails treatyDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update TreatyDetails partially : {}, {}", id, treatyDetails);
        if (treatyDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, treatyDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!treatyDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TreatyDetails> result = treatyDetailsRepository
            .findById(treatyDetails.getId())
            .map(
                existingTreatyDetails -> {
                    if (treatyDetails.getName() != null) {
                        existingTreatyDetails.setName(treatyDetails.getName());
                    }
                    if (treatyDetails.getTreatyId() != null) {
                        existingTreatyDetails.setTreatyId(treatyDetails.getTreatyId());
                    }
                    if (treatyDetails.getMaximumLimit() != null) {
                        existingTreatyDetails.setMaximumLimit(treatyDetails.getMaximumLimit());
                    }
                    if (treatyDetails.getProductId() != null) {
                        existingTreatyDetails.setProductId(treatyDetails.getProductId());
                    }
                    if (treatyDetails.getMinLimit() != null) {
                        existingTreatyDetails.setMinLimit(treatyDetails.getMinLimit());
                    }
                    if (treatyDetails.getRetainedAmount() != null) {
                        existingTreatyDetails.setRetainedAmount(treatyDetails.getRetainedAmount());
                    }
                    if (treatyDetails.getCededAmount() != null) {
                        existingTreatyDetails.setCededAmount(treatyDetails.getCededAmount());
                    }
                    if (treatyDetails.getRetainedPercenatge() != null) {
                        existingTreatyDetails.setRetainedPercenatge(treatyDetails.getRetainedPercenatge());
                    }
                    if (treatyDetails.getCededPercenatge() != null) {
                        existingTreatyDetails.setCededPercenatge(treatyDetails.getCededPercenatge());
                    }
                    if (treatyDetails.getSurplus() != null) {
                        existingTreatyDetails.setSurplus(treatyDetails.getSurplus());
                    }
                    if (treatyDetails.getClassificationType() != null) {
                        existingTreatyDetails.setClassificationType(treatyDetails.getClassificationType());
                    }
                    if (treatyDetails.getNaceCodeClassification() != null) {
                        existingTreatyDetails.setNaceCodeClassification(treatyDetails.getNaceCodeClassification());
                    }
                    if (treatyDetails.getOtherClassification() != null) {
                        existingTreatyDetails.setOtherClassification(treatyDetails.getOtherClassification());
                    }
                    if (treatyDetails.getRiskType() != null) {
                        existingTreatyDetails.setRiskType(treatyDetails.getRiskType());
                    }

                    return existingTreatyDetails;
                }
            )
            .map(treatyDetailsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, treatyDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /treaty-details} : get all the treatyDetails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of treatyDetails in body.
     */
    @GetMapping("/treaty-details")
    public List<TreatyDetails> getAllTreatyDetails() {
        log.debug("REST request to get all TreatyDetails");
        return treatyDetailsRepository.findAll();
    }

    /**
     * {@code GET  /treaty-details/:id} : get the "id" treatyDetails.
     *
     * @param id the id of the treatyDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the treatyDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/treaty-details/{id}")
    public ResponseEntity<TreatyDetails> getTreatyDetails(@PathVariable Long id) {
        log.debug("REST request to get TreatyDetails : {}", id);
        Optional<TreatyDetails> treatyDetails = treatyDetailsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(treatyDetails);
    }

    /**
     * {@code DELETE  /treaty-details/:id} : delete the "id" treatyDetails.
     *
     * @param id the id of the treatyDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/treaty-details/{id}")
    public ResponseEntity<Void> deleteTreatyDetails(@PathVariable Long id) {
        log.debug("REST request to delete TreatyDetails : {}", id);
        treatyDetailsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
