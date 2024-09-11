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
    @Query("SELECT c FROM Court c LEFT JOIN FETCH c.matches")
    Page<Court> findAllCourts(Pageable pageable);

    @Query("""
        SELECT c
        FROM Court c LEFT JOIN FETCH c.matches
        WHERE c.name LIKE CONCAT('%', :keyword, '%') OR
              c.address LIKE CONCAT('%', :keyword, '%')
    """)
    Page<Court> findCourtsByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT new ssafy.ddada.api.court.response.CourtSimpleResponse(c.id, c.name, c.address) " +
            "FROM Court c " +
            "WHERE (c.name LIKE CONCAT('%', :keyword, '%') OR " +
            "      c.address LIKE CONCAT('%', :keyword, '%')) AND " +
            "      (:facilities IS NULL OR :facilities MEMBER OF c.facilities)")
    Page<CourtSimpleResponse> findCourtPreviewsByKeywordAndFacilities(@Param("keyword") String keyword, @Param("facilities") Set<Facility> facilities, Pageable pageable);
}
