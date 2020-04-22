package men.doku.donation.service;

import men.doku.donation.domain.Donation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Donation}.
 */
public interface DonationService {

    /**
     * Save a donation.
     *
     * @param donation the entity to save.
     * @return the persisted entity.
     */
    Donation save(Donation donation);

    /**
     * Get all the donations.
     *
     * @param pageable the pagination information.
     * @param donation the donation information
     * @return the list of entities.
     */
    public Page<Donation> findAll(Donation donation, Pageable pageable);

    /**
     * Get the "id" donation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Donation> findOne(Long id);

    /**
     * Delete the "id" donation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get one donation by slug.
     *
     * @param String the paymentSlug of the entity.
     * @return the entity.
     */
    Optional<Donation> findOneByPaymentSlug(String paymentSlug);
}
