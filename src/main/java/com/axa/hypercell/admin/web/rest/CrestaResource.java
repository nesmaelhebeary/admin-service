package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.Cresta;
import com.axa.hypercell.admin.repository.CrestaRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.Cresta}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CrestaResource {

    private final Logger log = LoggerFactory.getLogger(CrestaResource.class);

    private static final String ENTITY_NAME = "adminserviceCresta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrestaRepository crestaRepository;

    public CrestaResource(CrestaRepository crestaRepository) {
        this.crestaRepository = crestaRepository;
    }

    /**
     * {@code POST  /crestas} : Create a new cresta.
     *
     * @param cresta the cresta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cresta, or with status {@code 400 (Bad Request)} if the cresta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crestas")
    public ResponseEntity<Cresta> createCresta(@RequestBody Cresta cresta) throws URISyntaxException {
        log.debug("REST request to save Cresta : {}", cresta);
        if (cresta.getId() != null) {
            throw new BadRequestAlertException("A new cresta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cresta result = crestaRepository.save(cresta);
        return ResponseEntity.created(new URI("/api/crestas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crestas} : Updates an existing cresta.
     *
     * @param cresta the cresta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cresta,
     * or with status {@code 400 (Bad Request)} if the cresta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cresta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crestas")
    public ResponseEntity<Cresta> updateCresta(@RequestBody Cresta cresta) throws URISyntaxException {
        log.debug("REST request to update Cresta : {}", cresta);
        if (cresta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Cresta result = crestaRepository.save(cresta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cresta.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /crestas} : get all the crestas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crestas in body.
     */
    @GetMapping("/crestas")
    public List<Cresta> getAllCrestas() {
        log.debug("REST request to get all Crestas");
        return crestaRepository.findAll();
    }

    /**
     * {@code GET  /crestas/:id} : get the "id" cresta.
     *
     * @param id the id of the cresta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cresta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crestas/{id}")
    public ResponseEntity<Cresta> getCresta(@PathVariable Long id) {
        log.debug("REST request to get Cresta : {}", id);
        Optional<Cresta> cresta = crestaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cresta);
    }

    /**
     * {@code DELETE  /crestas/:id} : delete the "id" cresta.
     *
     * @param id the id of the cresta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crestas/{id}")
    public ResponseEntity<Void> deleteCresta(@PathVariable Long id) {
        log.debug("REST request to delete Cresta : {}", id);
        crestaRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
