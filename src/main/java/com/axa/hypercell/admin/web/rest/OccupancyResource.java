package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.Occupancy;
import com.axa.hypercell.admin.repository.OccupancyRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.Occupancy}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OccupancyResource {

    private final Logger log = LoggerFactory.getLogger(OccupancyResource.class);

    private static final String ENTITY_NAME = "adminserviceOccupancy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OccupancyRepository occupancyRepository;

    public OccupancyResource(OccupancyRepository occupancyRepository) {
        this.occupancyRepository = occupancyRepository;
    }

    /**
     * {@code POST  /occupancies} : Create a new occupancy.
     *
     * @param occupancy the occupancy to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new occupancy, or with status {@code 400 (Bad Request)} if the occupancy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/occupancies")
    public ResponseEntity<Occupancy> createOccupancy(@RequestBody Occupancy occupancy) throws URISyntaxException {
        log.debug("REST request to save Occupancy : {}", occupancy);
        if (occupancy.getId() != null) {
            throw new BadRequestAlertException("A new occupancy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Occupancy result = occupancyRepository.save(occupancy);
        return ResponseEntity.created(new URI("/api/occupancies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /occupancies} : Updates an existing occupancy.
     *
     * @param occupancy the occupancy to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated occupancy,
     * or with status {@code 400 (Bad Request)} if the occupancy is not valid,
     * or with status {@code 500 (Internal Server Error)} if the occupancy couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/occupancies")
    public ResponseEntity<Occupancy> updateOccupancy(@RequestBody Occupancy occupancy) throws URISyntaxException {
        log.debug("REST request to update Occupancy : {}", occupancy);
        if (occupancy.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Occupancy result = occupancyRepository.save(occupancy);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, occupancy.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /occupancies} : get all the occupancies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of occupancies in body.
     */
    @GetMapping("/occupancies")
    public List<Occupancy> getAllOccupancies() {
        log.debug("REST request to get all Occupancies");
        return occupancyRepository.findAll();
    }

    /**
     * {@code GET  /occupancies/:id} : get the "id" occupancy.
     *
     * @param id the id of the occupancy to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the occupancy, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/occupancies/{id}")
    public ResponseEntity<Occupancy> getOccupancy(@PathVariable Long id) {
        log.debug("REST request to get Occupancy : {}", id);
        Optional<Occupancy> occupancy = occupancyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(occupancy);
    }

    /**
     * {@code DELETE  /occupancies/:id} : delete the "id" occupancy.
     *
     * @param id the id of the occupancy to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/occupancies/{id}")
    public ResponseEntity<Void> deleteOccupancy(@PathVariable Long id) {
        log.debug("REST request to delete Occupancy : {}", id);
        occupancyRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
