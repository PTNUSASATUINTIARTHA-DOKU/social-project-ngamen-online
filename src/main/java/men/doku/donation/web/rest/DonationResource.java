package men.doku.donation.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import men.doku.donation.domain.Donation;
import men.doku.donation.security.AuthoritiesConstants;
import men.doku.donation.security.SecurityUtils;
import men.doku.donation.service.DonationService;
import men.doku.donation.web.rest.errors.BadRequestAlertException;
import men.doku.donation.web.rest.errors.NoAuthorityException;

/**
 * REST controller for managing {@link men.doku.donation.domain.Donation}.
 */
@RestController
@RequestMapping("/api")
public class DonationResource {

    private final Logger log = LoggerFactory.getLogger(DonationResource.class);

    private static final String ENTITY_NAME = "donation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DonationService donationService;

    public DonationResource(DonationService donationService) {
        this.donationService = donationService;
    }

    /**
     * {@code POST  /donations} : Create a new donation.
     *
     * @param donation the donation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new donation, or with status {@code 400 (Bad Request)} if the donation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/donations")
    public ResponseEntity<Donation> createDonation(@Valid @RequestBody Donation donation) throws URISyntaxException {
        log.debug("REST request to save Donation : {}", donation);
        if (donation.getId() != null) {
            throw new BadRequestAlertException("A new donation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Donation result = donationService.save(donation);
        return ResponseEntity.created(new URI("/api/donations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /donations} : Updates an existing donation.
     *
     * @param donation the donation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated donation,
     * or with status {@code 400 (Bad Request)} if the donation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the donation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/donations")
    public ResponseEntity<Donation> updateDonation(@Valid @RequestBody Donation donation) throws URISyntaxException {
        log.debug("REST request to update Donation : {}", donation);
        if (donation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (donationService.checkDonationAuthority(donation.getId(), SecurityUtils.getCurrentUserLogin().get())) {
            throw new NoAuthorityException("Donation", "Edit");
        }
        Donation result = donationService.save(donation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, donation.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /donations} : get all the donations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of donations in body.
     */
    @GetMapping("/donations")
    public ResponseEntity<List<Donation>> getAllDonations(Pageable pageable) {
        log.debug("REST request to get a page of Donations");
        Page<Donation> page = donationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /donations/:id} : get the "id" donation.
     *
     * @param id the id of the donation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the donation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/donations/{id}")
    public ResponseEntity<Donation> getDonation(@PathVariable Long id) {
        log.debug("REST request to get Donation : {}", id);
        Optional<Donation> donation = donationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(donation);
    }

    /**
     * {@code DELETE  /donations/:id} : delete the "id" donation.
     *
     * @param id the id of the donation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/donations/{id}")
    @PreAuthorize("hasAuthority(\""+ AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteDonation(@PathVariable Long id) {
        log.debug("REST request to delete Donation : {}", id);
        donationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
