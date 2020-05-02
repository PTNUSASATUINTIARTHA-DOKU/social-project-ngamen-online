package men.doku.donation.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("select count(d) from Donation d where d.id = :id and d.organizer.id in :organizerIds")
    public Integer checkDonationAuthority(@Param("id") Long id, @Param("organizerIds") List<Long> organizerIds);

    @Query("select d from Donation d where d.organizer.id in :organizerIds")
    public Page<Donation> findAllByOrganizerIds(@Param("organizerIds") List<Long> organizerIds, Pageable pageable);
}
