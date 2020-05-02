package men.doku.donation.service;

import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import men.doku.donation.domain.Transaction;

/**
 * Service Interface for managing {@link Transaction}.
 */
public interface TransactionService {

    /**
     * Save a transaction.
     *
     * @param transaction the entity to save.
     * @return the persisted entity.
     */
    Transaction save(Transaction transaction);

    /**
     * Get all the transactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Transaction> findAll(Pageable pageable);

    /**
     * Get the "id" transaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Transaction> findOne(Long id);

    /**
     * Get one transaction by Example.
     *
     * @param Example<S> the example of the entity.
     * @return the entity.
     */
    Optional<Transaction> findOne(Example<Transaction> donation);

    /**
     * Delete the "id" transaction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Initiate transaction
     * 
     * @param transaction the transaction entity
     * @return Transaction the transaction entity
     */
    Transaction pay(Transaction transaction);

}
