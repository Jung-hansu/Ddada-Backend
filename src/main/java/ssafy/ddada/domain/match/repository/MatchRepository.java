package ssafy.ddada.domain.match.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ssafy.ddada.api.match.response.MatchSimpleResponse;
import ssafy.ddada.domain.member.manager.entity.Manager;
import ssafy.ddada.domain.match.entity.Match;
import ssafy.ddada.domain.match.entity.MatchStatus;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    @Query("SELECT new ssafy.ddada.api.match.response.MatchSimpleResponse(m.id, m.court.name, m.court.address) " +
            "FROM Match m")
    Page<MatchSimpleResponse> findAllMatches(Pageable pageable);

    @Query("SELECT new ssafy.ddada.api.match.response.MatchSimpleResponse(m.id, m.court.name, m.court.address) " +
            "FROM Match m " +
            "WHERE m.court.name LIKE CONCAT('%', :keyword, '%') OR " +
            "      m.court.address LIKE CONCAT('%', :keyword, '%')")
    Page<MatchSimpleResponse> findMatchesByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Modifying
    @Query("UPDATE Match m SET m.status = :matchStatus WHERE m.id = :matchId")
    void setMatchStatus(@Param("matchId") Long matchId, @Param("matchStatus") MatchStatus matchStatus);

    @Modifying
    @Query("UPDATE Match m SET m.manager = :manager WHERE m.id = :matchId")
    void setManager(@Param("matchId") Long matchId, @Param("manager") Manager manager);

    @Query("SELECT new ssafy.ddada.api.match.response.MatchSimpleResponse(m.id, m.court.name, m.court.address) " +
            "FROM Match m " +
            "WHERE m.manager.id = :managerId")
    Page<MatchSimpleResponse> findMatchesByManagerId(Long managerId, Pageable pageable);

}
