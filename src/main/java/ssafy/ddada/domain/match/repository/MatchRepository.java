package ssafy.ddada.domain.match.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ssafy.ddada.domain.match.entity.MatchType;
import ssafy.ddada.domain.match.entity.RankType;
import ssafy.ddada.domain.member.manager.entity.Manager;
import ssafy.ddada.domain.match.entity.Match;
import ssafy.ddada.domain.match.entity.MatchStatus;

import java.util.Optional;
import java.util.Set;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    @Query("""
        SELECT m
        FROM Match m JOIN FETCH m.team1 t JOIN FETCH m.team2
    """)
    Optional<Match> findByIdWithTeams(@Param("matchId") Long matchId);

    @Query("""
        SELECT m
        FROM Match m JOIN FETCH m.court c JOIN FETCH m.team1 JOIN FETCH m.team2
        WHERE (:keyword IS NULL OR c.name LIKE CONCAT('%', :keyword, '%') OR c.address LIKE CONCAT('%', :keyword, '%')) AND
            (:rankType IS NULL OR m.rankType = :rankType) AND
            (:matchTypes IS NULL OR m.matchType IN :matchTypes) AND
            (:statuses IS NULL OR m.status IN :statuses)
    """)
    Page<Match> findMatchesByKeywordAndTypeAndStatus(
            @Param("keyword") String keyword,
            @Param("rankType") RankType rankType,
            @Param("matchTypes") Set<MatchType> matchTypes,
            @Param("statuses") Set<MatchStatus> statuses
    );

    @Modifying
    @Query("UPDATE Match m SET m.status = :matchStatus WHERE m.id = :matchId")
    void setMatchStatus(@Param("matchId") Long matchId, @Param("matchStatus") MatchStatus matchStatus);

    @Modifying
    @Query("UPDATE Match m SET m.manager = :manager WHERE m.id = :matchId")
    void setManager(@Param("matchId") Long matchId, @Param("manager") Manager manager);

    @Query("SELECT m " +
            "FROM Match m JOIN FETCH m.court JOIN FETCH m.manager mg " +
            "WHERE mg.id = :managerId")
    Page<Match> findMatchesByManagerId(Long managerId, Pageable pageable);

}
