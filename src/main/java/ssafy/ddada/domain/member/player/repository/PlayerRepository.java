package ssafy.ddada.domain.member.player.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ssafy.ddada.domain.match.entity.RatingChange;
import ssafy.ddada.domain.member.player.entity.Player;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    boolean existsByEmail(String email);
    Optional<Player> findByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<Player> findByNumber(String number);

    @Query("SELECT COUNT(m) FROM Match m WHERE m.team1.player1.id = ?1 AND m.team1SetScore > m.team2SetScore")
    Integer countWinsByPlayerId(Long playerId);

    @Query("SELECT COUNT(m) FROM Match m WHERE m.team1.player1.id = ?1 AND m.team1SetScore < m.team2SetScore")
    Integer countLossesByPlayerId(Long playerId);

    @Query("SELECT rc FROM RatingChange rc WHERE rc.player.id = :playerId AND rc.match.id = :matchId")
    RatingChange findFirstByPlayerIdAndMatchId(@Param("playerId") Long playerId, @Param("matchId") Long matchId);
}
