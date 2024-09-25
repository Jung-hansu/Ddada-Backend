package ssafy.ddada.domain.court.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ssafy.ddada.domain.court.entity.Gym;

public interface GymRepository extends JpaRepository<Gym, Long> {

    @EntityGraph(attributePaths = {"gymAdmin", "courts", "courts.matches"})
    @Query("""
        SELECT g
        FROM Gym g
        WHERE g.gymAdmin.id = :gymAdminId
    """)
    Gym getGymsById(@Param("gymAdminId") Long gymAdminId);

}
