package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.Clauses;
import com.axa.hypercell.admin.repository.ClausesRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.Clauses}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ClausesResource {

    private final Logger log = LoggerFactory.getLogger(ClausesResource.class);

    private static final String ENTITY_NAME = "adminserviceClauses";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClausesRepository clausesRepository;

    public ClausesResource(ClausesRepository clausesRepository) {
        this.clausesRepository = clausesRepository;
    }

    /**
     * {@code POST  /clauses} : Create a new clauses.
     *
     * @param clauses the clauses to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clauses, or with status {@code 400 (Bad Request)} if the clauses has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clauses")
    public ResponseEntity<Clauses> createClauses(@RequestBody Clauses clauses) throws URISyntaxException {
        log.debug("REST request to save Clauses : {}", clauses);
        if (clauses.getId() != null) {
            throw new BadRequestAlertException("A new clauses cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Clauses result = clausesRepository.save(clauses);
        return ResponseEntity.created(new URI("/api/clauses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /clauses} : Updates an existing clauses.
     *
     * @param clauses the clauses to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clauses,
     * or with status {@code 400 (Bad Request)} if the clauses is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clauses couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/clauses")
    public ResponseEntity<Clauses> updateClauses(@RequestBody Clauses clauses) throws URISyntaxException {
        log.debug("REST request to update Clauses : {}", clauses);
        if (clauses.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Clauses result = clausesRepository.save(clauses);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clauses.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /clauses} : get all the clauses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clauses in body.
     */
    @GetMapping("/clauses")
    public List<Clauses> getAllClauses() {
        log.debug("REST request to get all Clauses");
        return clausesRepository.findAll();
    }

    /**
     * {@code GET  /clauses/:id} : get the "id" clauses.
     *
     * @param id the id of the clauses to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clauses, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clauses/{id}")
    public ResponseEntity<Clauses> getClauses(@PathVariable Long id) {
        log.debug("REST request to get Clauses : {}", id);
        Optional<Clauses> clauses = clausesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(clauses);
    }

    /**
     * {@code DELETE  /clauses/:id} : delete the "id" clauses.
     *
     * @param id the id of the clauses to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clauses/{id}")
    public ResponseEntity<Void> deleteClauses(@PathVariable Long id) {
        log.debug("REST request to delete Clauses : {}", id);
        clausesRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
