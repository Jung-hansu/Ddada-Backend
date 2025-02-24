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

    Optional<Player> findByEmail(String email);

    @Query("""
        SELECT COUNT(p) > 0
        FROM Player p
        WHERE p.nickname = :nickname AND p.isDeleted = false
    """)
    boolean existsByNickname(@Param("nickname") String nickname);

    @Query("""
        SELECT p
        FROM Player p
        WHERE p.email = :email AND p.isDeleted = false
    """)
    Optional<Player> findNotDeletedPlayerByEmail(@Param("email") String email);

    @Query("""
        SELECT COUNT(m)
        FROM Match m
        WHERE (m.winnerTeamNumber = 1 AND :playerId IN (m.team1.player1.id , m.team1.player2.id)) OR
            (m.winnerTeamNumber = 2 AND :playerId IN (m.team2.player1.id, m.team2.player2.id))
    """)
    Integer countWinsByPlayerId(@Param("playerId") Long playerId);

    @Query("""
        SELECT COUNT(m)
        FROM Match m
        WHERE (m.winnerTeamNumber = 2 AND :playerId IN (m.team1.player1.id, m.team1.player2.id)) OR
            (m.winnerTeamNumber = 1 AND :playerId IN (m.team2.player1.id, m.team2.player2.id))
    """)
    Integer countLossesByPlayerId(@Param("playerId") Long playerId);

    @Query("""
        SELECT rc
        FROM RatingChange rc
        WHERE rc.player.id = :playerId AND
            rc.match.id = :matchId
    """)
    RatingChange findFirstByPlayerIdAndMatchId(@Param("playerId") Long playerId, @Param("matchId") Long matchId);

    @Query("""
        SELECT COUNT(rc)
        FROM RatingChange rc
        WHERE rc.player.id = :playerId
    """)
    Integer countMatchesByPlayerId(@Param("playerId") Long playerId);

    @Query("""
        SELECT p
        FROM Player p
        WHERE p.isDeleted = true AND p.updatedAt < CURRENT_DATE - 1 MONTH
    """)
    List<Player> findDeletedPlayers();

    @Query("SELECT p.gameCount FROM Player p WHERE p.nickname = :nickname")
    Optional<Integer> findGameCountByNickname(String nickname);
}
