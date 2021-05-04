package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.ReInsuranceCompany;
import com.axa.hypercell.admin.repository.ReInsuranceCompanyRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.ReInsuranceCompany}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ReInsuranceCompanyResource {

    private final Logger log = LoggerFactory.getLogger(ReInsuranceCompanyResource.class);

    private static final String ENTITY_NAME = "adminserviceReInsuranceCompany";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReInsuranceCompanyRepository reInsuranceCompanyRepository;

    public ReInsuranceCompanyResource(ReInsuranceCompanyRepository reInsuranceCompanyRepository) {
        this.reInsuranceCompanyRepository = reInsuranceCompanyRepository;
    }

    /**
     * {@code POST  /re-insurance-companies} : Create a new reInsuranceCompany.
     *
     * @param reInsuranceCompany the reInsuranceCompany to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reInsuranceCompany, or with status {@code 400 (Bad Request)} if the reInsuranceCompany has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/re-insurance-companies")
    public ResponseEntity<ReInsuranceCompany> createReInsuranceCompany(@RequestBody ReInsuranceCompany reInsuranceCompany)
        throws URISyntaxException {
        log.debug("REST request to save ReInsuranceCompany : {}", reInsuranceCompany);
        if (reInsuranceCompany.getId() != null) {
            throw new BadRequestAlertException("A new reInsuranceCompany cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReInsuranceCompany result = reInsuranceCompanyRepository.save(reInsuranceCompany);
        return ResponseEntity
            .created(new URI("/api/re-insurance-companies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /re-insurance-companies/:id} : Updates an existing reInsuranceCompany.
     *
     * @param id the id of the reInsuranceCompany to save.
     * @param reInsuranceCompany the reInsuranceCompany to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reInsuranceCompany,
     * or with status {@code 400 (Bad Request)} if the reInsuranceCompany is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reInsuranceCompany couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/re-insurance-companies/{id}")
    public ResponseEntity<ReInsuranceCompany> updateReInsuranceCompany(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReInsuranceCompany reInsuranceCompany
    ) throws URISyntaxException {
        log.debug("REST request to update ReInsuranceCompany : {}, {}", id, reInsuranceCompany);
        if (reInsuranceCompany.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reInsuranceCompany.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reInsuranceCompanyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReInsuranceCompany result = reInsuranceCompanyRepository.save(reInsuranceCompany);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reInsuranceCompany.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /re-insurance-companies/:id} : Partial updates given fields of an existing reInsuranceCompany, field will ignore if it is null
     *
     * @param id the id of the reInsuranceCompany to save.
     * @param reInsuranceCompany the reInsuranceCompany to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reInsuranceCompany,
     * or with status {@code 400 (Bad Request)} if the reInsuranceCompany is not valid,
     * or with status {@code 404 (Not Found)} if the reInsuranceCompany is not found,
     * or with status {@code 500 (Internal Server Error)} if the reInsuranceCompany couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/re-insurance-companies/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ReInsuranceCompany> partialUpdateReInsuranceCompany(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReInsuranceCompany reInsuranceCompany
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReInsuranceCompany partially : {}, {}", id, reInsuranceCompany);
        if (reInsuranceCompany.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reInsuranceCompany.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reInsuranceCompanyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReInsuranceCompany> result = reInsuranceCompanyRepository
            .findById(reInsuranceCompany.getId())
            .map(
                existingReInsuranceCompany -> {
                    if (reInsuranceCompany.getNameEn() != null) {
                        existingReInsuranceCompany.setNameEn(reInsuranceCompany.getNameEn());
                    }
                    if (reInsuranceCompany.getNameAr() != null) {
                        existingReInsuranceCompany.setNameAr(reInsuranceCompany.getNameAr());
                    }
                    if (reInsuranceCompany.getAddress() != null) {
                        existingReInsuranceCompany.setAddress(reInsuranceCompany.getAddress());
                    }
                    if (reInsuranceCompany.getDistrict() != null) {
                        existingReInsuranceCompany.setDistrict(reInsuranceCompany.getDistrict());
                    }
                    if (reInsuranceCompany.getCity() != null) {
                        existingReInsuranceCompany.setCity(reInsuranceCompany.getCity());
                    }
                    if (reInsuranceCompany.getCountry() != null) {
                        existingReInsuranceCompany.setCountry(reInsuranceCompany.getCountry());
                    }

                    return existingReInsuranceCompany;
                }
            )
            .map(reInsuranceCompanyRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reInsuranceCompany.getId().toString())
        );
    }

    /**
     * {@code GET  /re-insurance-companies} : get all the reInsuranceCompanies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reInsuranceCompanies in body.
     */
    @GetMapping("/re-insurance-companies")
    public List<ReInsuranceCompany> getAllReInsuranceCompanies() {
        log.debug("REST request to get all ReInsuranceCompanies");
        return reInsuranceCompanyRepository.findAll();
    }

    /**
     * {@code GET  /re-insurance-companies/:id} : get the "id" reInsuranceCompany.
     *
     * @param id the id of the reInsuranceCompany to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reInsuranceCompany, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/re-insurance-companies/{id}")
    public ResponseEntity<ReInsuranceCompany> getReInsuranceCompany(@PathVariable Long id) {
        log.debug("REST request to get ReInsuranceCompany : {}", id);
        Optional<ReInsuranceCompany> reInsuranceCompany = reInsuranceCompanyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(reInsuranceCompany);
    }

    /**
     * {@code DELETE  /re-insurance-companies/:id} : delete the "id" reInsuranceCompany.
     *
     * @param id the id of the reInsuranceCompany to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/re-insurance-companies/{id}")
    public ResponseEntity<Void> deleteReInsuranceCompany(@PathVariable Long id) {
        log.debug("REST request to delete ReInsuranceCompany : {}", id);
        reInsuranceCompanyRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
