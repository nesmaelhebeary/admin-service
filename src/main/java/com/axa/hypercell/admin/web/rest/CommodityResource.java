package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.Commodity;
import com.axa.hypercell.admin.repository.CommodityRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.Commodity}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CommodityResource {

    private final Logger log = LoggerFactory.getLogger(CommodityResource.class);

    private static final String ENTITY_NAME = "adminserviceCommodity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommodityRepository commodityRepository;

    public CommodityResource(CommodityRepository commodityRepository) {
        this.commodityRepository = commodityRepository;
    }

    /**
     * {@code POST  /commodities} : Create a new commodity.
     *
     * @param commodity the commodity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commodity, or with status {@code 400 (Bad Request)} if the commodity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/commodities")
    public ResponseEntity<Commodity> createCommodity(@RequestBody Commodity commodity) throws URISyntaxException {
        log.debug("REST request to save Commodity : {}", commodity);
        if (commodity.getId() != null) {
            throw new BadRequestAlertException("A new commodity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Commodity result = commodityRepository.save(commodity);
        return ResponseEntity
            .created(new URI("/api/commodities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /commodities/:id} : Updates an existing commodity.
     *
     * @param id the id of the commodity to save.
     * @param commodity the commodity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commodity,
     * or with status {@code 400 (Bad Request)} if the commodity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commodity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/commodities/{id}")
    public ResponseEntity<Commodity> updateCommodity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Commodity commodity
    ) throws URISyntaxException {
        log.debug("REST request to update Commodity : {}, {}", id, commodity);
        if (commodity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commodity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commodityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Commodity result = commodityRepository.save(commodity);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commodity.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /commodities/:id} : Partial updates given fields of an existing commodity, field will ignore if it is null
     *
     * @param id the id of the commodity to save.
     * @param commodity the commodity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commodity,
     * or with status {@code 400 (Bad Request)} if the commodity is not valid,
     * or with status {@code 404 (Not Found)} if the commodity is not found,
     * or with status {@code 500 (Internal Server Error)} if the commodity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/commodities/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Commodity> partialUpdateCommodity(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Commodity commodity
    ) throws URISyntaxException {
        log.debug("REST request to partial update Commodity partially : {}, {}", id, commodity);
        if (commodity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, commodity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!commodityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Commodity> result = commodityRepository
            .findById(commodity.getId())
            .map(
                existingCommodity -> {
                    if (commodity.getNameEnglish() != null) {
                        existingCommodity.setNameEnglish(commodity.getNameEnglish());
                    }
                    if (commodity.getNameArabic() != null) {
                        existingCommodity.setNameArabic(commodity.getNameArabic());
                    }

                    return existingCommodity;
                }
            )
            .map(commodityRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, commodity.getId().toString())
        );
    }

    /**
     * {@code GET  /commodities} : get all the commodities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commodities in body.
     */
    @GetMapping("/commodities")
    public List<Commodity> getAllCommodities() {
        log.debug("REST request to get all Commodities");
        return commodityRepository.findAll();
    }

    /**
     * {@code GET  /commodities/:id} : get the "id" commodity.
     *
     * @param id the id of the commodity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commodity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/commodities/{id}")
    public ResponseEntity<Commodity> getCommodity(@PathVariable Long id) {
        log.debug("REST request to get Commodity : {}", id);
        Optional<Commodity> commodity = commodityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(commodity);
    }

    /**
     * {@code DELETE  /commodities/:id} : delete the "id" commodity.
     *
     * @param id the id of the commodity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/commodities/{id}")
    public ResponseEntity<Void> deleteCommodity(@PathVariable Long id) {
        log.debug("REST request to delete Commodity : {}", id);
        commodityRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
