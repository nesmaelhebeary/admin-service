package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.ExtensionParameters;
import com.axa.hypercell.admin.repository.ExtensionParametersRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.ExtensionParameters}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ExtensionParametersResource {

    private final Logger log = LoggerFactory.getLogger(ExtensionParametersResource.class);

    private static final String ENTITY_NAME = "adminserviceExtensionParameters";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExtensionParametersRepository extensionParametersRepository;

    public ExtensionParametersResource(ExtensionParametersRepository extensionParametersRepository) {
        this.extensionParametersRepository = extensionParametersRepository;
    }

    /**
     * {@code POST  /extension-parameters} : Create a new extensionParameters.
     *
     * @param extensionParameters the extensionParameters to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new extensionParameters, or with status {@code 400 (Bad Request)} if the extensionParameters has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/extension-parameters")
    public ResponseEntity<ExtensionParameters> createExtensionParameters(@RequestBody ExtensionParameters extensionParameters)
        throws URISyntaxException {
        log.debug("REST request to save ExtensionParameters : {}", extensionParameters);
        if (extensionParameters.getId() != null) {
            throw new BadRequestAlertException("A new extensionParameters cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExtensionParameters result = extensionParametersRepository.save(extensionParameters);
        return ResponseEntity
            .created(new URI("/api/extension-parameters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /extension-parameters/:id} : Updates an existing extensionParameters.
     *
     * @param id the id of the extensionParameters to save.
     * @param extensionParameters the extensionParameters to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated extensionParameters,
     * or with status {@code 400 (Bad Request)} if the extensionParameters is not valid,
     * or with status {@code 500 (Internal Server Error)} if the extensionParameters couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/extension-parameters/{id}")
    public ResponseEntity<ExtensionParameters> updateExtensionParameters(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExtensionParameters extensionParameters
    ) throws URISyntaxException {
        log.debug("REST request to update ExtensionParameters : {}, {}", id, extensionParameters);
        if (extensionParameters.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, extensionParameters.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!extensionParametersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExtensionParameters result = extensionParametersRepository.save(extensionParameters);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, extensionParameters.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /extension-parameters/:id} : Partial updates given fields of an existing extensionParameters, field will ignore if it is null
     *
     * @param id the id of the extensionParameters to save.
     * @param extensionParameters the extensionParameters to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated extensionParameters,
     * or with status {@code 400 (Bad Request)} if the extensionParameters is not valid,
     * or with status {@code 404 (Not Found)} if the extensionParameters is not found,
     * or with status {@code 500 (Internal Server Error)} if the extensionParameters couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/extension-parameters/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ExtensionParameters> partialUpdateExtensionParameters(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ExtensionParameters extensionParameters
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExtensionParameters partially : {}, {}", id, extensionParameters);
        if (extensionParameters.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, extensionParameters.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!extensionParametersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExtensionParameters> result = extensionParametersRepository
            .findById(extensionParameters.getId())
            .map(
                existingExtensionParameters -> {
                    if (extensionParameters.getParameterId() != null) {
                        existingExtensionParameters.setParameterId(extensionParameters.getParameterId());
                    }
                    if (extensionParameters.getExtensionId() != null) {
                        existingExtensionParameters.setExtensionId(extensionParameters.getExtensionId());
                    }

                    return existingExtensionParameters;
                }
            )
            .map(extensionParametersRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, extensionParameters.getId().toString())
        );
    }

    /**
     * {@code GET  /extension-parameters} : get all the extensionParameters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of extensionParameters in body.
     */
    @GetMapping("/extension-parameters")
    public List<ExtensionParameters> getAllExtensionParameters() {
        log.debug("REST request to get all ExtensionParameters");
        return extensionParametersRepository.findAll();
    }

    /**
     * {@code GET  /extension-parameters/:id} : get the "id" extensionParameters.
     *
     * @param id the id of the extensionParameters to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the extensionParameters, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/extension-parameters/{id}")
    public ResponseEntity<ExtensionParameters> getExtensionParameters(@PathVariable Long id) {
        log.debug("REST request to get ExtensionParameters : {}", id);
        Optional<ExtensionParameters> extensionParameters = extensionParametersRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(extensionParameters);
    }

    /**
     * {@code DELETE  /extension-parameters/:id} : delete the "id" extensionParameters.
     *
     * @param id the id of the extensionParameters to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/extension-parameters/{id}")
    public ResponseEntity<Void> deleteExtensionParameters(@PathVariable Long id) {
        log.debug("REST request to delete ExtensionParameters : {}", id);
        extensionParametersRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
