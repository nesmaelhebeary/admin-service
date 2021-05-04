package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.EvaluationBasis;
import com.axa.hypercell.admin.repository.EvaluationBasisRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.EvaluationBasis}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EvaluationBasisResource {

    private final Logger log = LoggerFactory.getLogger(EvaluationBasisResource.class);

    private static final String ENTITY_NAME = "adminserviceEvaluationBasis";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EvaluationBasisRepository evaluationBasisRepository;

    public EvaluationBasisResource(EvaluationBasisRepository evaluationBasisRepository) {
        this.evaluationBasisRepository = evaluationBasisRepository;
    }

    /**
     * {@code POST  /evaluation-bases} : Create a new evaluationBasis.
     *
     * @param evaluationBasis the evaluationBasis to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new evaluationBasis, or with status {@code 400 (Bad Request)} if the evaluationBasis has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/evaluation-bases")
    public ResponseEntity<EvaluationBasis> createEvaluationBasis(@RequestBody EvaluationBasis evaluationBasis) throws URISyntaxException {
        log.debug("REST request to save EvaluationBasis : {}", evaluationBasis);
        if (evaluationBasis.getId() != null) {
            throw new BadRequestAlertException("A new evaluationBasis cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EvaluationBasis result = evaluationBasisRepository.save(evaluationBasis);
        return ResponseEntity
            .created(new URI("/api/evaluation-bases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /evaluation-bases/:id} : Updates an existing evaluationBasis.
     *
     * @param id the id of the evaluationBasis to save.
     * @param evaluationBasis the evaluationBasis to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evaluationBasis,
     * or with status {@code 400 (Bad Request)} if the evaluationBasis is not valid,
     * or with status {@code 500 (Internal Server Error)} if the evaluationBasis couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/evaluation-bases/{id}")
    public ResponseEntity<EvaluationBasis> updateEvaluationBasis(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EvaluationBasis evaluationBasis
    ) throws URISyntaxException {
        log.debug("REST request to update EvaluationBasis : {}, {}", id, evaluationBasis);
        if (evaluationBasis.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evaluationBasis.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evaluationBasisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EvaluationBasis result = evaluationBasisRepository.save(evaluationBasis);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evaluationBasis.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /evaluation-bases/:id} : Partial updates given fields of an existing evaluationBasis, field will ignore if it is null
     *
     * @param id the id of the evaluationBasis to save.
     * @param evaluationBasis the evaluationBasis to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated evaluationBasis,
     * or with status {@code 400 (Bad Request)} if the evaluationBasis is not valid,
     * or with status {@code 404 (Not Found)} if the evaluationBasis is not found,
     * or with status {@code 500 (Internal Server Error)} if the evaluationBasis couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/evaluation-bases/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<EvaluationBasis> partialUpdateEvaluationBasis(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EvaluationBasis evaluationBasis
    ) throws URISyntaxException {
        log.debug("REST request to partial update EvaluationBasis partially : {}, {}", id, evaluationBasis);
        if (evaluationBasis.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, evaluationBasis.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!evaluationBasisRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EvaluationBasis> result = evaluationBasisRepository
            .findById(evaluationBasis.getId())
            .map(
                existingEvaluationBasis -> {
                    if (evaluationBasis.getNameAr() != null) {
                        existingEvaluationBasis.setNameAr(evaluationBasis.getNameAr());
                    }
                    if (evaluationBasis.getNaemEn() != null) {
                        existingEvaluationBasis.setNaemEn(evaluationBasis.getNaemEn());
                    }

                    return existingEvaluationBasis;
                }
            )
            .map(evaluationBasisRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, evaluationBasis.getId().toString())
        );
    }

    /**
     * {@code GET  /evaluation-bases} : get all the evaluationBases.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of evaluationBases in body.
     */
    @GetMapping("/evaluation-bases")
    public List<EvaluationBasis> getAllEvaluationBases() {
        log.debug("REST request to get all EvaluationBases");
        return evaluationBasisRepository.findAll();
    }

    /**
     * {@code GET  /evaluation-bases/:id} : get the "id" evaluationBasis.
     *
     * @param id the id of the evaluationBasis to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the evaluationBasis, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/evaluation-bases/{id}")
    public ResponseEntity<EvaluationBasis> getEvaluationBasis(@PathVariable Long id) {
        log.debug("REST request to get EvaluationBasis : {}", id);
        Optional<EvaluationBasis> evaluationBasis = evaluationBasisRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(evaluationBasis);
    }

    /**
     * {@code DELETE  /evaluation-bases/:id} : delete the "id" evaluationBasis.
     *
     * @param id the id of the evaluationBasis to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/evaluation-bases/{id}")
    public ResponseEntity<Void> deleteEvaluationBasis(@PathVariable Long id) {
        log.debug("REST request to delete EvaluationBasis : {}", id);
        evaluationBasisRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
