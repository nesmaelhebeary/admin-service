package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.ProductExtensions;
import com.axa.hypercell.admin.repository.ProductExtensionsRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.ProductExtensions}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProductExtensionsResource {

    private final Logger log = LoggerFactory.getLogger(ProductExtensionsResource.class);

    private static final String ENTITY_NAME = "adminserviceProductExtensions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductExtensionsRepository productExtensionsRepository;

    public ProductExtensionsResource(ProductExtensionsRepository productExtensionsRepository) {
        this.productExtensionsRepository = productExtensionsRepository;
    }

    /**
     * {@code POST  /product-extensions} : Create a new productExtensions.
     *
     * @param productExtensions the productExtensions to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productExtensions, or with status {@code 400 (Bad Request)} if the productExtensions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-extensions")
    public ResponseEntity<ProductExtensions> createProductExtensions(@RequestBody ProductExtensions productExtensions)
        throws URISyntaxException {
        log.debug("REST request to save ProductExtensions : {}", productExtensions);
        if (productExtensions.getId() != null) {
            throw new BadRequestAlertException("A new productExtensions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductExtensions result = productExtensionsRepository.save(productExtensions);
        return ResponseEntity
            .created(new URI("/api/product-extensions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-extensions/:id} : Updates an existing productExtensions.
     *
     * @param id the id of the productExtensions to save.
     * @param productExtensions the productExtensions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productExtensions,
     * or with status {@code 400 (Bad Request)} if the productExtensions is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productExtensions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-extensions/{id}")
    public ResponseEntity<ProductExtensions> updateProductExtensions(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductExtensions productExtensions
    ) throws URISyntaxException {
        log.debug("REST request to update ProductExtensions : {}, {}", id, productExtensions);
        if (productExtensions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productExtensions.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productExtensionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductExtensions result = productExtensionsRepository.save(productExtensions);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productExtensions.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /product-extensions/:id} : Partial updates given fields of an existing productExtensions, field will ignore if it is null
     *
     * @param id the id of the productExtensions to save.
     * @param productExtensions the productExtensions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productExtensions,
     * or with status {@code 400 (Bad Request)} if the productExtensions is not valid,
     * or with status {@code 404 (Not Found)} if the productExtensions is not found,
     * or with status {@code 500 (Internal Server Error)} if the productExtensions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/product-extensions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProductExtensions> partialUpdateProductExtensions(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductExtensions productExtensions
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductExtensions partially : {}, {}", id, productExtensions);
        if (productExtensions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productExtensions.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productExtensionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductExtensions> result = productExtensionsRepository
            .findById(productExtensions.getId())
            .map(
                existingProductExtensions -> {
                    if (productExtensions.getProductId() != null) {
                        existingProductExtensions.setProductId(productExtensions.getProductId());
                    }
                    if (productExtensions.getExtensionId() != null) {
                        existingProductExtensions.setExtensionId(productExtensions.getExtensionId());
                    }

                    return existingProductExtensions;
                }
            )
            .map(productExtensionsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productExtensions.getId().toString())
        );
    }

    /**
     * {@code GET  /product-extensions} : get all the productExtensions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productExtensions in body.
     */
    @GetMapping("/product-extensions")
    public List<ProductExtensions> getAllProductExtensions() {
        log.debug("REST request to get all ProductExtensions");
        return productExtensionsRepository.findAll();
    }

    /**
     * {@code GET  /product-extensions/:id} : get the "id" productExtensions.
     *
     * @param id the id of the productExtensions to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productExtensions, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-extensions/{id}")
    public ResponseEntity<ProductExtensions> getProductExtensions(@PathVariable Long id) {
        log.debug("REST request to get ProductExtensions : {}", id);
        Optional<ProductExtensions> productExtensions = productExtensionsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productExtensions);
    }

    /**
     * {@code DELETE  /product-extensions/:id} : delete the "id" productExtensions.
     *
     * @param id the id of the productExtensions to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-extensions/{id}")
    public ResponseEntity<Void> deleteProductExtensions(@PathVariable Long id) {
        log.debug("REST request to delete ProductExtensions : {}", id);
        productExtensionsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
