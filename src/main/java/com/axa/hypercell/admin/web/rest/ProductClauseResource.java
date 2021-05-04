package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.ProductClause;
import com.axa.hypercell.admin.repository.ProductClauseRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.ProductClause}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProductClauseResource {

    private final Logger log = LoggerFactory.getLogger(ProductClauseResource.class);

    private static final String ENTITY_NAME = "adminserviceProductClause";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductClauseRepository productClauseRepository;

    public ProductClauseResource(ProductClauseRepository productClauseRepository) {
        this.productClauseRepository = productClauseRepository;
    }

    /**
     * {@code POST  /product-clauses} : Create a new productClause.
     *
     * @param productClause the productClause to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productClause, or with status {@code 400 (Bad Request)} if the productClause has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-clauses")
    public ResponseEntity<ProductClause> createProductClause(@RequestBody ProductClause productClause) throws URISyntaxException {
        log.debug("REST request to save ProductClause : {}", productClause);
        if (productClause.getId() != null) {
            throw new BadRequestAlertException("A new productClause cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductClause result = productClauseRepository.save(productClause);
        return ResponseEntity
            .created(new URI("/api/product-clauses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-clauses/:id} : Updates an existing productClause.
     *
     * @param id the id of the productClause to save.
     * @param productClause the productClause to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productClause,
     * or with status {@code 400 (Bad Request)} if the productClause is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productClause couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-clauses/{id}")
    public ResponseEntity<ProductClause> updateProductClause(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductClause productClause
    ) throws URISyntaxException {
        log.debug("REST request to update ProductClause : {}, {}", id, productClause);
        if (productClause.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productClause.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productClauseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductClause result = productClauseRepository.save(productClause);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productClause.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-clauses/:id} : Partial updates given fields of an existing productClause, field will ignore if it is null
     *
     * @param id the id of the productClause to save.
     * @param productClause the productClause to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productClause,
     * or with status {@code 400 (Bad Request)} if the productClause is not valid,
     * or with status {@code 404 (Not Found)} if the productClause is not found,
     * or with status {@code 500 (Internal Server Error)} if the productClause couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-clauses/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProductClause> partialUpdateProductClause(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductClause productClause
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductClause partially : {}, {}", id, productClause);
        if (productClause.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productClause.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productClauseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductClause> result = productClauseRepository
            .findById(productClause.getId())
            .map(
                existingProductClause -> {
                    if (productClause.getProductId() != null) {
                        existingProductClause.setProductId(productClause.getProductId());
                    }
                    if (productClause.getClauseId() != null) {
                        existingProductClause.setClauseId(productClause.getClauseId());
                    }

                    return existingProductClause;
                }
            )
            .map(productClauseRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productClause.getId().toString())
        );
    }

    /**
     * {@code GET  /product-clauses} : get all the productClauses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productClauses in body.
     */
    @GetMapping("/product-clauses")
    public List<ProductClause> getAllProductClauses() {
        log.debug("REST request to get all ProductClauses");
        return productClauseRepository.findAll();
    }

    /**
     * {@code GET  /product-clauses/:id} : get the "id" productClause.
     *
     * @param id the id of the productClause to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productClause, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-clauses/{id}")
    public ResponseEntity<ProductClause> getProductClause(@PathVariable Long id) {
        log.debug("REST request to get ProductClause : {}", id);
        Optional<ProductClause> productClause = productClauseRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productClause);
    }

    /**
     * {@code DELETE  /product-clauses/:id} : delete the "id" productClause.
     *
     * @param id the id of the productClause to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-clauses/{id}")
    public ResponseEntity<Void> deleteProductClause(@PathVariable Long id) {
        log.debug("REST request to delete ProductClause : {}", id);
        productClauseRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
