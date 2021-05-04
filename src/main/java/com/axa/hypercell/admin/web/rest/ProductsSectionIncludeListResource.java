package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.ProductsSectionIncludeList;
import com.axa.hypercell.admin.repository.ProductsSectionIncludeListRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.ProductsSectionIncludeList}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProductsSectionIncludeListResource {

    private final Logger log = LoggerFactory.getLogger(ProductsSectionIncludeListResource.class);

    private static final String ENTITY_NAME = "adminserviceProductsSectionIncludeList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductsSectionIncludeListRepository productsSectionIncludeListRepository;

    public ProductsSectionIncludeListResource(ProductsSectionIncludeListRepository productsSectionIncludeListRepository) {
        this.productsSectionIncludeListRepository = productsSectionIncludeListRepository;
    }

    /**
     * {@code POST  /products-section-include-lists} : Create a new productsSectionIncludeList.
     *
     * @param productsSectionIncludeList the productsSectionIncludeList to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productsSectionIncludeList, or with status {@code 400 (Bad Request)} if the productsSectionIncludeList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/products-section-include-lists")
    public ResponseEntity<ProductsSectionIncludeList> createProductsSectionIncludeList(
        @RequestBody ProductsSectionIncludeList productsSectionIncludeList
    ) throws URISyntaxException {
        log.debug("REST request to save ProductsSectionIncludeList : {}", productsSectionIncludeList);
        if (productsSectionIncludeList.getId() != null) {
            throw new BadRequestAlertException("A new productsSectionIncludeList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductsSectionIncludeList result = productsSectionIncludeListRepository.save(productsSectionIncludeList);
        return ResponseEntity
            .created(new URI("/api/products-section-include-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /products-section-include-lists/:id} : Updates an existing productsSectionIncludeList.
     *
     * @param id the id of the productsSectionIncludeList to save.
     * @param productsSectionIncludeList the productsSectionIncludeList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productsSectionIncludeList,
     * or with status {@code 400 (Bad Request)} if the productsSectionIncludeList is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productsSectionIncludeList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/products-section-include-lists/{id}")
    public ResponseEntity<ProductsSectionIncludeList> updateProductsSectionIncludeList(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductsSectionIncludeList productsSectionIncludeList
    ) throws URISyntaxException {
        log.debug("REST request to update ProductsSectionIncludeList : {}, {}", id, productsSectionIncludeList);
        if (productsSectionIncludeList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productsSectionIncludeList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productsSectionIncludeListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductsSectionIncludeList result = productsSectionIncludeListRepository.save(productsSectionIncludeList);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productsSectionIncludeList.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /products-section-include-lists/:id} : Partial updates given fields of an existing productsSectionIncludeList, field will ignore if it is null
     *
     * @param id the id of the productsSectionIncludeList to save.
     * @param productsSectionIncludeList the productsSectionIncludeList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productsSectionIncludeList,
     * or with status {@code 400 (Bad Request)} if the productsSectionIncludeList is not valid,
     * or with status {@code 404 (Not Found)} if the productsSectionIncludeList is not found,
     * or with status {@code 500 (Internal Server Error)} if the productsSectionIncludeList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/products-section-include-lists/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProductsSectionIncludeList> partialUpdateProductsSectionIncludeList(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductsSectionIncludeList productsSectionIncludeList
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductsSectionIncludeList partially : {}, {}", id, productsSectionIncludeList);
        if (productsSectionIncludeList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productsSectionIncludeList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productsSectionIncludeListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductsSectionIncludeList> result = productsSectionIncludeListRepository
            .findById(productsSectionIncludeList.getId())
            .map(
                existingProductsSectionIncludeList -> {
                    if (productsSectionIncludeList.getSectionId() != null) {
                        existingProductsSectionIncludeList.setSectionId(productsSectionIncludeList.getSectionId());
                    }
                    if (productsSectionIncludeList.getOtherSectionId() != null) {
                        existingProductsSectionIncludeList.setOtherSectionId(productsSectionIncludeList.getOtherSectionId());
                    }

                    return existingProductsSectionIncludeList;
                }
            )
            .map(productsSectionIncludeListRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productsSectionIncludeList.getId().toString())
        );
    }

    /**
     * {@code GET  /products-section-include-lists} : get all the productsSectionIncludeLists.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productsSectionIncludeLists in body.
     */
    @GetMapping("/products-section-include-lists")
    public List<ProductsSectionIncludeList> getAllProductsSectionIncludeLists() {
        log.debug("REST request to get all ProductsSectionIncludeLists");
        return productsSectionIncludeListRepository.findAll();
    }

    /**
     * {@code GET  /products-section-include-lists/:id} : get the "id" productsSectionIncludeList.
     *
     * @param id the id of the productsSectionIncludeList to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productsSectionIncludeList, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/products-section-include-lists/{id}")
    public ResponseEntity<ProductsSectionIncludeList> getProductsSectionIncludeList(@PathVariable Long id) {
        log.debug("REST request to get ProductsSectionIncludeList : {}", id);
        Optional<ProductsSectionIncludeList> productsSectionIncludeList = productsSectionIncludeListRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productsSectionIncludeList);
    }

    /**
     * {@code DELETE  /products-section-include-lists/:id} : delete the "id" productsSectionIncludeList.
     *
     * @param id the id of the productsSectionIncludeList to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/products-section-include-lists/{id}")
    public ResponseEntity<Void> deleteProductsSectionIncludeList(@PathVariable Long id) {
        log.debug("REST request to delete ProductsSectionIncludeList : {}", id);
        productsSectionIncludeListRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
