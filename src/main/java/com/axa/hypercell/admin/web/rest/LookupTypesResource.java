package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.LookupTypes;
import com.axa.hypercell.admin.repository.LookupTypesRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.LookupTypes}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LookupTypesResource {

    private final Logger log = LoggerFactory.getLogger(LookupTypesResource.class);

    private static final String ENTITY_NAME = "adminserviceLookupTypes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LookupTypesRepository lookupTypesRepository;

    public LookupTypesResource(LookupTypesRepository lookupTypesRepository) {
        this.lookupTypesRepository = lookupTypesRepository;
    }

    /**
     * {@code POST  /lookup-types} : Create a new lookupTypes.
     *
     * @param lookupTypes the lookupTypes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lookupTypes, or with status {@code 400 (Bad Request)} if the lookupTypes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lookup-types")
    public ResponseEntity<LookupTypes> createLookupTypes(@RequestBody LookupTypes lookupTypes) throws URISyntaxException {
        log.debug("REST request to save LookupTypes : {}", lookupTypes);
        if (lookupTypes.getId() != null) {
            throw new BadRequestAlertException("A new lookupTypes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LookupTypes result = lookupTypesRepository.save(lookupTypes);
        return ResponseEntity
            .created(new URI("/api/lookup-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lookup-types/:id} : Updates an existing lookupTypes.
     *
     * @param id the id of the lookupTypes to save.
     * @param lookupTypes the lookupTypes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lookupTypes,
     * or with status {@code 400 (Bad Request)} if the lookupTypes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lookupTypes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lookup-types/{id}")
    public ResponseEntity<LookupTypes> updateLookupTypes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LookupTypes lookupTypes
    ) throws URISyntaxException {
        log.debug("REST request to update LookupTypes : {}, {}", id, lookupTypes);
        if (lookupTypes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lookupTypes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lookupTypesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LookupTypes result = lookupTypesRepository.save(lookupTypes);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lookupTypes.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lookup-types/:id} : Partial updates given fields of an existing lookupTypes, field will ignore if it is null
     *
     * @param id the id of the lookupTypes to save.
     * @param lookupTypes the lookupTypes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lookupTypes,
     * or with status {@code 400 (Bad Request)} if the lookupTypes is not valid,
     * or with status {@code 404 (Not Found)} if the lookupTypes is not found,
     * or with status {@code 500 (Internal Server Error)} if the lookupTypes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lookup-types/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LookupTypes> partialUpdateLookupTypes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LookupTypes lookupTypes
    ) throws URISyntaxException {
        log.debug("REST request to partial update LookupTypes partially : {}, {}", id, lookupTypes);
        if (lookupTypes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lookupTypes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lookupTypesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LookupTypes> result = lookupTypesRepository
            .findById(lookupTypes.getId())
            .map(
                existingLookupTypes -> {
                    if (lookupTypes.getName() != null) {
                        existingLookupTypes.setName(lookupTypes.getName());
                    }
                    if (lookupTypes.getChildName() != null) {
                        existingLookupTypes.setChildName(lookupTypes.getChildName());
                    }

                    return existingLookupTypes;
                }
            )
            .map(lookupTypesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lookupTypes.getId().toString())
        );
    }

    /**
     * {@code GET  /lookup-types} : get all the lookupTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lookupTypes in body.
     */
    @GetMapping("/lookup-types")
    public List<LookupTypes> getAllLookupTypes() {
        log.debug("REST request to get all LookupTypes");
        return lookupTypesRepository.findAll();
    }

    /**
     * {@code GET  /lookup-types/:id} : get the "id" lookupTypes.
     *
     * @param id the id of the lookupTypes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lookupTypes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lookup-types/{id}")
    public ResponseEntity<LookupTypes> getLookupTypes(@PathVariable Long id) {
        log.debug("REST request to get LookupTypes : {}", id);
        Optional<LookupTypes> lookupTypes = lookupTypesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(lookupTypes);
    }

    /**
     * {@code DELETE  /lookup-types/:id} : delete the "id" lookupTypes.
     *
     * @param id the id of the lookupTypes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lookup-types/{id}")
    public ResponseEntity<Void> deleteLookupTypes(@PathVariable Long id) {
        log.debug("REST request to delete LookupTypes : {}", id);
        lookupTypesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
