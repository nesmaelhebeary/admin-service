package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.LineType;
import com.axa.hypercell.admin.repository.LineTypeRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.LineType}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LineTypeResource {

    private final Logger log = LoggerFactory.getLogger(LineTypeResource.class);

    private static final String ENTITY_NAME = "adminserviceLineType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LineTypeRepository lineTypeRepository;

    public LineTypeResource(LineTypeRepository lineTypeRepository) {
        this.lineTypeRepository = lineTypeRepository;
    }

    /**
     * {@code POST  /line-types} : Create a new lineType.
     *
     * @param lineType the lineType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lineType, or with status {@code 400 (Bad Request)} if the lineType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/line-types")
    public ResponseEntity<LineType> createLineType(@RequestBody LineType lineType) throws URISyntaxException {
        log.debug("REST request to save LineType : {}", lineType);
        if (lineType.getId() != null) {
            throw new BadRequestAlertException("A new lineType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LineType result = lineTypeRepository.save(lineType);
        return ResponseEntity
            .created(new URI("/api/line-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /line-types/:id} : Updates an existing lineType.
     *
     * @param id the id of the lineType to save.
     * @param lineType the lineType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lineType,
     * or with status {@code 400 (Bad Request)} if the lineType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lineType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/line-types/{id}")
    public ResponseEntity<LineType> updateLineType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LineType lineType
    ) throws URISyntaxException {
        log.debug("REST request to update LineType : {}, {}", id, lineType);
        if (lineType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lineType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lineTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LineType result = lineTypeRepository.save(lineType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lineType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /line-types/:id} : Partial updates given fields of an existing lineType, field will ignore if it is null
     *
     * @param id the id of the lineType to save.
     * @param lineType the lineType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lineType,
     * or with status {@code 400 (Bad Request)} if the lineType is not valid,
     * or with status {@code 404 (Not Found)} if the lineType is not found,
     * or with status {@code 500 (Internal Server Error)} if the lineType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/line-types/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LineType> partialUpdateLineType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LineType lineType
    ) throws URISyntaxException {
        log.debug("REST request to partial update LineType partially : {}, {}", id, lineType);
        if (lineType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lineType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lineTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LineType> result = lineTypeRepository
            .findById(lineType.getId())
            .map(
                existingLineType -> {
                    if (lineType.getNameEn() != null) {
                        existingLineType.setNameEn(lineType.getNameEn());
                    }
                    if (lineType.getNameAr() != null) {
                        existingLineType.setNameAr(lineType.getNameAr());
                    }
                    if (lineType.getDescription() != null) {
                        existingLineType.setDescription(lineType.getDescription());
                    }

                    return existingLineType;
                }
            )
            .map(lineTypeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lineType.getId().toString())
        );
    }

    /**
     * {@code GET  /line-types} : get all the lineTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lineTypes in body.
     */
    @GetMapping("/line-types")
    public List<LineType> getAllLineTypes() {
        log.debug("REST request to get all LineTypes");
        return lineTypeRepository.findAll();
    }

    /**
     * {@code GET  /line-types/:id} : get the "id" lineType.
     *
     * @param id the id of the lineType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lineType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/line-types/{id}")
    public ResponseEntity<LineType> getLineType(@PathVariable Long id) {
        log.debug("REST request to get LineType : {}", id);
        Optional<LineType> lineType = lineTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(lineType);
    }

    /**
     * {@code DELETE  /line-types/:id} : delete the "id" lineType.
     *
     * @param id the id of the lineType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/line-types/{id}")
    public ResponseEntity<Void> deleteLineType(@PathVariable Long id) {
        log.debug("REST request to delete LineType : {}", id);
        lineTypeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
