package men.doku.donation.repository;

import men.doku.donation.domain.Organizer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Organizer entity.
 */
@Repository
public interface OrganizerRepository extends JpaRepository<Organizer, Long> {

    @Query(value = "select distinct organizer from Organizer organizer left join fetch organizer.users",
        countQuery = "select count(distinct organizer) from Organizer organizer")
    Page<Organizer> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct organizer from Organizer organizer left join fetch organizer.users")
    List<Organizer> findAllWithEagerRelationships();

    @Query("select organizer from Organizer organizer left join fetch organizer.users where organizer.id =:id")
    Optional<Organizer> findOneWithEagerRelationships(@Param("id") Long id);
}
