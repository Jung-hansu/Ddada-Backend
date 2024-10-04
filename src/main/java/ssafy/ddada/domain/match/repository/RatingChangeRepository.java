package ssafy.ddada.domain.match.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ssafy.ddada.domain.match.entity.RatingChange;

import java.util.Optional;

@Repository
public interface RatingChangeRepository extends JpaRepository<RatingChange, Long> {
    @EntityGraph(attributePaths = {"player", "match"})
    @Query("""
        SELECT rc
        FROM RatingChange rc
        WHERE rc.player.id = :playerId AND rc.match.id = :matchId
    """)
    Optional<RatingChange> findRatingChangeByMatchIdAndPlayerId(@Param("playerId") Long playerId, @Param("matchId") Long matchId);
}
