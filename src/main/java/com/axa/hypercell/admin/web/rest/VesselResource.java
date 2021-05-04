package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.Vessel;
import com.axa.hypercell.admin.repository.VesselRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.Vessel}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VesselResource {

    private final Logger log = LoggerFactory.getLogger(VesselResource.class);

    private static final String ENTITY_NAME = "adminserviceVessel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VesselRepository vesselRepository;

    public VesselResource(VesselRepository vesselRepository) {
        this.vesselRepository = vesselRepository;
    }

    /**
     * {@code POST  /vessels} : Create a new vessel.
     *
     * @param vessel the vessel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vessel, or with status {@code 400 (Bad Request)} if the vessel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vessels")
    public ResponseEntity<Vessel> createVessel(@RequestBody Vessel vessel) throws URISyntaxException {
        log.debug("REST request to save Vessel : {}", vessel);
        if (vessel.getId() != null) {
            throw new BadRequestAlertException("A new vessel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vessel result = vesselRepository.save(vessel);
        return ResponseEntity
            .created(new URI("/api/vessels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vessels/:id} : Updates an existing vessel.
     *
     * @param id the id of the vessel to save.
     * @param vessel the vessel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vessel,
     * or with status {@code 400 (Bad Request)} if the vessel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vessel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vessels/{id}")
    public ResponseEntity<Vessel> updateVessel(@PathVariable(value = "id", required = false) final Long id, @RequestBody Vessel vessel)
        throws URISyntaxException {
        log.debug("REST request to update Vessel : {}, {}", id, vessel);
        if (vessel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vessel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vesselRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Vessel result = vesselRepository.save(vessel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vessel.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vessels/:id} : Partial updates given fields of an existing vessel, field will ignore if it is null
     *
     * @param id the id of the vessel to save.
     * @param vessel the vessel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vessel,
     * or with status {@code 400 (Bad Request)} if the vessel is not valid,
     * or with status {@code 404 (Not Found)} if the vessel is not found,
     * or with status {@code 500 (Internal Server Error)} if the vessel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vessels/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Vessel> partialUpdateVessel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Vessel vessel
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vessel partially : {}, {}", id, vessel);
        if (vessel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vessel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vesselRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Vessel> result = vesselRepository
            .findById(vessel.getId())
            .map(
                existingVessel -> {
                    if (vessel.getNameEn() != null) {
                        existingVessel.setNameEn(vessel.getNameEn());
                    }
                    if (vessel.getNameAr() != null) {
                        existingVessel.setNameAr(vessel.getNameAr());
                    }

                    return existingVessel;
                }
            )
            .map(vesselRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vessel.getId().toString())
        );
    }

    /**
     * {@code GET  /vessels} : get all the vessels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vessels in body.
     */
    @GetMapping("/vessels")
    public List<Vessel> getAllVessels() {
        log.debug("REST request to get all Vessels");
        return vesselRepository.findAll();
    }

    /**
     * {@code GET  /vessels/:id} : get the "id" vessel.
     *
     * @param id the id of the vessel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vessel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vessels/{id}")
    public ResponseEntity<Vessel> getVessel(@PathVariable Long id) {
        log.debug("REST request to get Vessel : {}", id);
        Optional<Vessel> vessel = vesselRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(vessel);
    }

    /**
     * {@code DELETE  /vessels/:id} : delete the "id" vessel.
     *
     * @param id the id of the vessel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vessels/{id}")
    public ResponseEntity<Void> deleteVessel(@PathVariable Long id) {
        log.debug("REST request to delete Vessel : {}", id);
        vesselRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
