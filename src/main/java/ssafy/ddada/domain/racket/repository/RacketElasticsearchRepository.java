package ssafy.ddada.domain.racket.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import ssafy.ddada.domain.racket.entity.RacketDocument;

@Repository
public interface RacketElasticsearchRepository extends ElasticsearchRepository<RacketDocument, String> {
}
