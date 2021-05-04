package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.FireLineSettings;
import com.axa.hypercell.admin.repository.FireLineSettingsRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.FireLineSettings}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FireLineSettingsResource {

    private final Logger log = LoggerFactory.getLogger(FireLineSettingsResource.class);

    private static final String ENTITY_NAME = "adminserviceFireLineSettings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FireLineSettingsRepository fireLineSettingsRepository;

    public FireLineSettingsResource(FireLineSettingsRepository fireLineSettingsRepository) {
        this.fireLineSettingsRepository = fireLineSettingsRepository;
    }

    /**
     * {@code POST  /fire-line-settings} : Create a new fireLineSettings.
     *
     * @param fireLineSettings the fireLineSettings to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fireLineSettings, or with status {@code 400 (Bad Request)} if the fireLineSettings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fire-line-settings")
    public ResponseEntity<FireLineSettings> createFireLineSettings(@RequestBody FireLineSettings fireLineSettings)
        throws URISyntaxException {
        log.debug("REST request to save FireLineSettings : {}", fireLineSettings);
        if (fireLineSettings.getId() != null) {
            throw new BadRequestAlertException("A new fireLineSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FireLineSettings result = fireLineSettingsRepository.save(fireLineSettings);
        return ResponseEntity
            .created(new URI("/api/fire-line-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fire-line-settings/:id} : Updates an existing fireLineSettings.
     *
     * @param id the id of the fireLineSettings to save.
     * @param fireLineSettings the fireLineSettings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fireLineSettings,
     * or with status {@code 400 (Bad Request)} if the fireLineSettings is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fireLineSettings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fire-line-settings/{id}")
    public ResponseEntity<FireLineSettings> updateFireLineSettings(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FireLineSettings fireLineSettings
    ) throws URISyntaxException {
        log.debug("REST request to update FireLineSettings : {}, {}", id, fireLineSettings);
        if (fireLineSettings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fireLineSettings.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fireLineSettingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FireLineSettings result = fireLineSettingsRepository.save(fireLineSettings);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fireLineSettings.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fire-line-settings/:id} : Partial updates given fields of an existing fireLineSettings, field will ignore if it is null
     *
     * @param id the id of the fireLineSettings to save.
     * @param fireLineSettings the fireLineSettings to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fireLineSettings,
     * or with status {@code 400 (Bad Request)} if the fireLineSettings is not valid,
     * or with status {@code 404 (Not Found)} if the fireLineSettings is not found,
     * or with status {@code 500 (Internal Server Error)} if the fireLineSettings couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fire-line-settings/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<FireLineSettings> partialUpdateFireLineSettings(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FireLineSettings fireLineSettings
    ) throws URISyntaxException {
        log.debug("REST request to partial update FireLineSettings partially : {}, {}", id, fireLineSettings);
        if (fireLineSettings.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fireLineSettings.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fireLineSettingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FireLineSettings> result = fireLineSettingsRepository
            .findById(fireLineSettings.getId())
            .map(
                existingFireLineSettings -> {
                    if (fireLineSettings.getClassName() != null) {
                        existingFireLineSettings.setClassName(fireLineSettings.getClassName());
                    }
                    if (fireLineSettings.getFromValue() != null) {
                        existingFireLineSettings.setFromValue(fireLineSettings.getFromValue());
                    }
                    if (fireLineSettings.getToValue() != null) {
                        existingFireLineSettings.setToValue(fireLineSettings.getToValue());
                    }
                    if (fireLineSettings.getCurrency() != null) {
                        existingFireLineSettings.setCurrency(fireLineSettings.getCurrency());
                    }
                    if (fireLineSettings.getCoveragePercentage() != null) {
                        existingFireLineSettings.setCoveragePercentage(fireLineSettings.getCoveragePercentage());
                    }

                    return existingFireLineSettings;
                }
            )
            .map(fireLineSettingsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fireLineSettings.getId().toString())
        );
    }

    /**
     * {@code GET  /fire-line-settings} : get all the fireLineSettings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fireLineSettings in body.
     */
    @GetMapping("/fire-line-settings")
    public List<FireLineSettings> getAllFireLineSettings() {
        log.debug("REST request to get all FireLineSettings");
        return fireLineSettingsRepository.findAll();
    }

    /**
     * {@code GET  /fire-line-settings/:id} : get the "id" fireLineSettings.
     *
     * @param id the id of the fireLineSettings to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fireLineSettings, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fire-line-settings/{id}")
    public ResponseEntity<FireLineSettings> getFireLineSettings(@PathVariable Long id) {
        log.debug("REST request to get FireLineSettings : {}", id);
        Optional<FireLineSettings> fireLineSettings = fireLineSettingsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fireLineSettings);
    }

    /**
     * {@code DELETE  /fire-line-settings/:id} : delete the "id" fireLineSettings.
     *
     * @param id the id of the fireLineSettings to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fire-line-settings/{id}")
    public ResponseEntity<Void> deleteFireLineSettings(@PathVariable Long id) {
        log.debug("REST request to delete FireLineSettings : {}", id);
        fireLineSettingsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
