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

import men.doku.donation.domain.Donation;
import men.doku.donation.repository.DonationRepository;
import men.doku.donation.security.SecurityUtils;
import men.doku.donation.service.DonationService;

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
        donation.setLastUpdatedAt(Instant.now());
        donation.setLastUpdatedBy(SecurityUtils.getCurrentUserLogin().get());
        log.debug("Request to save Donation : {}", donation);
        return donationRepository.save(donation);
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
        log.debug("Request to get all Donations");
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
        log.debug("Request to get Donation : {}", id);
        return donationRepository.findById(id);
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
        return donationRepository.findOne(donation);
    }

    /**
     * Delete the donation by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.info("Request to delete Donation : {}", findOne(id).toString());
        donationRepository.deleteById(id);
    }
}
