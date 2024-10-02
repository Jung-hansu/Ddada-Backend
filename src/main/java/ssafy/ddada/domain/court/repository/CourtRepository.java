package ssafy.ddada.domain.court.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ssafy.ddada.domain.court.entity.Court;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {

    @EntityGraph(attributePaths = {"matches"})
    @Query("""
        SELECT c
        FROM Court c
        WHERE c.id = :courtId
    """)
    Optional<Court> findCourtWithMatchesById(@Param("courtId") Long courtId);

    @EntityGraph(attributePaths = {"gym", "gym.gymAdmin", "matches"})
    @NotNull
    List<Court> findAll();

}
