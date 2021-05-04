package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.ProductsSection;
import com.axa.hypercell.admin.repository.ProductsSectionRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.ProductsSection}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProductsSectionResource {

    private final Logger log = LoggerFactory.getLogger(ProductsSectionResource.class);

    private static final String ENTITY_NAME = "adminserviceProductsSection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductsSectionRepository productsSectionRepository;

    public ProductsSectionResource(ProductsSectionRepository productsSectionRepository) {
        this.productsSectionRepository = productsSectionRepository;
    }

    /**
     * {@code POST  /products-sections} : Create a new productsSection.
     *
     * @param productsSection the productsSection to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productsSection, or with status {@code 400 (Bad Request)} if the productsSection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/products-sections")
    public ResponseEntity<ProductsSection> createProductsSection(@RequestBody ProductsSection productsSection) throws URISyntaxException {
        log.debug("REST request to save ProductsSection : {}", productsSection);
        if (productsSection.getId() != null) {
            throw new BadRequestAlertException("A new productsSection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductsSection result = productsSectionRepository.save(productsSection);
        return ResponseEntity
            .created(new URI("/api/products-sections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /products-sections/:id} : Updates an existing productsSection.
     *
     * @param id the id of the productsSection to save.
     * @param productsSection the productsSection to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productsSection,
     * or with status {@code 400 (Bad Request)} if the productsSection is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productsSection couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/products-sections/{id}")
    public ResponseEntity<ProductsSection> updateProductsSection(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductsSection productsSection
    ) throws URISyntaxException {
        log.debug("REST request to update ProductsSection : {}, {}", id, productsSection);
        if (productsSection.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productsSection.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productsSectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductsSection result = productsSectionRepository.save(productsSection);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productsSection.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /products-sections/:id} : Partial updates given fields of an existing productsSection, field will ignore if it is null
     *
     * @param id the id of the productsSection to save.
     * @param productsSection the productsSection to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productsSection,
     * or with status {@code 400 (Bad Request)} if the productsSection is not valid,
     * or with status {@code 404 (Not Found)} if the productsSection is not found,
     * or with status {@code 500 (Internal Server Error)} if the productsSection couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/products-sections/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProductsSection> partialUpdateProductsSection(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductsSection productsSection
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductsSection partially : {}, {}", id, productsSection);
        if (productsSection.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productsSection.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productsSectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductsSection> result = productsSectionRepository
            .findById(productsSection.getId())
            .map(
                existingProductsSection -> {
                    if (productsSection.getProductId() != null) {
                        existingProductsSection.setProductId(productsSection.getProductId());
                    }
                    if (productsSection.getNameEn() != null) {
                        existingProductsSection.setNameEn(productsSection.getNameEn());
                    }
                    if (productsSection.getNameAr() != null) {
                        existingProductsSection.setNameAr(productsSection.getNameAr());
                    }
                    if (productsSection.getStatus() != null) {
                        existingProductsSection.setStatus(productsSection.getStatus());
                    }
                    if (productsSection.getDefaultSumUp() != null) {
                        existingProductsSection.setDefaultSumUp(productsSection.getDefaultSumUp());
                    }

                    return existingProductsSection;
                }
            )
            .map(productsSectionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productsSection.getId().toString())
        );
    }

    /**
     * {@code GET  /products-sections} : get all the productsSections.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productsSections in body.
     */
    @GetMapping("/products-sections")
    public List<ProductsSection> getAllProductsSections() {
        log.debug("REST request to get all ProductsSections");
        return productsSectionRepository.findAll();
    }

    /**
     * {@code GET  /products-sections/:id} : get the "id" productsSection.
     *
     * @param id the id of the productsSection to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productsSection, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/products-sections/{id}")
    public ResponseEntity<ProductsSection> getProductsSection(@PathVariable Long id) {
        log.debug("REST request to get ProductsSection : {}", id);
        Optional<ProductsSection> productsSection = productsSectionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productsSection);
    }

    /**
     * {@code DELETE  /products-sections/:id} : delete the "id" productsSection.
     *
     * @param id the id of the productsSection to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/products-sections/{id}")
    public ResponseEntity<Void> deleteProductsSection(@PathVariable Long id) {
        log.debug("REST request to delete ProductsSection : {}", id);
        productsSectionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
