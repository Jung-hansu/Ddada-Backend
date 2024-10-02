package ssafy.ddada.domain.court.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import ssafy.ddada.domain.court.entity.CourtDocument;

@Repository
public interface CourtElasticsearchRepository extends ElasticsearchRepository<CourtDocument, String> {
}
