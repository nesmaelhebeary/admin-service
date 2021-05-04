package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.ProductsCommissionSchema;
import com.axa.hypercell.admin.repository.ProductsCommissionSchemaRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.ProductsCommissionSchema}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ProductsCommissionSchemaResource {

    private final Logger log = LoggerFactory.getLogger(ProductsCommissionSchemaResource.class);

    private static final String ENTITY_NAME = "adminserviceProductsCommissionSchema";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductsCommissionSchemaRepository productsCommissionSchemaRepository;

    public ProductsCommissionSchemaResource(ProductsCommissionSchemaRepository productsCommissionSchemaRepository) {
        this.productsCommissionSchemaRepository = productsCommissionSchemaRepository;
    }

    /**
     * {@code POST  /products-commission-schemas} : Create a new productsCommissionSchema.
     *
     * @param productsCommissionSchema the productsCommissionSchema to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productsCommissionSchema, or with status {@code 400 (Bad Request)} if the productsCommissionSchema has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/products-commission-schemas")
    public ResponseEntity<ProductsCommissionSchema> createProductsCommissionSchema(
        @RequestBody ProductsCommissionSchema productsCommissionSchema
    ) throws URISyntaxException {
        log.debug("REST request to save ProductsCommissionSchema : {}", productsCommissionSchema);
        if (productsCommissionSchema.getId() != null) {
            throw new BadRequestAlertException("A new productsCommissionSchema cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductsCommissionSchema result = productsCommissionSchemaRepository.save(productsCommissionSchema);
        return ResponseEntity
            .created(new URI("/api/products-commission-schemas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /products-commission-schemas/:id} : Updates an existing productsCommissionSchema.
     *
     * @param id the id of the productsCommissionSchema to save.
     * @param productsCommissionSchema the productsCommissionSchema to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productsCommissionSchema,
     * or with status {@code 400 (Bad Request)} if the productsCommissionSchema is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productsCommissionSchema couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/products-commission-schemas/{id}")
    public ResponseEntity<ProductsCommissionSchema> updateProductsCommissionSchema(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductsCommissionSchema productsCommissionSchema
    ) throws URISyntaxException {
        log.debug("REST request to update ProductsCommissionSchema : {}, {}", id, productsCommissionSchema);
        if (productsCommissionSchema.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productsCommissionSchema.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productsCommissionSchemaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProductsCommissionSchema result = productsCommissionSchemaRepository.save(productsCommissionSchema);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productsCommissionSchema.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /products-commission-schemas/:id} : Partial updates given fields of an existing productsCommissionSchema, field will ignore if it is null
     *
     * @param id the id of the productsCommissionSchema to save.
     * @param productsCommissionSchema the productsCommissionSchema to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productsCommissionSchema,
     * or with status {@code 400 (Bad Request)} if the productsCommissionSchema is not valid,
     * or with status {@code 404 (Not Found)} if the productsCommissionSchema is not found,
     * or with status {@code 500 (Internal Server Error)} if the productsCommissionSchema couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/products-commission-schemas/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ProductsCommissionSchema> partialUpdateProductsCommissionSchema(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProductsCommissionSchema productsCommissionSchema
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProductsCommissionSchema partially : {}, {}", id, productsCommissionSchema);
        if (productsCommissionSchema.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productsCommissionSchema.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productsCommissionSchemaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductsCommissionSchema> result = productsCommissionSchemaRepository
            .findById(productsCommissionSchema.getId())
            .map(
                existingProductsCommissionSchema -> {
                    if (productsCommissionSchema.getProductId() != null) {
                        existingProductsCommissionSchema.setProductId(productsCommissionSchema.getProductId());
                    }
                    if (productsCommissionSchema.getNameEn() != null) {
                        existingProductsCommissionSchema.setNameEn(productsCommissionSchema.getNameEn());
                    }
                    if (productsCommissionSchema.getNameAr() != null) {
                        existingProductsCommissionSchema.setNameAr(productsCommissionSchema.getNameAr());
                    }
                    if (productsCommissionSchema.getDisplayInTemplate() != null) {
                        existingProductsCommissionSchema.setDisplayInTemplate(productsCommissionSchema.getDisplayInTemplate());
                    }

                    return existingProductsCommissionSchema;
                }
            )
            .map(productsCommissionSchemaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productsCommissionSchema.getId().toString())
        );
    }

    /**
     * {@code GET  /products-commission-schemas} : get all the productsCommissionSchemas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productsCommissionSchemas in body.
     */
    @GetMapping("/products-commission-schemas")
    public List<ProductsCommissionSchema> getAllProductsCommissionSchemas() {
        log.debug("REST request to get all ProductsCommissionSchemas");
        return productsCommissionSchemaRepository.findAll();
    }

    /**
     * {@code GET  /products-commission-schemas/:id} : get the "id" productsCommissionSchema.
     *
     * @param id the id of the productsCommissionSchema to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productsCommissionSchema, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/products-commission-schemas/{id}")
    public ResponseEntity<ProductsCommissionSchema> getProductsCommissionSchema(@PathVariable Long id) {
        log.debug("REST request to get ProductsCommissionSchema : {}", id);
        Optional<ProductsCommissionSchema> productsCommissionSchema = productsCommissionSchemaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(productsCommissionSchema);
    }

    /**
     * {@code DELETE  /products-commission-schemas/:id} : delete the "id" productsCommissionSchema.
     *
     * @param id the id of the productsCommissionSchema to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/products-commission-schemas/{id}")
    public ResponseEntity<Void> deleteProductsCommissionSchema(@PathVariable Long id) {
        log.debug("REST request to delete ProductsCommissionSchema : {}", id);
        productsCommissionSchemaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
