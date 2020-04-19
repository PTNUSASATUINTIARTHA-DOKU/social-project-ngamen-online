package men.doku.donation.repository;

import men.doku.donation.domain.Donation;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Donation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
}
