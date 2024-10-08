package ssafy.ddada.domain.match.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ssafy.ddada.domain.gym.entity.Region;
import ssafy.ddada.domain.match.entity.MatchType;
import ssafy.ddada.domain.match.entity.RankType;
import ssafy.ddada.domain.match.entity.Match;
import ssafy.ddada.domain.match.entity.MatchStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    @EntityGraph(attributePaths = {"team1", "team2"})
    @Query("""
        SELECT m
        FROM Match m
        WHERE m.id = :matchId
    """)
    Optional<Match> findByIdWithTeams(@Param("matchId") Long matchId);

    @EntityGraph(attributePaths = {"court", "court.gym", "team1", "team2"})
    @Query("""
        SELECT m
        FROM Match m
        WHERE (:keyword IS NULL OR m.court.gym.name LIKE CONCAT('%', CAST(:keyword AS string), '%') OR m.court.gym.address LIKE CONCAT('%', CAST(:keyword AS string), '%')) AND
            (:rankType IS NULL OR m.rankType = :rankType) AND
            (:matchTypes IS NULL OR m.matchType IN :matchTypes) AND
            (:statuses IS NULL OR m.status IN :statuses) AND
            (:regions IS NULL OR m.court.gym.region IN :regions)
    """)
    Page<Match> findMatchesByKeywordAndTypeAndStatus(
            @Param("keyword") String keyword,
            @Param("rankType") RankType rankType,
            @Param("matchTypes") Set<MatchType> matchTypes,
            @Param("statuses") Set<MatchStatus> statuses,
            @Param("regions") Set<Region> regions,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"court", "court.gym", "team1", "team2", "manager"})
    @Query("""
        SELECT m
        FROM Match m
        WHERE (m.manager.id = :managerId) AND
            (:keyword IS NULL OR m.court.gym.name LIKE CONCAT('%', CAST(:keyword AS string), '%') OR m.court.gym.address LIKE CONCAT('%', CAST(:keyword AS string), '%')) AND
            (:todayOnly = FALSE OR m.matchDate = CURRENT_DATE) AND
            (:statuses IS NULL OR m.status IN :statuses)
    """)
    Page<Match> findFilteredMatches(
            @Param("managerId") Long managerId,
            @Param("keyword") String keyword,
            @Param("todayOnly") boolean todayOnly,
            @Param("statuses") Set<MatchStatus> statuses,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"court", "court.gym", "court.gym.gymAdmin", "manager", "team1", "team1.player1", "team1.player2", "team2", "team2.player1", "team2.player2", "sets"})
    @Query("""
        SELECT m
        FROM Match m
        WHERE m.id = :matchId
    """)
    Optional<Match> findByIdWithInfos(@Param("matchId") Long matchId);

    @Query("""
    SELECT m
    FROM Match m
    WHERE :playerId IN (m.team1.player1.id, m.team1.player2.id, m.team2.player1.id, m.team2.player2.id)
    ORDER BY m.matchDate DESC, m.matchTime DESC
    """)
    List<Match> findMatchesByPlayerId(Long playerId);



    @Query("""
        SELECT m
        FROM Match m
        WHERE m.status = 'COMPLETED' AND
            :playerId IN (m.team1.player1.id, m.team1.player2.id, m.team2.player1.id, m.team2.player2.id)
        ORDER BY m.matchDate DESC, m.matchTime DESC
    """)
    List<Match> findCompletedMatchesByPlayerId(Long playerId);

    @Query("""
        SELECT m
        FROM Match m
        WHERE (m.status = 'CREATED' OR m.status = 'RESERVED') AND
            m.matchDate < CURRENT_DATE
    """)
    List<Match> findAllOutDatedMatches();

    @EntityGraph(attributePaths = {"team1.player1", "team1.player2", "team2.player1", "team2.player2"})
    @Query("""
        SELECT COUNT(m)
        FROM Match m
        WHERE m.matchDate = :matchDate AND
            m.matchTime = :matchTime AND
            :playerId IN (m.team1.player1.id, m.team1.player2.id, m.team2.player1.id, m.team2.player2.id)
    """)
    int countByPlayerAndDateTime(@Param("playerId") Long playerId, @Param("matchDate") LocalDate matchDate, @Param("matchTime") LocalTime matchTime);

    @EntityGraph(attributePaths = {"manager"})
    @Query("""
        SELECT COUNT(m)
        FROM Match m
        WHERE m.manager.id = :managerId AND
            m.matchDate = :matchDate AND
            m.matchTime = :matchTime
    """)
    int countByManagerAndDateTime(@Param("managerId") Long managerId, @Param("matchDate") LocalDate matchDate, @Param("matchTime") LocalTime matchTime);

    @EntityGraph(attributePaths = {"court", "court.gym", "manager", "team1", "team2"})
    @Query("""
        SELECT m
        FROM Match m
        WHERE m.matchDate = :date AND
            m.court.gym.id = :gymAdminId
    """)
    List<Match> getMatchesByGymIdAndDate(@Param("gymAdminId") Long gymAdminId, @Param("date") LocalDate date);

    @EntityGraph(attributePaths = {"court", "court.gym"})
    @Query("""
        SELECT COUNT(m)
        FROM Match m
        WHERE m.status = "FINISHED" AND
            m.matchDate = :date AND
            m.court.gym.id = :gymId
    """)
    int countByGymIdAndMatchDate(@Param("gymId") Long gymId, @Param("matchDate") LocalDate date);

}
