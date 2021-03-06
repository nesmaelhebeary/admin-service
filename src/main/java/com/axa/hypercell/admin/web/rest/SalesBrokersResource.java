package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.SalesBrokers;
import com.axa.hypercell.admin.repository.SalesBrokersRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.SalesBrokers}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SalesBrokersResource {

    private final Logger log = LoggerFactory.getLogger(SalesBrokersResource.class);

    private static final String ENTITY_NAME = "adminserviceSalesBrokers";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SalesBrokersRepository salesBrokersRepository;

    public SalesBrokersResource(SalesBrokersRepository salesBrokersRepository) {
        this.salesBrokersRepository = salesBrokersRepository;
    }

    /**
     * {@code POST  /sales-brokers} : Create a new salesBrokers.
     *
     * @param salesBrokers the salesBrokers to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new salesBrokers, or with status {@code 400 (Bad Request)} if the salesBrokers has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sales-brokers")
    public ResponseEntity<SalesBrokers> createSalesBrokers(@RequestBody SalesBrokers salesBrokers) throws URISyntaxException {
        log.debug("REST request to save SalesBrokers : {}", salesBrokers);
        if (salesBrokers.getId() != null) {
            throw new BadRequestAlertException("A new salesBrokers cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SalesBrokers result = salesBrokersRepository.save(salesBrokers);
        return ResponseEntity.created(new URI("/api/sales-brokers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sales-brokers} : Updates an existing salesBrokers.
     *
     * @param salesBrokers the salesBrokers to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated salesBrokers,
     * or with status {@code 400 (Bad Request)} if the salesBrokers is not valid,
     * or with status {@code 500 (Internal Server Error)} if the salesBrokers couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sales-brokers")
    public ResponseEntity<SalesBrokers> updateSalesBrokers(@RequestBody SalesBrokers salesBrokers) throws URISyntaxException {
        log.debug("REST request to update SalesBrokers : {}", salesBrokers);
        if (salesBrokers.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SalesBrokers result = salesBrokersRepository.save(salesBrokers);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, salesBrokers.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sales-brokers} : get all the salesBrokers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of salesBrokers in body.
     */
    @GetMapping("/sales-brokers")
    public List<SalesBrokers> getAllSalesBrokers() {
        log.debug("REST request to get all SalesBrokers");
        return salesBrokersRepository.findAll();
    }

    /**
     * {@code GET  /sales-brokers/:id} : get the "id" salesBrokers.
     *
     * @param id the id of the salesBrokers to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the salesBrokers, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sales-brokers/{id}")
    public ResponseEntity<SalesBrokers> getSalesBrokers(@PathVariable Long id) {
        log.debug("REST request to get SalesBrokers : {}", id);
        Optional<SalesBrokers> salesBrokers = salesBrokersRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(salesBrokers);
    }

    /**
     * {@code DELETE  /sales-brokers/:id} : delete the "id" salesBrokers.
     *
     * @param id the id of the salesBrokers to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sales-brokers/{id}")
    public ResponseEntity<Void> deleteSalesBrokers(@PathVariable Long id) {
        log.debug("REST request to delete SalesBrokers : {}", id);
        salesBrokersRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
