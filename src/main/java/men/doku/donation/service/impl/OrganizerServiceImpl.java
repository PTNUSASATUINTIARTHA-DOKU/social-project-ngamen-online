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
import men.doku.donation.repository.OrganizerRepository;
import men.doku.donation.security.SecurityUtils;
import men.doku.donation.service.OrganizerService;
import men.doku.donation.service.UserService;
import men.doku.donation.web.rest.errors.NoAuthorityException;

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
        log.debug("Request by {} to save Organizer : {}", SecurityUtils.getCurrentUserLogin().get(), organizer);
        if (!SecurityUtils.isCurrentUserInRole(Constants.ADMIN)) {
            if (organizer.getId() != null) {
                organizer.setUsers(findOne(organizer.getId()).get().getUsers());;
            } else {
                organizer.getUsers().clear();
            }
        }
        organizer.getUsers().add(userService.getUserWithAuthorities().get());
        organizer.setLastUpdatedAt(Instant.now());
        organizer.setLastUpdatedBy(SecurityUtils.getCurrentUserLogin().get());
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
        if (SecurityUtils.isCurrentUserInRole(Constants.ADMIN)) {
            return organizerRepository.findAll(pageable);
        } else {
            List<Long> organizerIds = organizerRepository.findAllIdsOwnedWithEagerRealtionships(SecurityUtils.getCurrentUserLogin().get());
            return organizerRepository.findAllOwned(organizerIds, pageable);
        }
    }

    /**
     * Get all the organizers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Organizer> findAllWithEagerRelationships(Pageable pageable) {
        log.debug("Request to get all Organizers");
        if (SecurityUtils.isCurrentUserInRole(Constants.ADMIN)) {
            return organizerRepository.findAllWithEagerRelationships(pageable);
        } else {
            List<Long> organizerIds = organizerRepository.findAllIdsOwnedWithEagerRealtionships(SecurityUtils.getCurrentUserLogin().get());
            List<Organizer> organizers = organizerRepository.findAllOwnedWithEagerRelationships(organizerIds);
            return new PageImpl<>(organizers, pageable, organizerIds.size());
        }
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
        Optional<Organizer> result = organizerRepository.findOneWithEagerRelationships(id);
        log.debug("Users : [{}] ", result.get().getUsers());
        log.debug("Current User: {}", SecurityUtils.getCurrentUserLogin().get());
        log.debug("User filtered: {}", result.get().getUsers().stream().filter(
            user -> (user.getLogin().equalsIgnoreCase(SecurityUtils.getCurrentUserLogin().get()))).findFirst());
        if (!SecurityUtils.isCurrentUserInRole(Constants.ADMIN) && 
            !result.get().getUsers().stream().filter(
                user -> user.getLogin().equalsIgnoreCase(SecurityUtils.getCurrentUserLogin().get()))
            .findFirst().isPresent()) {
                throw new NoAuthorityException("Organizer", "findOne");
        } 
        return result;
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
