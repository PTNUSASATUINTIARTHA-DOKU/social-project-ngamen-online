package men.doku.donation.service.impl;

import java.time.Instant;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import men.doku.donation.config.Constants;
import men.doku.donation.domain.Donation;
import men.doku.donation.repository.DonationRepository;
import men.doku.donation.security.SecurityUtils;
import men.doku.donation.service.DonationService;
import men.doku.donation.web.rest.errors.NoAuthorityException;

/**
 * Service Implementation for managing {@link Donation}.
 */
@Service
@Transactional
public class DonationServiceImpl implements DonationService {

    private final Logger log = LoggerFactory.getLogger(DonationServiceImpl.class);

    private final DonationRepository donationRepository;

    public DonationServiceImpl(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    /**
     * Save a donation.
     *
     * @param donation the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Donation save(Donation donation) {
        final String currentUser = SecurityUtils.getCurrentUserLogin().get();
        log.debug("Request by {} to save Donation : {}", currentUser, donation);
        if (SecurityUtils.isCurrentUserInRole(Constants.ADMIN) || donation.getLastUpdatedBy() == currentUser) {
            donation.setLastUpdatedAt(Instant.now());
            donation.setLastUpdatedBy(currentUser);
            return donationRepository.save(donation);    
        } 
        throw new NoAuthorityException("Donation", "save");
    }

    /**
     * Get all the donations.
     *
     * @param donation the donation information.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Donation> findAll(Donation donation, Pageable pageable) {
        log.debug("Request by {} to get all Donations", SecurityUtils.getCurrentUserLogin().get());
        if(!SecurityUtils.isCurrentUserInRole(Constants.ADMIN)) donation.setLastUpdatedBy(SecurityUtils.getCurrentUserLogin().get());
        return donationRepository.findAll(Example.of(donation), pageable);
    }

    /**
     * Get one donation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Donation> findOne(Long id) {
        final String currentUser = SecurityUtils.getCurrentUserLogin().get();
        log.debug("Request by {} to get Donation : {}", currentUser, id);
        if(SecurityUtils.isCurrentUserInRole(Constants.ADMIN)) return donationRepository.findById(id);
        return donationRepository.findByIdAndLastUpdatedBy(id, currentUser);
    }

    /**
     * Get one donation by Example.
     *
     * @param Example<S> the example of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Donation> findOne(Example<Donation> donation) {
        final String currentUser = SecurityUtils.getCurrentUserLogin().get();
        log.debug("Request by {} to get Donation : {}", currentUser, donation);
        if(SecurityUtils.isCurrentUserInRole(Constants.ADMIN)) return donationRepository.findOne(donation);
        donation.getProbe().setLastUpdatedBy(currentUser);
        return donationRepository.findOne(donation);
    }

    /**
     * Delete the donation by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        Donation donation = findOne(id).get();
        log.info("Request by {} to delete Donation : {}", 
            SecurityUtils.getCurrentUserLogin().get(), donation);
        if (SecurityUtils.isCurrentUserInRole(Constants.ADMIN) || donation.getLastUpdatedBy() == SecurityUtils.getCurrentUserLogin().get()) {
            donationRepository.deleteById(id);
        } 
        throw new NoAuthorityException("Donation", "delete");
    }
}
