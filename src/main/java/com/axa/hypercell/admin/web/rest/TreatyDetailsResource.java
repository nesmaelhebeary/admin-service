package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.TreatyDetails;
import com.axa.hypercell.admin.repository.TreatyDetailsRepository;
import com.axa.hypercell.admin.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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
        return ResponseEntity.created(new URI("/api/treaty-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /treaty-details} : Updates an existing treatyDetails.
     *
     * @param treatyDetails the treatyDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated treatyDetails,
     * or with status {@code 400 (Bad Request)} if the treatyDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the treatyDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/treaty-details")
    public ResponseEntity<TreatyDetails> updateTreatyDetails(@RequestBody TreatyDetails treatyDetails) throws URISyntaxException {
        log.debug("REST request to update TreatyDetails : {}", treatyDetails);
        if (treatyDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TreatyDetails result = treatyDetailsRepository.save(treatyDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, treatyDetails.getId().toString()))
            .body(result);
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
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
