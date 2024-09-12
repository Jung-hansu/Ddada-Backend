package ssafy.ddada.domain.court.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ssafy.ddada.api.court.response.CourtSimpleResponse;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.court.entity.Facility;

import java.util.Set;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {

    // N+1 문제 방지를 위해 FETCH 조인 사용
    @Query("SELECT c FROM Court c LEFT OUTER JOIN FETCH c.matches")
    Page<Court> findAllCourts(Pageable pageable);

    @Query(value = """
        SELECT c
        FROM Court c LEFT OUTER JOIN Match m
        WHERE c.name LIKE CONCAT('%', :keyword, '%') AND 
        (c.facilities & :facilities) > 0
    """, nativeQuery = true)
    Page<Court> findCourtsByKeywordAndFacilities(@Param("keyword") String keyword, @Param("facilities") Long facilities, Pageable pageable);
}
//EXISTS (SELECT f FROM Court court JOIN court.facilities f WHERE f IN :facilities))