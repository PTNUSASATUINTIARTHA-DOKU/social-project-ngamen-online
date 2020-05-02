package men.doku.donation.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import men.doku.donation.domain.Transaction;

/**
 * Spring Data  repository for the Transaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("select t from Transaction t where t.donation.id in :donationIds and t.lastUpdatedBy = :login")
    public Page<Transaction> findByDonationIdsAndLastUpdatedBy(@Param("donationIds") List<Long> donationIds, @Param("login") String login, Pageable pageable);

    @Query("select t from Transaction t where t.donation.organizer.id in :organizerIds")
    public Page<Transaction> findAllByOrganizerIds(@Param("organizerIds") List<Long> organizerIds, Pageable pageable);
}
