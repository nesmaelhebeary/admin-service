package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.Treaty;
import com.axa.hypercell.admin.repository.TreatyRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.Treaty}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TreatyResource {

    private final Logger log = LoggerFactory.getLogger(TreatyResource.class);

    private static final String ENTITY_NAME = "adminserviceTreaty";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TreatyRepository treatyRepository;

    public TreatyResource(TreatyRepository treatyRepository) {
        this.treatyRepository = treatyRepository;
    }

    /**
     * {@code POST  /treaties} : Create a new treaty.
     *
     * @param treaty the treaty to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new treaty, or with status {@code 400 (Bad Request)} if the treaty has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/treaties")
    public ResponseEntity<Treaty> createTreaty(@RequestBody Treaty treaty) throws URISyntaxException {
        log.debug("REST request to save Treaty : {}", treaty);
        if (treaty.getId() != null) {
            throw new BadRequestAlertException("A new treaty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Treaty result = treatyRepository.save(treaty);
        return ResponseEntity
            .created(new URI("/api/treaties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /treaties/:id} : Updates an existing treaty.
     *
     * @param id the id of the treaty to save.
     * @param treaty the treaty to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated treaty,
     * or with status {@code 400 (Bad Request)} if the treaty is not valid,
     * or with status {@code 500 (Internal Server Error)} if the treaty couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/treaties/{id}")
    public ResponseEntity<Treaty> updateTreaty(@PathVariable(value = "id", required = false) final Long id, @RequestBody Treaty treaty)
        throws URISyntaxException {
        log.debug("REST request to update Treaty : {}, {}", id, treaty);
        if (treaty.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, treaty.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!treatyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Treaty result = treatyRepository.save(treaty);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, treaty.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /treaties/:id} : Partial updates given fields of an existing treaty, field will ignore if it is null
     *
     * @param id the id of the treaty to save.
     * @param treaty the treaty to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated treaty,
     * or with status {@code 400 (Bad Request)} if the treaty is not valid,
     * or with status {@code 404 (Not Found)} if the treaty is not found,
     * or with status {@code 500 (Internal Server Error)} if the treaty couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/treaties/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Treaty> partialUpdateTreaty(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Treaty treaty
    ) throws URISyntaxException {
        log.debug("REST request to partial update Treaty partially : {}, {}", id, treaty);
        if (treaty.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, treaty.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!treatyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Treaty> result = treatyRepository
            .findById(treaty.getId())
            .map(
                existingTreaty -> {
                    if (treaty.getTreatyType() != null) {
                        existingTreaty.setTreatyType(treaty.getTreatyType());
                    }
                    if (treaty.getStartDate() != null) {
                        existingTreaty.setStartDate(treaty.getStartDate());
                    }
                    if (treaty.getEndDate() != null) {
                        existingTreaty.setEndDate(treaty.getEndDate());
                    }
                    if (treaty.getTreatyDocumentPath() != null) {
                        existingTreaty.setTreatyDocumentPath(treaty.getTreatyDocumentPath());
                    }

                    return existingTreaty;
                }
            )
            .map(treatyRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, treaty.getId().toString())
        );
    }

    /**
     * {@code GET  /treaties} : get all the treaties.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of treaties in body.
     */
    @GetMapping("/treaties")
    public List<Treaty> getAllTreaties() {
        log.debug("REST request to get all Treaties");
        return treatyRepository.findAll();
    }

    /**
     * {@code GET  /treaties/:id} : get the "id" treaty.
     *
     * @param id the id of the treaty to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the treaty, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/treaties/{id}")
    public ResponseEntity<Treaty> getTreaty(@PathVariable Long id) {
        log.debug("REST request to get Treaty : {}", id);
        Optional<Treaty> treaty = treatyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(treaty);
    }

    /**
     * {@code DELETE  /treaties/:id} : delete the "id" treaty.
     *
     * @param id the id of the treaty to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/treaties/{id}")
    public ResponseEntity<Void> deleteTreaty(@PathVariable Long id) {
        log.debug("REST request to delete Treaty : {}", id);
        treatyRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
