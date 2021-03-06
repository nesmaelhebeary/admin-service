package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.ProductTAndC;
import com.axa.hypercell.admin.repository.ProductTAndCRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.ProductTAndC}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProductTAndCResource {

    private final Logger log = LoggerFactory.getLogger(ProductTAndCResource.class);

    private static final String ENTITY_NAME = "adminserviceProductTAndC";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductTAndCRepository productTAndCRepository;

    public ProductTAndCResource(ProductTAndCRepository productTAndCRepository) {
        this.productTAndCRepository = productTAndCRepository;
    }

    /**
     * {@code POST  /product-t-and-cs} : Create a new productTAndC.
     *
     * @param productTAndC the productTAndC to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productTAndC, or with status {@code 400 (Bad Request)} if the productTAndC has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-t-and-cs")
    public ResponseEntity<ProductTAndC> createProductTAndC(@RequestBody ProductTAndC productTAndC) throws URISyntaxException {
        log.debug("REST request to save ProductTAndC : {}", productTAndC);
        if (productTAndC.getId() != null) {
            throw new BadRequestAlertException("A new productTAndC cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductTAndC result = productTAndCRepository.save(productTAndC);
        return ResponseEntity.created(new URI("/api/product-t-and-cs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-t-and-cs} : Updates an existing productTAndC.
     *
     * @param productTAndC the productTAndC to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productTAndC,
     * or with status {@code 400 (Bad Request)} if the productTAndC is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productTAndC couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-t-and-cs")
    public ResponseEntity<ProductTAndC> updateProductTAndC(@RequestBody ProductTAndC productTAndC) throws URISyntaxException {
        log.debug("REST request to update ProductTAndC : {}", productTAndC);
        if (productTAndC.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductTAndC result = productTAndCRepository.save(productTAndC);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productTAndC.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /product-t-and-cs} : get all the productTAndCS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productTAndCS in body.
     */
    @GetMapping("/product-t-and-cs")
    public List<ProductTAndC> getAllProductTAndCS() {
        log.debug("REST request to get all ProductTAndCS");
        return productTAndCRepository.findAll();
    }

    /**
     * {@code GET  /product-t-and-cs/:id} : get the "id" productTAndC.
     *
     * @param id the id of the productTAndC to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productTAndC, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-t-and-cs/{id}")
    public ResponseEntity<ProductTAndC> getProductTAndC(@PathVariable Long id) {
        log.debug("REST request to get ProductTAndC : {}", id);
        Optional<ProductTAndC> productTAndC = productTAndCRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productTAndC);
    }

    /**
     * {@code DELETE  /product-t-and-cs/:id} : delete the "id" productTAndC.
     *
     * @param id the id of the productTAndC to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-t-and-cs/{id}")
    public ResponseEntity<Void> deleteProductTAndC(@PathVariable Long id) {
        log.debug("REST request to delete ProductTAndC : {}", id);
        productTAndCRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
