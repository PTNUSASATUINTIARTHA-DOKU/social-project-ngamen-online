package men.doku.donation.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import men.doku.donation.config.Constants;
import men.doku.donation.domain.Organizer;
import men.doku.donation.domain.enumeration.IsActiveStatus;
import men.doku.donation.repository.OrganizerRepository;
import men.doku.donation.security.SecurityUtils;
import men.doku.donation.service.OrganizerService;
import men.doku.donation.service.UserService;

/**
 * Service Implementation for managing {@link Organizer}.
 */
@Service
@Transactional
public class OrganizerServiceImpl implements OrganizerService {

    private final Logger log = LoggerFactory.getLogger(OrganizerServiceImpl.class);

    private final OrganizerRepository organizerRepository;
    private final UserService userService;

    public OrganizerServiceImpl(OrganizerRepository organizerRepository, UserService userService) {
        this.organizerRepository = organizerRepository;
        this.userService = userService;
    }

    /**
     * Save a organizer.
     *
     * @param organizer the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Organizer save(Organizer organizer) {
        final String login = SecurityUtils.getCurrentUserLogin().get();
        log.debug("Request by {} to save Organizer : {}", login, organizer);
        if (!SecurityUtils.isCurrentUserInRole(Constants.ADMIN)) {
            if (organizer.getId() != null) {
                Organizer current = findOne(organizer.getId()).get();
                organizer.setUsers(current.getUsers());
                organizer.setMdr(current.getMdr());
                organizer.setSharing(current.getSharing());
            } else {
                organizer.getUsers().clear();
                organizer.setMdr(null);
                organizer.setSharing(null);
            }
            organizer.setStatus(IsActiveStatus.WAITING_APPROVAL);
        }
        organizer.getUsers().add(userService.getUserWithAuthorities().get());
        organizer.setLastUpdatedAt(Instant.now());
        organizer.setLastUpdatedBy(login);
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
        final String login = SecurityUtils.getCurrentUserLogin().get();
        log.debug("Request by {} to get all Organizers", login);
        if (SecurityUtils.isCurrentUserInRole(Constants.ADMIN)) {
            return organizerRepository.findAll(pageable);
        } else {
            List<Long> organizerIds = organizerRepository.findAllIdsOwnedWithEagerRealtionships(login);
            return organizerRepository.findAllOwned(organizerIds, pageable);
        }
    }

    /**
     * Get all the organizers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Organizer> findAllWithEagerRelationships(Pageable pageable) {
        final String login = SecurityUtils.getCurrentUserLogin().get();
        log.debug("Request by {} to get all Organizers", login);
        if (SecurityUtils.isCurrentUserInRole(Constants.ADMIN)) {
            return organizerRepository.findAllWithEagerRelationships(pageable);
        } else {
            List<Long> organizerIds = organizerRepository.findAllIdsOwnedWithEagerRealtionships(login);
            List<Organizer> organizers = organizerRepository.findAllOwnedWithEagerRelationships(organizerIds);
            return new PageImpl<>(organizers, pageable, organizers.size());
        }
    }

    /**
     * Get all id of organizer owned by user login
     * 
     * @param login
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<Long> findAllIdsOwnedWithEagerRealtionships(String login) {
        return organizerRepository.findAllIdsOwnedWithEagerRealtionships(login);
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
        final String login = SecurityUtils.getCurrentUserLogin().get();
        log.debug("Request by {} to get Organizer : {}", login, id);
        return organizerRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the organizer by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.info("Request by {} to delete Organizer : {}", SecurityUtils.getCurrentUserLogin().get(), findOne(id).toString());
        organizerRepository.deleteById(id);
    }
}
