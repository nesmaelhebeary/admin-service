package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.NaceCode;
import com.axa.hypercell.admin.repository.NaceCodeRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.NaceCode}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class NaceCodeResource {

    private final Logger log = LoggerFactory.getLogger(NaceCodeResource.class);

    private static final String ENTITY_NAME = "adminserviceNaceCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NaceCodeRepository naceCodeRepository;

    public NaceCodeResource(NaceCodeRepository naceCodeRepository) {
        this.naceCodeRepository = naceCodeRepository;
    }

    /**
     * {@code POST  /nace-codes} : Create a new naceCode.
     *
     * @param naceCode the naceCode to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new naceCode, or with status {@code 400 (Bad Request)} if the naceCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nace-codes")
    public ResponseEntity<NaceCode> createNaceCode(@RequestBody NaceCode naceCode) throws URISyntaxException {
        log.debug("REST request to save NaceCode : {}", naceCode);
        if (naceCode.getId() != null) {
            throw new BadRequestAlertException("A new naceCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NaceCode result = naceCodeRepository.save(naceCode);
        return ResponseEntity.created(new URI("/api/nace-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /nace-codes} : Updates an existing naceCode.
     *
     * @param naceCode the naceCode to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated naceCode,
     * or with status {@code 400 (Bad Request)} if the naceCode is not valid,
     * or with status {@code 500 (Internal Server Error)} if the naceCode couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nace-codes")
    public ResponseEntity<NaceCode> updateNaceCode(@RequestBody NaceCode naceCode) throws URISyntaxException {
        log.debug("REST request to update NaceCode : {}", naceCode);
        if (naceCode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NaceCode result = naceCodeRepository.save(naceCode);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, naceCode.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /nace-codes} : get all the naceCodes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of naceCodes in body.
     */
    @GetMapping("/nace-codes")
    public List<NaceCode> getAllNaceCodes() {
        log.debug("REST request to get all NaceCodes");
        return naceCodeRepository.findAll();
    }

    /**
     * {@code GET  /nace-codes/:id} : get the "id" naceCode.
     *
     * @param id the id of the naceCode to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the naceCode, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nace-codes/{id}")
    public ResponseEntity<NaceCode> getNaceCode(@PathVariable Long id) {
        log.debug("REST request to get NaceCode : {}", id);
        Optional<NaceCode> naceCode = naceCodeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(naceCode);
    }

    /**
     * {@code DELETE  /nace-codes/:id} : delete the "id" naceCode.
     *
     * @param id the id of the naceCode to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nace-codes/{id}")
    public ResponseEntity<Void> deleteNaceCode(@PathVariable Long id) {
        log.debug("REST request to delete NaceCode : {}", id);
        naceCodeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
