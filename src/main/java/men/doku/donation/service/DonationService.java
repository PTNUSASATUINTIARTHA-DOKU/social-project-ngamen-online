package men.doku.donation.service;

import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import men.doku.donation.domain.Donation;
import men.doku.donation.domain.enumeration.IsActiveStatus;

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
     * Check donation authority when trying to update donation
     * 
     * @param donationId
     * @param login
     * @return
     */
    Boolean checkDonationAuthority(Long donationId, String login);

    /**
     * Update Donation Status based on Organizer Id and Organizer status
     * 
     * @param organizerId
     * @param organizerStatus
     */
    void updateStatusByOrganizerId(Long organizerId, IsActiveStatus organizerStatus);

    /**
     * Get all the donations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Donation> findAll(Pageable pageable);

    /**
     * Get the "id" donation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Donation> findOne(Long id);

    /**
     * Get one donation by Example.
     *
     * @param Example<S> the example of the entity.
     * @return the entity.
     */
    Optional<Donation> findOne(Example<Donation> donation);

    /**
     * Delete the "id" donation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void sendEmail(Long id);

}
