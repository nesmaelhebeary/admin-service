package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.SubArea;
import com.axa.hypercell.admin.repository.SubAreaRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.SubArea}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SubAreaResource {

    private final Logger log = LoggerFactory.getLogger(SubAreaResource.class);

    private static final String ENTITY_NAME = "adminserviceSubArea";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubAreaRepository subAreaRepository;

    public SubAreaResource(SubAreaRepository subAreaRepository) {
        this.subAreaRepository = subAreaRepository;
    }

    /**
     * {@code POST  /sub-areas} : Create a new subArea.
     *
     * @param subArea the subArea to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subArea, or with status {@code 400 (Bad Request)} if the subArea has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sub-areas")
    public ResponseEntity<SubArea> createSubArea(@RequestBody SubArea subArea) throws URISyntaxException {
        log.debug("REST request to save SubArea : {}", subArea);
        if (subArea.getId() != null) {
            throw new BadRequestAlertException("A new subArea cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubArea result = subAreaRepository.save(subArea);
        return ResponseEntity.created(new URI("/api/sub-areas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sub-areas} : Updates an existing subArea.
     *
     * @param subArea the subArea to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subArea,
     * or with status {@code 400 (Bad Request)} if the subArea is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subArea couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sub-areas")
    public ResponseEntity<SubArea> updateSubArea(@RequestBody SubArea subArea) throws URISyntaxException {
        log.debug("REST request to update SubArea : {}", subArea);
        if (subArea.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SubArea result = subAreaRepository.save(subArea);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subArea.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sub-areas} : get all the subAreas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subAreas in body.
     */
    @GetMapping("/sub-areas")
    public List<SubArea> getAllSubAreas() {
        log.debug("REST request to get all SubAreas");
        return subAreaRepository.findAll();
    }

    /**
     * {@code GET  /sub-areas/:id} : get the "id" subArea.
     *
     * @param id the id of the subArea to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subArea, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sub-areas/{id}")
    public ResponseEntity<SubArea> getSubArea(@PathVariable Long id) {
        log.debug("REST request to get SubArea : {}", id);
        Optional<SubArea> subArea = subAreaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(subArea);
    }

    /**
     * {@code DELETE  /sub-areas/:id} : delete the "id" subArea.
     *
     * @param id the id of the subArea to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sub-areas/{id}")
    public ResponseEntity<Void> deleteSubArea(@PathVariable Long id) {
        log.debug("REST request to delete SubArea : {}", id);
        subAreaRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
