package ssafy.ddada.domain.court.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ssafy.ddada.domain.court.entity.Gym;
import ssafy.ddada.domain.match.entity.Match;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GymRepository extends JpaRepository<Gym, Long> {

    @EntityGraph(attributePaths = {"gymAdmin", "courts", "courts.matches"})
    @Query("""
        SELECT g
        FROM Gym g
        WHERE g.gymAdmin.id = :gymAdminId
    """)
    Optional<Gym> getGymsById(@Param("gymAdminId") Long gymAdminId);

    @EntityGraph(attributePaths = {"court", "court.gym", "manager", "team1", "team2"})
    @Query("""
        SELECT m
        FROM Match m
        WHERE m.court.gym.id = :gymAdminId AND m.matchDate = :date
    """)
    List<Match> getMatchesByGymIdAndDate(@Param("gymAdminId") Long gymAdminId, @Param("date") LocalDate date);

}
