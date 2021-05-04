package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.Cresta;
import com.axa.hypercell.admin.repository.CrestaRepository;
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
        return ResponseEntity
            .created(new URI("/api/crestas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crestas/:id} : Updates an existing cresta.
     *
     * @param id the id of the cresta to save.
     * @param cresta the cresta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cresta,
     * or with status {@code 400 (Bad Request)} if the cresta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cresta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crestas/{id}")
    public ResponseEntity<Cresta> updateCresta(@PathVariable(value = "id", required = false) final Long id, @RequestBody Cresta cresta)
        throws URISyntaxException {
        log.debug("REST request to update Cresta : {}, {}", id, cresta);
        if (cresta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cresta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crestaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cresta result = crestaRepository.save(cresta);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cresta.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /crestas/:id} : Partial updates given fields of an existing cresta, field will ignore if it is null
     *
     * @param id the id of the cresta to save.
     * @param cresta the cresta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cresta,
     * or with status {@code 400 (Bad Request)} if the cresta is not valid,
     * or with status {@code 404 (Not Found)} if the cresta is not found,
     * or with status {@code 500 (Internal Server Error)} if the cresta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/crestas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Cresta> partialUpdateCresta(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Cresta cresta
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cresta partially : {}, {}", id, cresta);
        if (cresta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cresta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crestaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cresta> result = crestaRepository
            .findById(cresta.getId())
            .map(
                existingCresta -> {
                    if (cresta.getName() != null) {
                        existingCresta.setName(cresta.getName());
                    }
                    if (cresta.getShortName() != null) {
                        existingCresta.setShortName(cresta.getShortName());
                    }
                    if (cresta.getCode() != null) {
                        existingCresta.setCode(cresta.getCode());
                    }

                    return existingCresta;
                }
            )
            .map(crestaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cresta.getId().toString())
        );
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
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
