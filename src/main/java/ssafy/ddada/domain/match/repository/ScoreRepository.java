package ssafy.ddada.domain.match.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssafy.ddada.domain.match.entity.Score;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
}
