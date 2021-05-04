package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.Roof;
import com.axa.hypercell.admin.repository.RoofRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.Roof}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RoofResource {

    private final Logger log = LoggerFactory.getLogger(RoofResource.class);

    private static final String ENTITY_NAME = "adminserviceRoof";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoofRepository roofRepository;

    public RoofResource(RoofRepository roofRepository) {
        this.roofRepository = roofRepository;
    }

    /**
     * {@code POST  /roofs} : Create a new roof.
     *
     * @param roof the roof to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roof, or with status {@code 400 (Bad Request)} if the roof has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/roofs")
    public ResponseEntity<Roof> createRoof(@RequestBody Roof roof) throws URISyntaxException {
        log.debug("REST request to save Roof : {}", roof);
        if (roof.getId() != null) {
            throw new BadRequestAlertException("A new roof cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Roof result = roofRepository.save(roof);
        return ResponseEntity
            .created(new URI("/api/roofs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /roofs/:id} : Updates an existing roof.
     *
     * @param id the id of the roof to save.
     * @param roof the roof to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roof,
     * or with status {@code 400 (Bad Request)} if the roof is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roof couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/roofs/{id}")
    public ResponseEntity<Roof> updateRoof(@PathVariable(value = "id", required = false) final Long id, @RequestBody Roof roof)
        throws URISyntaxException {
        log.debug("REST request to update Roof : {}, {}", id, roof);
        if (roof.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roof.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roofRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Roof result = roofRepository.save(roof);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roof.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /roofs/:id} : Partial updates given fields of an existing roof, field will ignore if it is null
     *
     * @param id the id of the roof to save.
     * @param roof the roof to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roof,
     * or with status {@code 400 (Bad Request)} if the roof is not valid,
     * or with status {@code 404 (Not Found)} if the roof is not found,
     * or with status {@code 500 (Internal Server Error)} if the roof couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/roofs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Roof> partialUpdateRoof(@PathVariable(value = "id", required = false) final Long id, @RequestBody Roof roof)
        throws URISyntaxException {
        log.debug("REST request to partial update Roof partially : {}, {}", id, roof);
        if (roof.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roof.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roofRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Roof> result = roofRepository
            .findById(roof.getId())
            .map(
                existingRoof -> {
                    if (roof.getName() != null) {
                        existingRoof.setName(roof.getName());
                    }
                    if (roof.getDescription() != null) {
                        existingRoof.setDescription(roof.getDescription());
                    }

                    return existingRoof;
                }
            )
            .map(roofRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roof.getId().toString())
        );
    }

    /**
     * {@code GET  /roofs} : get all the roofs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roofs in body.
     */
    @GetMapping("/roofs")
    public List<Roof> getAllRoofs() {
        log.debug("REST request to get all Roofs");
        return roofRepository.findAll();
    }

    /**
     * {@code GET  /roofs/:id} : get the "id" roof.
     *
     * @param id the id of the roof to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roof, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/roofs/{id}")
    public ResponseEntity<Roof> getRoof(@PathVariable Long id) {
        log.debug("REST request to get Roof : {}", id);
        Optional<Roof> roof = roofRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(roof);
    }

    /**
     * {@code DELETE  /roofs/:id} : delete the "id" roof.
     *
     * @param id the id of the roof to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/roofs/{id}")
    public ResponseEntity<Void> deleteRoof(@PathVariable Long id) {
        log.debug("REST request to delete Roof : {}", id);
        roofRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
