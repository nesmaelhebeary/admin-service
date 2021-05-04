package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.Packing;
import com.axa.hypercell.admin.repository.PackingRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.Packing}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PackingResource {

    private final Logger log = LoggerFactory.getLogger(PackingResource.class);

    private static final String ENTITY_NAME = "adminservicePacking";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PackingRepository packingRepository;

    public PackingResource(PackingRepository packingRepository) {
        this.packingRepository = packingRepository;
    }

    /**
     * {@code POST  /packings} : Create a new packing.
     *
     * @param packing the packing to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new packing, or with status {@code 400 (Bad Request)} if the packing has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/packings")
    public ResponseEntity<Packing> createPacking(@RequestBody Packing packing) throws URISyntaxException {
        log.debug("REST request to save Packing : {}", packing);
        if (packing.getId() != null) {
            throw new BadRequestAlertException("A new packing cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Packing result = packingRepository.save(packing);
        return ResponseEntity
            .created(new URI("/api/packings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /packings/:id} : Updates an existing packing.
     *
     * @param id the id of the packing to save.
     * @param packing the packing to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated packing,
     * or with status {@code 400 (Bad Request)} if the packing is not valid,
     * or with status {@code 500 (Internal Server Error)} if the packing couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/packings/{id}")
    public ResponseEntity<Packing> updatePacking(@PathVariable(value = "id", required = false) final Long id, @RequestBody Packing packing)
        throws URISyntaxException {
        log.debug("REST request to update Packing : {}, {}", id, packing);
        if (packing.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, packing.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!packingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Packing result = packingRepository.save(packing);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, packing.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /packings/:id} : Partial updates given fields of an existing packing, field will ignore if it is null
     *
     * @param id the id of the packing to save.
     * @param packing the packing to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated packing,
     * or with status {@code 400 (Bad Request)} if the packing is not valid,
     * or with status {@code 404 (Not Found)} if the packing is not found,
     * or with status {@code 500 (Internal Server Error)} if the packing couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/packings/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Packing> partialUpdatePacking(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Packing packing
    ) throws URISyntaxException {
        log.debug("REST request to partial update Packing partially : {}, {}", id, packing);
        if (packing.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, packing.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!packingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Packing> result = packingRepository
            .findById(packing.getId())
            .map(
                existingPacking -> {
                    if (packing.getNameEnglish() != null) {
                        existingPacking.setNameEnglish(packing.getNameEnglish());
                    }
                    if (packing.getNameArabic() != null) {
                        existingPacking.setNameArabic(packing.getNameArabic());
                    }

                    return existingPacking;
                }
            )
            .map(packingRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, packing.getId().toString())
        );
    }

    /**
     * {@code GET  /packings} : get all the packings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of packings in body.
     */
    @GetMapping("/packings")
    public List<Packing> getAllPackings() {
        log.debug("REST request to get all Packings");
        return packingRepository.findAll();
    }

    /**
     * {@code GET  /packings/:id} : get the "id" packing.
     *
     * @param id the id of the packing to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the packing, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/packings/{id}")
    public ResponseEntity<Packing> getPacking(@PathVariable Long id) {
        log.debug("REST request to get Packing : {}", id);
        Optional<Packing> packing = packingRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(packing);
    }

    /**
     * {@code DELETE  /packings/:id} : delete the "id" packing.
     *
     * @param id the id of the packing to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/packings/{id}")
    public ResponseEntity<Void> deletePacking(@PathVariable Long id) {
        log.debug("REST request to delete Packing : {}", id);
        packingRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
