package ssafy.ddada.domain.match.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ssafy.ddada.domain.member.manager.entity.Manager;
import ssafy.ddada.domain.match.entity.Match;
import ssafy.ddada.domain.match.entity.MatchStatus;

import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    @Query("""
        SELECT m
        FROM Match m JOIN FETCH m.team1 t JOIN FETCH m.team2
    """)
    Optional<Match> findByIdWithTeams(@Param("matchId") Long matchId);

    @Query("""
        SELECT m
        FROM Match m JOIN FETCH m.court c
    """)
    Page<Match> findAllMatches(Pageable pageable);

    @Query("""
        SELECT m
        FROM Match m JOIN FETCH m.court c
        WHERE c.name LIKE CONCAT('%', :keyword, '%') OR
              c.address LIKE CONCAT('%', :keyword, '%')
    """)
    Page<Match> findMatchesByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
        SELECT m
        FROM Match m JOIN FETCH m.court c
        WHERE m.status = :status
    """)
    Page<Match> findAllMatchesByStatus(MatchStatus status, Pageable pageable);

    @Query("""
        SELECT m
        FROM Match m JOIN FETCH m.court c
        WHERE m.status = :status AND
             (c.name LIKE CONCAT('%', :keyword, '%') OR c.address LIKE CONCAT('%', :keyword, '%'))
    """)
    Page<Match> findMatchesByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") MatchStatus status, Pageable pageable);

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
