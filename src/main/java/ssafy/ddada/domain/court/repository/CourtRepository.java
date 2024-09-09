package ssafy.ddada.domain.court.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ssafy.ddada.api.court.response.CourtSimpleResponse;
import ssafy.ddada.domain.court.entity.Court;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {

    @Query("SELECT new ssafy.ddada.api.court.response.CourtSimpleResponse(c.id, c.name, c.address) " +
            "FROM Court c")
    Page<CourtSimpleResponse> findAllCourtPreviews(Pageable pageable);

    @Query("SELECT new ssafy.ddada.api.court.response.CourtSimpleResponse(c.id, c.name, c.address) " +
            "FROM Court c " +
            "WHERE c.name LIKE CONCAT('%', :keyword, '%') OR " +
            "      c.address LIKE CONCAT('%', :keyword, '%')")
    Page<CourtSimpleResponse> findCourtPreviewsByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
