package ssafy.ddada.domain.court.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.court.entity.Region;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {

    @EntityGraph(attributePaths = {"court", "matches"})
    @Query("""
        SELECT c
        FROM Court c
        WHERE (:keyword IS NULL OR c.gym.name LIKE CONCAT('%', CAST(:keyword AS string), '%') OR c.gym.address LIKE CONCAT('%', CAST(:keyword AS string), '%'))
            AND (:regions IS NULL OR c.gym.region IN :regions)
    """)
    Page<Court> findCourtsByKeywordAndRegion(
            @Param("keyword") String keyword,
            @Param("regions") Set<Region> regions,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"matches"})
    @Query("""
        SELECT c
        FROM Court c
        WHERE c.id = :courtId
    """)
    Optional<Court> findCourtWithMatchesById(@Param("courtId") Long courtId);

}
