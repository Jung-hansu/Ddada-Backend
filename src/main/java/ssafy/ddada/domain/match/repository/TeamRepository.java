package ssafy.ddada.domain.match.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ssafy.ddada.domain.match.entity.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    @Modifying
    @Query("UPDATE Team t SET t.player1.id = :player1Id WHERE t.id = :teamId")
    void updatePlayer1(@Param("teamId") Long teamId, @Param("player1Id") Long player1Id);

    @Modifying
    @Query("UPDATE Team t SET t.player2.id = :player2Id WHERE t.id = :teamId")
    void updatePlayer2(@Param("teamId") Long teamId, @Param("player2Id") Long player2Id);

}
