package men.doku.donation.service;

import men.doku.donation.domain.Organizer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Organizer}.
 */
public interface OrganizerService {

    /**
     * Save a organizer.
     *
     * @param organizer the entity to save.
     * @return the persisted entity.
     */
    Organizer save(Organizer organizer);

    /**
     * Get all the organizers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Organizer> findAll(Pageable pageable);

    /**
     * Get the "id" organizer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Organizer> findOne(Long id);

    /**
     * Delete the "id" organizer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
