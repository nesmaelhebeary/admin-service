package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.ProductClauses;
import com.axa.hypercell.admin.repository.ProductClausesRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.ProductClauses}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProductClausesResource {

    private final Logger log = LoggerFactory.getLogger(ProductClausesResource.class);

    private static final String ENTITY_NAME = "adminserviceProductClauses";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductClausesRepository productClausesRepository;

    public ProductClausesResource(ProductClausesRepository productClausesRepository) {
        this.productClausesRepository = productClausesRepository;
    }

    /**
     * {@code POST  /product-clauses} : Create a new productClauses.
     *
     * @param productClauses the productClauses to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productClauses, or with status {@code 400 (Bad Request)} if the productClauses has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-clauses")
    public ResponseEntity<ProductClauses> createProductClauses(@RequestBody ProductClauses productClauses) throws URISyntaxException {
        log.debug("REST request to save ProductClauses : {}", productClauses);
        if (productClauses.getId() != null) {
            throw new BadRequestAlertException("A new productClauses cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductClauses result = productClausesRepository.save(productClauses);
        return ResponseEntity.created(new URI("/api/product-clauses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-clauses} : Updates an existing productClauses.
     *
     * @param productClauses the productClauses to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productClauses,
     * or with status {@code 400 (Bad Request)} if the productClauses is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productClauses couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-clauses")
    public ResponseEntity<ProductClauses> updateProductClauses(@RequestBody ProductClauses productClauses) throws URISyntaxException {
        log.debug("REST request to update ProductClauses : {}", productClauses);
        if (productClauses.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductClauses result = productClausesRepository.save(productClauses);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productClauses.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /product-clauses} : get all the productClauses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productClauses in body.
     */
    @GetMapping("/product-clauses")
    public List<ProductClauses> getAllProductClauses() {
        log.debug("REST request to get all ProductClauses");
        return productClausesRepository.findAll();
    }

    /**
     * {@code GET  /product-clauses/:id} : get the "id" productClauses.
     *
     * @param id the id of the productClauses to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productClauses, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-clauses/{id}")
    public ResponseEntity<ProductClauses> getProductClauses(@PathVariable Long id) {
        log.debug("REST request to get ProductClauses : {}", id);
        Optional<ProductClauses> productClauses = productClausesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productClauses);
    }

    /**
     * {@code DELETE  /product-clauses/:id} : delete the "id" productClauses.
     *
     * @param id the id of the productClauses to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-clauses/{id}")
    public ResponseEntity<Void> deleteProductClauses(@PathVariable Long id) {
        log.debug("REST request to delete ProductClauses : {}", id);
        productClausesRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
