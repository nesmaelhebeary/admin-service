package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.PolicyType;
import com.axa.hypercell.admin.repository.PolicyTypeRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.PolicyType}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PolicyTypeResource {

    private final Logger log = LoggerFactory.getLogger(PolicyTypeResource.class);

    private static final String ENTITY_NAME = "adminservicePolicyType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PolicyTypeRepository policyTypeRepository;

    public PolicyTypeResource(PolicyTypeRepository policyTypeRepository) {
        this.policyTypeRepository = policyTypeRepository;
    }

    /**
     * {@code POST  /policy-types} : Create a new policyType.
     *
     * @param policyType the policyType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new policyType, or with status {@code 400 (Bad Request)} if the policyType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/policy-types")
    public ResponseEntity<PolicyType> createPolicyType(@RequestBody PolicyType policyType) throws URISyntaxException {
        log.debug("REST request to save PolicyType : {}", policyType);
        if (policyType.getId() != null) {
            throw new BadRequestAlertException("A new policyType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PolicyType result = policyTypeRepository.save(policyType);
        return ResponseEntity
            .created(new URI("/api/policy-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /policy-types/:id} : Updates an existing policyType.
     *
     * @param id the id of the policyType to save.
     * @param policyType the policyType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated policyType,
     * or with status {@code 400 (Bad Request)} if the policyType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the policyType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/policy-types/{id}")
    public ResponseEntity<PolicyType> updatePolicyType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PolicyType policyType
    ) throws URISyntaxException {
        log.debug("REST request to update PolicyType : {}, {}", id, policyType);
        if (policyType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, policyType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!policyTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PolicyType result = policyTypeRepository.save(policyType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, policyType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /policy-types/:id} : Partial updates given fields of an existing policyType, field will ignore if it is null
     *
     * @param id the id of the policyType to save.
     * @param policyType the policyType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated policyType,
     * or with status {@code 400 (Bad Request)} if the policyType is not valid,
     * or with status {@code 404 (Not Found)} if the policyType is not found,
     * or with status {@code 500 (Internal Server Error)} if the policyType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/policy-types/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PolicyType> partialUpdatePolicyType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PolicyType policyType
    ) throws URISyntaxException {
        log.debug("REST request to partial update PolicyType partially : {}, {}", id, policyType);
        if (policyType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, policyType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!policyTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PolicyType> result = policyTypeRepository
            .findById(policyType.getId())
            .map(
                existingPolicyType -> {
                    if (policyType.getName() != null) {
                        existingPolicyType.setName(policyType.getName());
                    }

                    return existingPolicyType;
                }
            )
            .map(policyTypeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, policyType.getId().toString())
        );
    }

    /**
     * {@code GET  /policy-types} : get all the policyTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of policyTypes in body.
     */
    @GetMapping("/policy-types")
    public List<PolicyType> getAllPolicyTypes() {
        log.debug("REST request to get all PolicyTypes");
        return policyTypeRepository.findAll();
    }

    /**
     * {@code GET  /policy-types/:id} : get the "id" policyType.
     *
     * @param id the id of the policyType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the policyType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/policy-types/{id}")
    public ResponseEntity<PolicyType> getPolicyType(@PathVariable Long id) {
        log.debug("REST request to get PolicyType : {}", id);
        Optional<PolicyType> policyType = policyTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(policyType);
    }

    /**
     * {@code DELETE  /policy-types/:id} : delete the "id" policyType.
     *
     * @param id the id of the policyType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/policy-types/{id}")
    public ResponseEntity<Void> deletePolicyType(@PathVariable Long id) {
        log.debug("REST request to delete PolicyType : {}", id);
        policyTypeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
