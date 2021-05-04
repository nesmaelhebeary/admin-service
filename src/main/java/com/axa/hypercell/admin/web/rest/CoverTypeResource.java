package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.CoverType;
import com.axa.hypercell.admin.repository.CoverTypeRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.CoverType}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CoverTypeResource {

    private final Logger log = LoggerFactory.getLogger(CoverTypeResource.class);

    private static final String ENTITY_NAME = "adminserviceCoverType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CoverTypeRepository coverTypeRepository;

    public CoverTypeResource(CoverTypeRepository coverTypeRepository) {
        this.coverTypeRepository = coverTypeRepository;
    }

    /**
     * {@code POST  /cover-types} : Create a new coverType.
     *
     * @param coverType the coverType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new coverType, or with status {@code 400 (Bad Request)} if the coverType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cover-types")
    public ResponseEntity<CoverType> createCoverType(@RequestBody CoverType coverType) throws URISyntaxException {
        log.debug("REST request to save CoverType : {}", coverType);
        if (coverType.getId() != null) {
            throw new BadRequestAlertException("A new coverType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CoverType result = coverTypeRepository.save(coverType);
        return ResponseEntity
            .created(new URI("/api/cover-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cover-types/:id} : Updates an existing coverType.
     *
     * @param id the id of the coverType to save.
     * @param coverType the coverType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coverType,
     * or with status {@code 400 (Bad Request)} if the coverType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the coverType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cover-types/{id}")
    public ResponseEntity<CoverType> updateCoverType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CoverType coverType
    ) throws URISyntaxException {
        log.debug("REST request to update CoverType : {}, {}", id, coverType);
        if (coverType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coverType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coverTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CoverType result = coverTypeRepository.save(coverType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coverType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cover-types/:id} : Partial updates given fields of an existing coverType, field will ignore if it is null
     *
     * @param id the id of the coverType to save.
     * @param coverType the coverType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coverType,
     * or with status {@code 400 (Bad Request)} if the coverType is not valid,
     * or with status {@code 404 (Not Found)} if the coverType is not found,
     * or with status {@code 500 (Internal Server Error)} if the coverType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cover-types/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CoverType> partialUpdateCoverType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CoverType coverType
    ) throws URISyntaxException {
        log.debug("REST request to partial update CoverType partially : {}, {}", id, coverType);
        if (coverType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coverType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coverTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CoverType> result = coverTypeRepository
            .findById(coverType.getId())
            .map(
                existingCoverType -> {
                    if (coverType.getName() != null) {
                        existingCoverType.setName(coverType.getName());
                    }

                    return existingCoverType;
                }
            )
            .map(coverTypeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coverType.getId().toString())
        );
    }

    /**
     * {@code GET  /cover-types} : get all the coverTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of coverTypes in body.
     */
    @GetMapping("/cover-types")
    public List<CoverType> getAllCoverTypes() {
        log.debug("REST request to get all CoverTypes");
        return coverTypeRepository.findAll();
    }

    /**
     * {@code GET  /cover-types/:id} : get the "id" coverType.
     *
     * @param id the id of the coverType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the coverType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cover-types/{id}")
    public ResponseEntity<CoverType> getCoverType(@PathVariable Long id) {
        log.debug("REST request to get CoverType : {}", id);
        Optional<CoverType> coverType = coverTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(coverType);
    }

    /**
     * {@code DELETE  /cover-types/:id} : delete the "id" coverType.
     *
     * @param id the id of the coverType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cover-types/{id}")
    public ResponseEntity<Void> deleteCoverType(@PathVariable Long id) {
        log.debug("REST request to delete CoverType : {}", id);
        coverTypeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
