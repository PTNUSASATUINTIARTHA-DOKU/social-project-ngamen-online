package men.doku.donation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import men.doku.donation.domain.Donation;

/**
 * Spring Data  repository for the Donation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

    @Query("select d from Donation d where d.id = :id and d.lastUpdatedBy = :login")
    public Optional<Donation> findByIdAndLastUpdatedBy(@Param("id") Long id, @Param("login")String login);
}
