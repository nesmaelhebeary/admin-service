package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.TermsAndConditions;
import com.axa.hypercell.admin.repository.TermsAndConditionsRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.TermsAndConditions}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TermsAndConditionsResource {

    private final Logger log = LoggerFactory.getLogger(TermsAndConditionsResource.class);

    private static final String ENTITY_NAME = "adminserviceTermsAndConditions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TermsAndConditionsRepository termsAndConditionsRepository;

    public TermsAndConditionsResource(TermsAndConditionsRepository termsAndConditionsRepository) {
        this.termsAndConditionsRepository = termsAndConditionsRepository;
    }

    /**
     * {@code POST  /terms-and-conditions} : Create a new termsAndConditions.
     *
     * @param termsAndConditions the termsAndConditions to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new termsAndConditions, or with status {@code 400 (Bad Request)} if the termsAndConditions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/terms-and-conditions")
    public ResponseEntity<TermsAndConditions> createTermsAndConditions(@RequestBody TermsAndConditions termsAndConditions) throws URISyntaxException {
        log.debug("REST request to save TermsAndConditions : {}", termsAndConditions);
        if (termsAndConditions.getId() != null) {
            throw new BadRequestAlertException("A new termsAndConditions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TermsAndConditions result = termsAndConditionsRepository.save(termsAndConditions);
        return ResponseEntity.created(new URI("/api/terms-and-conditions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /terms-and-conditions} : Updates an existing termsAndConditions.
     *
     * @param termsAndConditions the termsAndConditions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated termsAndConditions,
     * or with status {@code 400 (Bad Request)} if the termsAndConditions is not valid,
     * or with status {@code 500 (Internal Server Error)} if the termsAndConditions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/terms-and-conditions")
    public ResponseEntity<TermsAndConditions> updateTermsAndConditions(@RequestBody TermsAndConditions termsAndConditions) throws URISyntaxException {
        log.debug("REST request to update TermsAndConditions : {}", termsAndConditions);
        if (termsAndConditions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TermsAndConditions result = termsAndConditionsRepository.save(termsAndConditions);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, termsAndConditions.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /terms-and-conditions} : get all the termsAndConditions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of termsAndConditions in body.
     */
    @GetMapping("/terms-and-conditions")
    public List<TermsAndConditions> getAllTermsAndConditions() {
        log.debug("REST request to get all TermsAndConditions");
        return termsAndConditionsRepository.findAll();
    }

    /**
     * {@code GET  /terms-and-conditions/:id} : get the "id" termsAndConditions.
     *
     * @param id the id of the termsAndConditions to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the termsAndConditions, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/terms-and-conditions/{id}")
    public ResponseEntity<TermsAndConditions> getTermsAndConditions(@PathVariable Long id) {
        log.debug("REST request to get TermsAndConditions : {}", id);
        Optional<TermsAndConditions> termsAndConditions = termsAndConditionsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(termsAndConditions);
    }

    /**
     * {@code DELETE  /terms-and-conditions/:id} : delete the "id" termsAndConditions.
     *
     * @param id the id of the termsAndConditions to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/terms-and-conditions/{id}")
    public ResponseEntity<Void> deleteTermsAndConditions(@PathVariable Long id) {
        log.debug("REST request to delete TermsAndConditions : {}", id);
        termsAndConditionsRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
