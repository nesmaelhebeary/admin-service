package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.Deductibles;
import com.axa.hypercell.admin.repository.DeductiblesRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.Deductibles}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DeductiblesResource {

    private final Logger log = LoggerFactory.getLogger(DeductiblesResource.class);

    private static final String ENTITY_NAME = "adminserviceDeductibles";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeductiblesRepository deductiblesRepository;

    public DeductiblesResource(DeductiblesRepository deductiblesRepository) {
        this.deductiblesRepository = deductiblesRepository;
    }

    /**
     * {@code POST  /deductibles} : Create a new deductibles.
     *
     * @param deductibles the deductibles to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deductibles, or with status {@code 400 (Bad Request)} if the deductibles has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/deductibles")
    public ResponseEntity<Deductibles> createDeductibles(@RequestBody Deductibles deductibles) throws URISyntaxException {
        log.debug("REST request to save Deductibles : {}", deductibles);
        if (deductibles.getId() != null) {
            throw new BadRequestAlertException("A new deductibles cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Deductibles result = deductiblesRepository.save(deductibles);
        return ResponseEntity
            .created(new URI("/api/deductibles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /deductibles/:id} : Updates an existing deductibles.
     *
     * @param id the id of the deductibles to save.
     * @param deductibles the deductibles to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deductibles,
     * or with status {@code 400 (Bad Request)} if the deductibles is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deductibles couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/deductibles/{id}")
    public ResponseEntity<Deductibles> updateDeductibles(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Deductibles deductibles
    ) throws URISyntaxException {
        log.debug("REST request to update Deductibles : {}, {}", id, deductibles);
        if (deductibles.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deductibles.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deductiblesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Deductibles result = deductiblesRepository.save(deductibles);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deductibles.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /deductibles/:id} : Partial updates given fields of an existing deductibles, field will ignore if it is null
     *
     * @param id the id of the deductibles to save.
     * @param deductibles the deductibles to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deductibles,
     * or with status {@code 400 (Bad Request)} if the deductibles is not valid,
     * or with status {@code 404 (Not Found)} if the deductibles is not found,
     * or with status {@code 500 (Internal Server Error)} if the deductibles couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/deductibles/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Deductibles> partialUpdateDeductibles(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Deductibles deductibles
    ) throws URISyntaxException {
        log.debug("REST request to partial update Deductibles partially : {}, {}", id, deductibles);
        if (deductibles.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deductibles.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deductiblesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Deductibles> result = deductiblesRepository
            .findById(deductibles.getId())
            .map(
                existingDeductibles -> {
                    if (deductibles.getSectionId() != null) {
                        existingDeductibles.setSectionId(deductibles.getSectionId());
                    }
                    if (deductibles.getTextAr() != null) {
                        existingDeductibles.setTextAr(deductibles.getTextAr());
                    }
                    if (deductibles.getTextEn() != null) {
                        existingDeductibles.setTextEn(deductibles.getTextEn());
                    }

                    return existingDeductibles;
                }
            )
            .map(deductiblesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deductibles.getId().toString())
        );
    }

    /**
     * {@code GET  /deductibles} : get all the deductibles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deductibles in body.
     */
    @GetMapping("/deductibles")
    public List<Deductibles> getAllDeductibles() {
        log.debug("REST request to get all Deductibles");
        return deductiblesRepository.findAll();
    }

    /**
     * {@code GET  /deductibles/:id} : get the "id" deductibles.
     *
     * @param id the id of the deductibles to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deductibles, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/deductibles/{id}")
    public ResponseEntity<Deductibles> getDeductibles(@PathVariable Long id) {
        log.debug("REST request to get Deductibles : {}", id);
        Optional<Deductibles> deductibles = deductiblesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(deductibles);
    }

    /**
     * {@code DELETE  /deductibles/:id} : delete the "id" deductibles.
     *
     * @param id the id of the deductibles to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/deductibles/{id}")
    public ResponseEntity<Void> deleteDeductibles(@PathVariable Long id) {
        log.debug("REST request to delete Deductibles : {}", id);
        deductiblesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
