package ssafy.ddada.domain.racket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssafy.ddada.domain.racket.entity.Racket;

import java.util.List;

@Repository
public interface RacketRepository extends JpaRepository<Racket, Long> {

    List<Racket> findByNameOrManufacturer(String name, String manufacturer);

}
