package men.doku.donation.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import men.doku.donation.config.Constants;
import men.doku.donation.domain.Transaction;
import men.doku.donation.repository.TransactionRepository;
import men.doku.donation.security.SecurityUtils;
import men.doku.donation.service.OrganizerService;
import men.doku.donation.service.TransactionService;

/**
 * Service Implementation for managing {@link Transaction}.
 */
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;
    private final OrganizerService organizerService;

    public TransactionServiceImpl(
        TransactionRepository transactionRepository,
        OrganizerService organizerService) {
        this.transactionRepository = transactionRepository;
        this.organizerService = organizerService;
    }

    /**
     * Save a transaction.
     *
     * @param transaction the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Transaction save(Transaction transaction) {
        log.debug("Request by {} to save Transaction : {}", SecurityUtils.getCurrentUserLogin().get(), transaction);
        transaction.setLastUpdatedAt(Instant.now());
        transaction.setLastUpdatedBy(SecurityUtils.getCurrentUserLogin().map(usr -> usr).orElse("SYSTEM"));
        return transactionRepository.save(transaction);
    }

    /**
     * Get all the transactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Transaction> findAll(Pageable pageable) {
        final String login = SecurityUtils.getCurrentUserLogin().get();
        log.debug("Request by {} to get all Transactions", login);
        if(SecurityUtils.isCurrentUserInRole(Constants.ADMIN)) {
            return transactionRepository.findAll(pageable);
        } else {
            List<Long> organizerIds = organizerService.findAllIdsOwnedWithEagerRealtionships(login);
            return transactionRepository.findAllByOrganizerIds(organizerIds, pageable);
        }
    }

    /**
     * Get one transaction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Transaction> findOne(Long id) {
        log.debug("Request by {} to get Transaction : {}", SecurityUtils.getCurrentUserLogin().get(), id);
        return transactionRepository.findById(id);
    }

    /**
     * Delete the transaction by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.warn("Request by {} to delete Transaction {} Forbidden", SecurityUtils.getCurrentUserLogin().get(), id);
    }
    
}
