package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.Extensions;
import com.axa.hypercell.admin.repository.ExtensionsRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.Extensions}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ExtensionsResource {

    private final Logger log = LoggerFactory.getLogger(ExtensionsResource.class);

    private static final String ENTITY_NAME = "adminserviceExtensions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExtensionsRepository extensionsRepository;

    public ExtensionsResource(ExtensionsRepository extensionsRepository) {
        this.extensionsRepository = extensionsRepository;
    }

    /**
     * {@code POST  /extensions} : Create a new extensions.
     *
     * @param extensions the extensions to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new extensions, or with status {@code 400 (Bad Request)} if the extensions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/extensions")
    public ResponseEntity<Extensions> createExtensions(@RequestBody Extensions extensions) throws URISyntaxException {
        log.debug("REST request to save Extensions : {}", extensions);
        if (extensions.getId() != null) {
            throw new BadRequestAlertException("A new extensions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Extensions result = extensionsRepository.save(extensions);
        return ResponseEntity
            .created(new URI("/api/extensions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /extensions/:id} : Updates an existing extensions.
     *
     * @param id the id of the extensions to save.
     * @param extensions the extensions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated extensions,
     * or with status {@code 400 (Bad Request)} if the extensions is not valid,
     * or with status {@code 500 (Internal Server Error)} if the extensions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/extensions/{id}")
    public ResponseEntity<Extensions> updateExtensions(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Extensions extensions
    ) throws URISyntaxException {
        log.debug("REST request to update Extensions : {}, {}", id, extensions);
        if (extensions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, extensions.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!extensionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Extensions result = extensionsRepository.save(extensions);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, extensions.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /extensions/:id} : Partial updates given fields of an existing extensions, field will ignore if it is null
     *
     * @param id the id of the extensions to save.
     * @param extensions the extensions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated extensions,
     * or with status {@code 400 (Bad Request)} if the extensions is not valid,
     * or with status {@code 404 (Not Found)} if the extensions is not found,
     * or with status {@code 500 (Internal Server Error)} if the extensions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/extensions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Extensions> partialUpdateExtensions(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Extensions extensions
    ) throws URISyntaxException {
        log.debug("REST request to partial update Extensions partially : {}, {}", id, extensions);
        if (extensions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, extensions.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!extensionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Extensions> result = extensionsRepository
            .findById(extensions.getId())
            .map(
                existingExtensions -> {
                    if (extensions.getCode() != null) {
                        existingExtensions.setCode(extensions.getCode());
                    }
                    if (extensions.getTextEn() != null) {
                        existingExtensions.setTextEn(extensions.getTextEn());
                    }
                    if (extensions.getTextAr() != null) {
                        existingExtensions.setTextAr(extensions.getTextAr());
                    }
                    if (extensions.getAffectMpl() != null) {
                        existingExtensions.setAffectMpl(extensions.getAffectMpl());
                    }
                    if (extensions.getEffectiveDate() != null) {
                        existingExtensions.setEffectiveDate(extensions.getEffectiveDate());
                    }
                    if (extensions.getStatus() != null) {
                        existingExtensions.setStatus(extensions.getStatus());
                    }

                    return existingExtensions;
                }
            )
            .map(extensionsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, extensions.getId().toString())
        );
    }

    /**
     * {@code GET  /extensions} : get all the extensions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of extensions in body.
     */
    @GetMapping("/extensions")
    public List<Extensions> getAllExtensions() {
        log.debug("REST request to get all Extensions");
        return extensionsRepository.findAll();
    }

    /**
     * {@code GET  /extensions/:id} : get the "id" extensions.
     *
     * @param id the id of the extensions to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the extensions, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/extensions/{id}")
    public ResponseEntity<Extensions> getExtensions(@PathVariable Long id) {
        log.debug("REST request to get Extensions : {}", id);
        Optional<Extensions> extensions = extensionsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(extensions);
    }

    /**
     * {@code DELETE  /extensions/:id} : delete the "id" extensions.
     *
     * @param id the id of the extensions to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/extensions/{id}")
    public ResponseEntity<Void> deleteExtensions(@PathVariable Long id) {
        log.debug("REST request to delete Extensions : {}", id);
        extensionsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
