package com.axa.hypercell.admin.web.rest;

import com.axa.hypercell.admin.domain.Port;
import com.axa.hypercell.admin.repository.PortRepository;
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
 * REST controller for managing {@link com.axa.hypercell.admin.domain.Port}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PortResource {

    private final Logger log = LoggerFactory.getLogger(PortResource.class);

    private static final String ENTITY_NAME = "adminservicePort";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PortRepository portRepository;

    public PortResource(PortRepository portRepository) {
        this.portRepository = portRepository;
    }

    /**
     * {@code POST  /ports} : Create a new port.
     *
     * @param port the port to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new port, or with status {@code 400 (Bad Request)} if the port has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ports")
    public ResponseEntity<Port> createPort(@RequestBody Port port) throws URISyntaxException {
        log.debug("REST request to save Port : {}", port);
        if (port.getId() != null) {
            throw new BadRequestAlertException("A new port cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Port result = portRepository.save(port);
        return ResponseEntity
            .created(new URI("/api/ports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ports/:id} : Updates an existing port.
     *
     * @param id the id of the port to save.
     * @param port the port to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated port,
     * or with status {@code 400 (Bad Request)} if the port is not valid,
     * or with status {@code 500 (Internal Server Error)} if the port couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ports/{id}")
    public ResponseEntity<Port> updatePort(@PathVariable(value = "id", required = false) final Long id, @RequestBody Port port)
        throws URISyntaxException {
        log.debug("REST request to update Port : {}, {}", id, port);
        if (port.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, port.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!portRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Port result = portRepository.save(port);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, port.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ports/:id} : Partial updates given fields of an existing port, field will ignore if it is null
     *
     * @param id the id of the port to save.
     * @param port the port to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated port,
     * or with status {@code 400 (Bad Request)} if the port is not valid,
     * or with status {@code 404 (Not Found)} if the port is not found,
     * or with status {@code 500 (Internal Server Error)} if the port couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ports/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Port> partialUpdatePort(@PathVariable(value = "id", required = false) final Long id, @RequestBody Port port)
        throws URISyntaxException {
        log.debug("REST request to partial update Port partially : {}, {}", id, port);
        if (port.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, port.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!portRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Port> result = portRepository
            .findById(port.getId())
            .map(
                existingPort -> {
                    if (port.getCountryId() != null) {
                        existingPort.setCountryId(port.getCountryId());
                    }
                    if (port.getNameEnglish() != null) {
                        existingPort.setNameEnglish(port.getNameEnglish());
                    }
                    if (port.getNameArabic() != null) {
                        existingPort.setNameArabic(port.getNameArabic());
                    }

                    return existingPort;
                }
            )
            .map(portRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, port.getId().toString())
        );
    }

    /**
     * {@code GET  /ports} : get all the ports.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ports in body.
     */
    @GetMapping("/ports")
    public List<Port> getAllPorts() {
        log.debug("REST request to get all Ports");
        return portRepository.findAll();
    }

    /**
     * {@code GET  /ports/:id} : get the "id" port.
     *
     * @param id the id of the port to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the port, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ports/{id}")
    public ResponseEntity<Port> getPort(@PathVariable Long id) {
        log.debug("REST request to get Port : {}", id);
        Optional<Port> port = portRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(port);
    }

    /**
     * {@code DELETE  /ports/:id} : delete the "id" port.
     *
     * @param id the id of the port to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ports/{id}")
    public ResponseEntity<Void> deletePort(@PathVariable Long id) {
        log.debug("REST request to delete Port : {}", id);
        portRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
