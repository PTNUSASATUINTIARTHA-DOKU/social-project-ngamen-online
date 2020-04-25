package men.doku.donation.service.impl;

import men.doku.donation.service.TransactionService;
import men.doku.donation.service.dto.PaymentDTO;
import men.doku.donation.domain.Transaction;
import men.doku.donation.domain.enumeration.TransactionStatus;
import men.doku.donation.repository.TransactionRepository;
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
 * Service Implementation for managing {@link Transaction}.
 */
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Save a transaction.
     *
     * @param transaction the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Transaction save(Transaction transaction) {
        log.debug("Request to save Transaction : {}", transaction);
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
        log.debug("Request to get all Transactions");
        return transactionRepository.findAll(pageable);
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
        log.debug("Request to get Transaction : {}", id);
        return transactionRepository.findById(id);
    }

    /**
     * Delete the transaction by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.warn("Request to delete Transaction Forbidden", id);
    }

    /**
     * Initiate transaction
     * 
     * @param payment the payment DTO
     * @return Transaction the transaction entity
     */
    @Override
    public Transaction payment(PaymentDTO payment) {
        log.debug("Request to payment : {}", payment);
        Transaction transaction = payment.getTransaction();
        // Process to MIB here
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ie) {}
        transaction.setOvoIdMasked("ovoIdMasked");
        TransactionStatus status = TransactionStatus.SUCCESS;
        transaction.setStatus(status);
        return save(transaction);
    }

}
