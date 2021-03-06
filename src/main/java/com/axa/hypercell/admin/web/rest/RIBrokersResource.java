package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.RIBrokers;
import com.axa.hypercell.admin.repository.RIBrokersRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.RIBrokers}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RIBrokersResource {

    private final Logger log = LoggerFactory.getLogger(RIBrokersResource.class);

    private static final String ENTITY_NAME = "adminserviceRiBrokers";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RIBrokersRepository rIBrokersRepository;

    public RIBrokersResource(RIBrokersRepository rIBrokersRepository) {
        this.rIBrokersRepository = rIBrokersRepository;
    }

    /**
     * {@code POST  /ri-brokers} : Create a new rIBrokers.
     *
     * @param rIBrokers the rIBrokers to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rIBrokers, or with status {@code 400 (Bad Request)} if the rIBrokers has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ri-brokers")
    public ResponseEntity<RIBrokers> createRIBrokers(@RequestBody RIBrokers rIBrokers) throws URISyntaxException {
        log.debug("REST request to save RIBrokers : {}", rIBrokers);
        if (rIBrokers.getId() != null) {
            throw new BadRequestAlertException("A new rIBrokers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RIBrokers result = rIBrokersRepository.save(rIBrokers);
        return ResponseEntity.created(new URI("/api/ri-brokers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ri-brokers} : Updates an existing rIBrokers.
     *
     * @param rIBrokers the rIBrokers to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rIBrokers,
     * or with status {@code 400 (Bad Request)} if the rIBrokers is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rIBrokers couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ri-brokers")
    public ResponseEntity<RIBrokers> updateRIBrokers(@RequestBody RIBrokers rIBrokers) throws URISyntaxException {
        log.debug("REST request to update RIBrokers : {}", rIBrokers);
        if (rIBrokers.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RIBrokers result = rIBrokersRepository.save(rIBrokers);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rIBrokers.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ri-brokers} : get all the rIBrokers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rIBrokers in body.
     */
    @GetMapping("/ri-brokers")
    public List<RIBrokers> getAllRIBrokers() {
        log.debug("REST request to get all RIBrokers");
        return rIBrokersRepository.findAll();
    }

    /**
     * {@code GET  /ri-brokers/:id} : get the "id" rIBrokers.
     *
     * @param id the id of the rIBrokers to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rIBrokers, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ri-brokers/{id}")
    public ResponseEntity<RIBrokers> getRIBrokers(@PathVariable Long id) {
        log.debug("REST request to get RIBrokers : {}", id);
        Optional<RIBrokers> rIBrokers = rIBrokersRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(rIBrokers);
    }

    /**
     * {@code DELETE  /ri-brokers/:id} : delete the "id" rIBrokers.
     *
     * @param id the id of the rIBrokers to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ri-brokers/{id}")
    public ResponseEntity<Void> deleteRIBrokers(@PathVariable Long id) {
        log.debug("REST request to delete RIBrokers : {}", id);
        rIBrokersRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
