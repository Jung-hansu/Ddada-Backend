package ssafy.ddada.domain.court.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ssafy.ddada.domain.court.entity.Court;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {

    // N+1 문제 방지를 위해 FETCH 조인 사용
    @Query("SELECT c FROM Court c LEFT JOIN FETCH c.matches")
    Page<Court> findAllCourts(Pageable pageable);

    @Query("""
        SELECT c
        FROM Court c LEFT JOIN FETCH c.matches
        WHERE c.name LIKE CONCAT('%', :keyword, '%') OR
              c.address LIKE CONCAT('%', :keyword, '%')
    """)
    Page<Court> findCourtsByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
