package ssafy.ddada.domain.court.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ssafy.ddada.domain.court.entity.Court;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {

    @Query("""
        SELECT c
        FROM Court c LEFT JOIN FETCH c.matches
        WHERE c.id = :courtId
    """)
    Optional<Court> findCourtWithMatchesById(@Param("courtId") Long courtId);

    // N+1 문제 방지를 위해 FETCH 조인 사용
    @Query("""
        SELECT c
        FROM Court c LEFT OUTER JOIN FETCH c.matches
    """)
    List<Court> findAllCourts();

    @Query("""
        SELECT c
        FROM Court c LEFT OUTER JOIN FETCH c.matches
        WHERE c.name LIKE CONCAT('%', :keyword, '%') OR
              c.address LIKE CONCAT('%', :keyword, '%')
    """)
    List<Court> findCourtsByKeyword(@Param("keyword") String keyword);
}
