package men.doku.donation.web.rest;

import men.doku.donation.domain.Organizer;
import men.doku.donation.service.OrganizerService;
import men.doku.donation.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link men.doku.donation.domain.Organizer}.
 */
@RestController
@RequestMapping("/api")
public class OrganizerResource {

    private final Logger log = LoggerFactory.getLogger(OrganizerResource.class);

    private static final String ENTITY_NAME = "organizer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrganizerService organizerService;

    public OrganizerResource(OrganizerService organizerService) {
        this.organizerService = organizerService;
    }

    /**
     * {@code POST  /organizers} : Create a new organizer.
     *
     * @param organizer the organizer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new organizer, or with status {@code 400 (Bad Request)} if the organizer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/organizers")
    public ResponseEntity<Organizer> createOrganizer(@Valid @RequestBody Organizer organizer) throws URISyntaxException {
        log.debug("REST request to save Organizer : {}", organizer);
        if (organizer.getId() != null) {
            throw new BadRequestAlertException("A new organizer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Organizer result = organizerService.save(organizer);
        return ResponseEntity.created(new URI("/api/organizers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /organizers} : Updates an existing organizer.
     *
     * @param organizer the organizer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizer,
     * or with status {@code 400 (Bad Request)} if the organizer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organizer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/organizers")
    public ResponseEntity<Organizer> updateOrganizer(@Valid @RequestBody Organizer organizer) throws URISyntaxException {
        log.debug("REST request to update Organizer : {}", organizer);
        if (organizer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Organizer result = organizerService.save(organizer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organizer.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /organizers} : get all the organizers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organizers in body.
     */
    @GetMapping("/organizers")
    public ResponseEntity<List<Organizer>> getAllOrganizers(Pageable pageable) {
        log.debug("REST request to get a page of Organizers");
        Page<Organizer> page = organizerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /organizers/:id} : get the "id" organizer.
     *
     * @param id the id of the organizer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organizer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/organizers/{id}")
    public ResponseEntity<Organizer> getOrganizer(@PathVariable Long id) {
        log.debug("REST request to get Organizer : {}", id);
        Optional<Organizer> organizer = organizerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organizer);
    }

    /**
     * {@code DELETE  /organizers/:id} : delete the "id" organizer.
     *
     * @param id the id of the organizer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/organizers/{id}")
    public ResponseEntity<Void> deleteOrganizer(@PathVariable Long id) {
        log.debug("REST request to delete Organizer : {}", id);
        organizerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
