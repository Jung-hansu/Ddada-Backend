package ssafy.ddada.domain.gym.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ssafy.ddada.domain.gym.entity.Gym;
import ssafy.ddada.domain.gym.entity.Region;

import java.util.Optional;
import java.util.Set;

@Repository
public interface GymRepository extends JpaRepository<Gym, Long> {

    @EntityGraph(attributePaths = {"matches"})
    @Query("""
        SELECT c
        FROM Gym c
        WHERE (:keyword IS NULL OR c.name LIKE CONCAT('%', CAST(:keyword AS string), '%') OR c.address LIKE CONCAT('%', CAST(:keyword AS string), '%'))
            AND (:regions IS NULL OR c.region IN :regions)
    """)
    Page<Gym> findGymsByKeywordAndRegion(
            @Param("keyword") String keyword,
            @Param("regions") Set<Region> regions,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"matches"})
    @Query("""
        SELECT c
        FROM Gym c
        WHERE c.id = :gymId
    """)
    Optional<Gym> findGymWithMatchesById(@Param("gymId") Long gymId);

}
