package men.doku.donation.service.impl;

import men.doku.donation.service.OrganizerService;
import men.doku.donation.domain.Organizer;
import men.doku.donation.repository.OrganizerRepository;
import men.doku.donation.security.SecurityUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Organizer}.
 */
@Service
@Transactional
public class OrganizerServiceImpl implements OrganizerService {

    private final Logger log = LoggerFactory.getLogger(OrganizerServiceImpl.class);

    private final OrganizerRepository organizerRepository;

    public OrganizerServiceImpl(OrganizerRepository organizerRepository) {
        this.organizerRepository = organizerRepository;
    }

    /**
     * Save a organizer.
     *
     * @param organizer the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Organizer save(Organizer organizer) {
        organizer.setLastUpdatedAt(Instant.now());
        organizer.setLastUpdatedBy(SecurityUtils.getCurrentUserLogin().get());
        log.debug("Request to save Organizer : {}", organizer);
        return organizerRepository.save(organizer);
    }

    /**
     * Get all the organizers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Organizer> findAll(Pageable pageable) {
        log.debug("Request to get all Organizers");
        return organizerRepository.findAll(pageable);
    }

    /**
     * Get one organizer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Organizer> findOne(Long id) {
        log.debug("Request to get Organizer : {}", id);
        return organizerRepository.findById(id);
    }

    /**
     * Delete the organizer by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.info("Request to delete Organizer : {}", findOne(id).toString());
        organizerRepository.deleteById(id);
    }
}
