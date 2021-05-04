package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.SystemClauses;
import com.axa.hypercell.admin.repository.SystemClausesRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.SystemClauses}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SystemClausesResource {

    private final Logger log = LoggerFactory.getLogger(SystemClausesResource.class);

    private static final String ENTITY_NAME = "adminserviceSystemClauses";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SystemClausesRepository systemClausesRepository;

    public SystemClausesResource(SystemClausesRepository systemClausesRepository) {
        this.systemClausesRepository = systemClausesRepository;
    }

    /**
     * {@code POST  /system-clauses} : Create a new systemClauses.
     *
     * @param systemClauses the systemClauses to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systemClauses, or with status {@code 400 (Bad Request)} if the systemClauses has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/system-clauses")
    public ResponseEntity<SystemClauses> createSystemClauses(@RequestBody SystemClauses systemClauses) throws URISyntaxException {
        log.debug("REST request to save SystemClauses : {}", systemClauses);
        if (systemClauses.getId() != null) {
            throw new BadRequestAlertException("A new systemClauses cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SystemClauses result = systemClausesRepository.save(systemClauses);
        return ResponseEntity
            .created(new URI("/api/system-clauses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /system-clauses/:id} : Updates an existing systemClauses.
     *
     * @param id the id of the systemClauses to save.
     * @param systemClauses the systemClauses to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemClauses,
     * or with status {@code 400 (Bad Request)} if the systemClauses is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemClauses couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/system-clauses/{id}")
    public ResponseEntity<SystemClauses> updateSystemClauses(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SystemClauses systemClauses
    ) throws URISyntaxException {
        log.debug("REST request to update SystemClauses : {}, {}", id, systemClauses);
        if (systemClauses.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemClauses.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemClausesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SystemClauses result = systemClausesRepository.save(systemClauses);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemClauses.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /system-clauses/:id} : Partial updates given fields of an existing systemClauses, field will ignore if it is null
     *
     * @param id the id of the systemClauses to save.
     * @param systemClauses the systemClauses to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemClauses,
     * or with status {@code 400 (Bad Request)} if the systemClauses is not valid,
     * or with status {@code 404 (Not Found)} if the systemClauses is not found,
     * or with status {@code 500 (Internal Server Error)} if the systemClauses couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/system-clauses/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SystemClauses> partialUpdateSystemClauses(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SystemClauses systemClauses
    ) throws URISyntaxException {
        log.debug("REST request to partial update SystemClauses partially : {}, {}", id, systemClauses);
        if (systemClauses.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemClauses.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemClausesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SystemClauses> result = systemClausesRepository
            .findById(systemClauses.getId())
            .map(
                existingSystemClauses -> {
                    if (systemClauses.getCode() != null) {
                        existingSystemClauses.setCode(systemClauses.getCode());
                    }
                    if (systemClauses.getTextEn() != null) {
                        existingSystemClauses.setTextEn(systemClauses.getTextEn());
                    }
                    if (systemClauses.getTextAr() != null) {
                        existingSystemClauses.setTextAr(systemClauses.getTextAr());
                    }
                    if (systemClauses.getClauseType() != null) {
                        existingSystemClauses.setClauseType(systemClauses.getClauseType());
                    }
                    if (systemClauses.getEffectiveDate() != null) {
                        existingSystemClauses.setEffectiveDate(systemClauses.getEffectiveDate());
                    }
                    if (systemClauses.getStatus() != null) {
                        existingSystemClauses.setStatus(systemClauses.getStatus());
                    }

                    return existingSystemClauses;
                }
            )
            .map(systemClausesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemClauses.getId().toString())
        );
    }

    /**
     * {@code GET  /system-clauses} : get all the systemClauses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systemClauses in body.
     */
    @GetMapping("/system-clauses")
    public List<SystemClauses> getAllSystemClauses() {
        log.debug("REST request to get all SystemClauses");
        return systemClausesRepository.findAll();
    }

    /**
     * {@code GET  /system-clauses/:id} : get the "id" systemClauses.
     *
     * @param id the id of the systemClauses to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systemClauses, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/system-clauses/{id}")
    public ResponseEntity<SystemClauses> getSystemClauses(@PathVariable Long id) {
        log.debug("REST request to get SystemClauses : {}", id);
        Optional<SystemClauses> systemClauses = systemClausesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(systemClauses);
    }

    /**
     * {@code DELETE  /system-clauses/:id} : delete the "id" systemClauses.
     *
     * @param id the id of the systemClauses to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/system-clauses/{id}")
    public ResponseEntity<Void> deleteSystemClauses(@PathVariable Long id) {
        log.debug("REST request to delete SystemClauses : {}", id);
        systemClausesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
