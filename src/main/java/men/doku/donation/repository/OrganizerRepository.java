package men.doku.donation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import men.doku.donation.domain.Organizer;

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

    @Query("select distinct organizer.id from Organizer organizer join organizer.users user where user.login = :login")
    List<Long> findAllIdsOwnedWithEagerRealtionships(@Param("login") String login); 

    @Query(value = "select distinct organizer from Organizer organizer left join fetch organizer.users where organizer.id in :organizerIds")
    List<Organizer> findAllOwnedWithEagerRelationships(@Param("organizerIds") List<Long> organizerIds);

    @Query(value = "select organizer from Organizer organizer where organizer.id in :organizerIds",
    countQuery = "select count(organizer) from Organizer organizer where organizer.id in :organizerIds")
    Page<Organizer> findAllOwned(@Param("organizerIds") List<Long> organizerIds, Pageable pageable);

}
