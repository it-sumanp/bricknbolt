package com.bricknbolt.web.rest;
import com.bricknbolt.domain.BoltUser;
import com.bricknbolt.repository.BoltUserRepository;
import com.bricknbolt.web.rest.errors.BadRequestAlertException;
import com.bricknbolt.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing BoltUser.
 */
@RestController
@RequestMapping("/api")
public class BoltUserResource {

    private final Logger log = LoggerFactory.getLogger(BoltUserResource.class);

    private static final String ENTITY_NAME = "boltUser";

    private final BoltUserRepository boltUserRepository;

    public BoltUserResource(BoltUserRepository boltUserRepository) {
        this.boltUserRepository = boltUserRepository;
    }

    /**
     * POST  /bolt-users : Create a new boltUser.
     *
     * @param boltUser the boltUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new boltUser, or with status 400 (Bad Request) if the boltUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bolt-users")
    public ResponseEntity<BoltUser> createBoltUser(@Valid @RequestBody BoltUser boltUser) throws URISyntaxException {
        log.debug("REST request to save BoltUser : {}", boltUser);
        if (boltUser.getId() != null) {
            throw new BadRequestAlertException("A new boltUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BoltUser result = boltUserRepository.save(boltUser);
        return ResponseEntity.created(new URI("/api/bolt-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bolt-users : Updates an existing boltUser.
     *
     * @param boltUser the boltUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated boltUser,
     * or with status 400 (Bad Request) if the boltUser is not valid,
     * or with status 500 (Internal Server Error) if the boltUser couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bolt-users")
    public ResponseEntity<BoltUser> updateBoltUser(@Valid @RequestBody BoltUser boltUser) throws URISyntaxException {
        log.debug("REST request to update BoltUser : {}", boltUser);
        if (boltUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BoltUser result = boltUserRepository.save(boltUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, boltUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bolt-users : get all the boltUsers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of boltUsers in body
     */
    @GetMapping("/bolt-users")
    public List<BoltUser> getAllBoltUsers() {
        log.debug("REST request to get all BoltUsers");
        return boltUserRepository.findAll();
    }

    /**
     * GET  /bolt-users/:id : get the "id" boltUser.
     *
     * @param id the id of the boltUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the boltUser, or with status 404 (Not Found)
     */
    @GetMapping("/bolt-users/{id}")
    public ResponseEntity<BoltUser> getBoltUser(@PathVariable Long id) {
        log.debug("REST request to get BoltUser : {}", id);
        Optional<BoltUser> boltUser = boltUserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(boltUser);
    }

    /**
     * DELETE  /bolt-users/:id : delete the "id" boltUser.
     *
     * @param id the id of the boltUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bolt-users/{id}")
    public ResponseEntity<Void> deleteBoltUser(@PathVariable Long id) {
        log.debug("REST request to delete BoltUser : {}", id);
        boltUserRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
