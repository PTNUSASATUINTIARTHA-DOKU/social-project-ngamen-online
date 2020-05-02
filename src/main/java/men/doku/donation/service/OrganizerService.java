package men.doku.donation.service;

import men.doku.donation.domain.Organizer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
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
     * Get all the organizers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<Organizer> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get all id of organizer owned by user login
     * 
     * @param login
     * @return
     */
    public List<Long> findAllIdsOwnedWithEagerRealtionships(String login);

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
