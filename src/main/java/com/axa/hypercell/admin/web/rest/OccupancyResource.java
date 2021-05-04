package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.Occupancy;
import com.axa.hypercell.admin.repository.OccupancyRepository;
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
        return ResponseEntity
            .created(new URI("/api/occupancies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /occupancies/:id} : Updates an existing occupancy.
     *
     * @param id the id of the occupancy to save.
     * @param occupancy the occupancy to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated occupancy,
     * or with status {@code 400 (Bad Request)} if the occupancy is not valid,
     * or with status {@code 500 (Internal Server Error)} if the occupancy couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/occupancies/{id}")
    public ResponseEntity<Occupancy> updateOccupancy(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Occupancy occupancy
    ) throws URISyntaxException {
        log.debug("REST request to update Occupancy : {}, {}", id, occupancy);
        if (occupancy.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, occupancy.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!occupancyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Occupancy result = occupancyRepository.save(occupancy);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, occupancy.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /occupancies/:id} : Partial updates given fields of an existing occupancy, field will ignore if it is null
     *
     * @param id the id of the occupancy to save.
     * @param occupancy the occupancy to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated occupancy,
     * or with status {@code 400 (Bad Request)} if the occupancy is not valid,
     * or with status {@code 404 (Not Found)} if the occupancy is not found,
     * or with status {@code 500 (Internal Server Error)} if the occupancy couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/occupancies/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Occupancy> partialUpdateOccupancy(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Occupancy occupancy
    ) throws URISyntaxException {
        log.debug("REST request to partial update Occupancy partially : {}, {}", id, occupancy);
        if (occupancy.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, occupancy.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!occupancyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Occupancy> result = occupancyRepository
            .findById(occupancy.getId())
            .map(
                existingOccupancy -> {
                    if (occupancy.getName() != null) {
                        existingOccupancy.setName(occupancy.getName());
                    }
                    if (occupancy.getShortName() != null) {
                        existingOccupancy.setShortName(occupancy.getShortName());
                    }
                    if (occupancy.getDescription() != null) {
                        existingOccupancy.setDescription(occupancy.getDescription());
                    }
                    if (occupancy.getLineTypeId() != null) {
                        existingOccupancy.setLineTypeId(occupancy.getLineTypeId());
                    }
                    if (occupancy.getLineTypeOccupancy() != null) {
                        existingOccupancy.setLineTypeOccupancy(occupancy.getLineTypeOccupancy());
                    }

                    return existingOccupancy;
                }
            )
            .map(occupancyRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, occupancy.getId().toString())
        );
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
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
