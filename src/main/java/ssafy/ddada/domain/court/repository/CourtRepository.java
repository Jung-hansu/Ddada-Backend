package ssafy.ddada.domain.court.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.court.entity.Region;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {

    @Query("""
        SELECT c
        FROM Court c LEFT OUTER JOIN FETCH c.matches
        WHERE (:keyword IS NULL OR :keyword = '' OR c.name LIKE CONCAT('%', :keyword, '%') OR c.address LIKE CONCAT('%', :keyword, '%'))
            AND (:regions IS NULL OR c.region IN :regions)
    """)
    List<Court> findCourtsByKeywordAndRegion(@Param("keyword") String keyword, @Param("regions") Set<Region> regions);

    @Query("""
        SELECT c
        FROM Court c LEFT JOIN FETCH c.matches
        WHERE c.id = :courtId
    """)
    Optional<Court> findCourtWithMatchesById(@Param("courtId") Long courtId);

}
