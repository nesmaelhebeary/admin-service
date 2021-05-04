package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.LkExtensionParameters;
import com.axa.hypercell.admin.repository.LkExtensionParametersRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.LkExtensionParameters}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LkExtensionParametersResource {

    private final Logger log = LoggerFactory.getLogger(LkExtensionParametersResource.class);

    private static final String ENTITY_NAME = "adminserviceLkExtensionParameters";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LkExtensionParametersRepository lkExtensionParametersRepository;

    public LkExtensionParametersResource(LkExtensionParametersRepository lkExtensionParametersRepository) {
        this.lkExtensionParametersRepository = lkExtensionParametersRepository;
    }

    /**
     * {@code POST  /lk-extension-parameters} : Create a new lkExtensionParameters.
     *
     * @param lkExtensionParameters the lkExtensionParameters to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lkExtensionParameters, or with status {@code 400 (Bad Request)} if the lkExtensionParameters has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lk-extension-parameters")
    public ResponseEntity<LkExtensionParameters> createLkExtensionParameters(@RequestBody LkExtensionParameters lkExtensionParameters)
        throws URISyntaxException {
        log.debug("REST request to save LkExtensionParameters : {}", lkExtensionParameters);
        if (lkExtensionParameters.getId() != null) {
            throw new BadRequestAlertException("A new lkExtensionParameters cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LkExtensionParameters result = lkExtensionParametersRepository.save(lkExtensionParameters);
        return ResponseEntity
            .created(new URI("/api/lk-extension-parameters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lk-extension-parameters/:id} : Updates an existing lkExtensionParameters.
     *
     * @param id the id of the lkExtensionParameters to save.
     * @param lkExtensionParameters the lkExtensionParameters to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lkExtensionParameters,
     * or with status {@code 400 (Bad Request)} if the lkExtensionParameters is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lkExtensionParameters couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lk-extension-parameters/{id}")
    public ResponseEntity<LkExtensionParameters> updateLkExtensionParameters(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LkExtensionParameters lkExtensionParameters
    ) throws URISyntaxException {
        log.debug("REST request to update LkExtensionParameters : {}, {}", id, lkExtensionParameters);
        if (lkExtensionParameters.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lkExtensionParameters.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lkExtensionParametersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LkExtensionParameters result = lkExtensionParametersRepository.save(lkExtensionParameters);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lkExtensionParameters.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lk-extension-parameters/:id} : Partial updates given fields of an existing lkExtensionParameters, field will ignore if it is null
     *
     * @param id the id of the lkExtensionParameters to save.
     * @param lkExtensionParameters the lkExtensionParameters to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lkExtensionParameters,
     * or with status {@code 400 (Bad Request)} if the lkExtensionParameters is not valid,
     * or with status {@code 404 (Not Found)} if the lkExtensionParameters is not found,
     * or with status {@code 500 (Internal Server Error)} if the lkExtensionParameters couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lk-extension-parameters/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LkExtensionParameters> partialUpdateLkExtensionParameters(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LkExtensionParameters lkExtensionParameters
    ) throws URISyntaxException {
        log.debug("REST request to partial update LkExtensionParameters partially : {}, {}", id, lkExtensionParameters);
        if (lkExtensionParameters.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lkExtensionParameters.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lkExtensionParametersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LkExtensionParameters> result = lkExtensionParametersRepository
            .findById(lkExtensionParameters.getId())
            .map(
                existingLkExtensionParameters -> {
                    if (lkExtensionParameters.getName() != null) {
                        existingLkExtensionParameters.setName(lkExtensionParameters.getName());
                    }
                    if (lkExtensionParameters.getDataType() != null) {
                        existingLkExtensionParameters.setDataType(lkExtensionParameters.getDataType());
                    }

                    return existingLkExtensionParameters;
                }
            )
            .map(lkExtensionParametersRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lkExtensionParameters.getId().toString())
        );
    }

    /**
     * {@code GET  /lk-extension-parameters} : get all the lkExtensionParameters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lkExtensionParameters in body.
     */
    @GetMapping("/lk-extension-parameters")
    public List<LkExtensionParameters> getAllLkExtensionParameters() {
        log.debug("REST request to get all LkExtensionParameters");
        return lkExtensionParametersRepository.findAll();
    }

    /**
     * {@code GET  /lk-extension-parameters/:id} : get the "id" lkExtensionParameters.
     *
     * @param id the id of the lkExtensionParameters to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lkExtensionParameters, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lk-extension-parameters/{id}")
    public ResponseEntity<LkExtensionParameters> getLkExtensionParameters(@PathVariable Long id) {
        log.debug("REST request to get LkExtensionParameters : {}", id);
        Optional<LkExtensionParameters> lkExtensionParameters = lkExtensionParametersRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(lkExtensionParameters);
    }

    /**
     * {@code DELETE  /lk-extension-parameters/:id} : delete the "id" lkExtensionParameters.
     *
     * @param id the id of the lkExtensionParameters to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lk-extension-parameters/{id}")
    public ResponseEntity<Void> deleteLkExtensionParameters(@PathVariable Long id) {
        log.debug("REST request to delete LkExtensionParameters : {}", id);
        lkExtensionParametersRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
