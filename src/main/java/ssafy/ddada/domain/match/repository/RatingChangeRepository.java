package ssafy.ddada.domain.match.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssafy.ddada.domain.match.entity.RatingChange;

@Repository
public interface RatingChangeRepository extends JpaRepository<RatingChange, Long> {
}
