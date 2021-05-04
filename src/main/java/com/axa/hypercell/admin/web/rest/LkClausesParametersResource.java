package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.LkClausesParameters;
import com.axa.hypercell.admin.repository.LkClausesParametersRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.LkClausesParameters}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LkClausesParametersResource {

    private final Logger log = LoggerFactory.getLogger(LkClausesParametersResource.class);

    private static final String ENTITY_NAME = "adminserviceLkClausesParameters";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LkClausesParametersRepository lkClausesParametersRepository;

    public LkClausesParametersResource(LkClausesParametersRepository lkClausesParametersRepository) {
        this.lkClausesParametersRepository = lkClausesParametersRepository;
    }

    /**
     * {@code POST  /lk-clauses-parameters} : Create a new lkClausesParameters.
     *
     * @param lkClausesParameters the lkClausesParameters to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lkClausesParameters, or with status {@code 400 (Bad Request)} if the lkClausesParameters has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lk-clauses-parameters")
    public ResponseEntity<LkClausesParameters> createLkClausesParameters(@RequestBody LkClausesParameters lkClausesParameters)
        throws URISyntaxException {
        log.debug("REST request to save LkClausesParameters : {}", lkClausesParameters);
        if (lkClausesParameters.getId() != null) {
            throw new BadRequestAlertException("A new lkClausesParameters cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LkClausesParameters result = lkClausesParametersRepository.save(lkClausesParameters);
        return ResponseEntity
            .created(new URI("/api/lk-clauses-parameters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lk-clauses-parameters/:id} : Updates an existing lkClausesParameters.
     *
     * @param id the id of the lkClausesParameters to save.
     * @param lkClausesParameters the lkClausesParameters to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lkClausesParameters,
     * or with status {@code 400 (Bad Request)} if the lkClausesParameters is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lkClausesParameters couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lk-clauses-parameters/{id}")
    public ResponseEntity<LkClausesParameters> updateLkClausesParameters(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LkClausesParameters lkClausesParameters
    ) throws URISyntaxException {
        log.debug("REST request to update LkClausesParameters : {}, {}", id, lkClausesParameters);
        if (lkClausesParameters.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lkClausesParameters.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lkClausesParametersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LkClausesParameters result = lkClausesParametersRepository.save(lkClausesParameters);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lkClausesParameters.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lk-clauses-parameters/:id} : Partial updates given fields of an existing lkClausesParameters, field will ignore if it is null
     *
     * @param id the id of the lkClausesParameters to save.
     * @param lkClausesParameters the lkClausesParameters to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lkClausesParameters,
     * or with status {@code 400 (Bad Request)} if the lkClausesParameters is not valid,
     * or with status {@code 404 (Not Found)} if the lkClausesParameters is not found,
     * or with status {@code 500 (Internal Server Error)} if the lkClausesParameters couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lk-clauses-parameters/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LkClausesParameters> partialUpdateLkClausesParameters(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LkClausesParameters lkClausesParameters
    ) throws URISyntaxException {
        log.debug("REST request to partial update LkClausesParameters partially : {}, {}", id, lkClausesParameters);
        if (lkClausesParameters.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lkClausesParameters.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lkClausesParametersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LkClausesParameters> result = lkClausesParametersRepository
            .findById(lkClausesParameters.getId())
            .map(
                existingLkClausesParameters -> {
                    if (lkClausesParameters.getName() != null) {
                        existingLkClausesParameters.setName(lkClausesParameters.getName());
                    }

                    return existingLkClausesParameters;
                }
            )
            .map(lkClausesParametersRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lkClausesParameters.getId().toString())
        );
    }

    /**
     * {@code GET  /lk-clauses-parameters} : get all the lkClausesParameters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lkClausesParameters in body.
     */
    @GetMapping("/lk-clauses-parameters")
    public List<LkClausesParameters> getAllLkClausesParameters() {
        log.debug("REST request to get all LkClausesParameters");
        return lkClausesParametersRepository.findAll();
    }

    /**
     * {@code GET  /lk-clauses-parameters/:id} : get the "id" lkClausesParameters.
     *
     * @param id the id of the lkClausesParameters to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lkClausesParameters, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lk-clauses-parameters/{id}")
    public ResponseEntity<LkClausesParameters> getLkClausesParameters(@PathVariable Long id) {
        log.debug("REST request to get LkClausesParameters : {}", id);
        Optional<LkClausesParameters> lkClausesParameters = lkClausesParametersRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(lkClausesParameters);
    }

    /**
     * {@code DELETE  /lk-clauses-parameters/:id} : delete the "id" lkClausesParameters.
     *
     * @param id the id of the lkClausesParameters to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lk-clauses-parameters/{id}")
    public ResponseEntity<Void> deleteLkClausesParameters(@PathVariable Long id) {
        log.debug("REST request to delete LkClausesParameters : {}", id);
        lkClausesParametersRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
