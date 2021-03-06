package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.ProductsAttr;
import com.axa.hypercell.admin.repository.ProductsAttrRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.ProductsAttr}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProductsAttrResource {

    private final Logger log = LoggerFactory.getLogger(ProductsAttrResource.class);

    private static final String ENTITY_NAME = "adminserviceProductsAttr";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductsAttrRepository productsAttrRepository;

    public ProductsAttrResource(ProductsAttrRepository productsAttrRepository) {
        this.productsAttrRepository = productsAttrRepository;
    }

    /**
     * {@code POST  /products-attrs} : Create a new productsAttr.
     *
     * @param productsAttr the productsAttr to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productsAttr, or with status {@code 400 (Bad Request)} if the productsAttr has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/products-attrs")
    public ResponseEntity<ProductsAttr> createProductsAttr(@RequestBody ProductsAttr productsAttr) throws URISyntaxException {
        log.debug("REST request to save ProductsAttr : {}", productsAttr);
        if (productsAttr.getId() != null) {
            throw new BadRequestAlertException("A new productsAttr cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductsAttr result = productsAttrRepository.save(productsAttr);
        return ResponseEntity.created(new URI("/api/products-attrs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /products-attrs} : Updates an existing productsAttr.
     *
     * @param productsAttr the productsAttr to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productsAttr,
     * or with status {@code 400 (Bad Request)} if the productsAttr is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productsAttr couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/products-attrs")
    public ResponseEntity<ProductsAttr> updateProductsAttr(@RequestBody ProductsAttr productsAttr) throws URISyntaxException {
        log.debug("REST request to update ProductsAttr : {}", productsAttr);
        if (productsAttr.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductsAttr result = productsAttrRepository.save(productsAttr);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productsAttr.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /products-attrs} : get all the productsAttrs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productsAttrs in body.
     */
    @GetMapping("/products-attrs")
    public List<ProductsAttr> getAllProductsAttrs() {
        log.debug("REST request to get all ProductsAttrs");
        return productsAttrRepository.findAll();
    }

    /**
     * {@code GET  /products-attrs/:id} : get the "id" productsAttr.
     *
     * @param id the id of the productsAttr to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productsAttr, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/products-attrs/{id}")
    public ResponseEntity<ProductsAttr> getProductsAttr(@PathVariable Long id) {
        log.debug("REST request to get ProductsAttr : {}", id);
        Optional<ProductsAttr> productsAttr = productsAttrRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productsAttr);
    }

    /**
     * {@code DELETE  /products-attrs/:id} : delete the "id" productsAttr.
     *
     * @param id the id of the productsAttr to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/products-attrs/{id}")
    public ResponseEntity<Void> deleteProductsAttr(@PathVariable Long id) {
        log.debug("REST request to delete ProductsAttr : {}", id);
        productsAttrRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
