package ssafy.ddada.domain.racket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssafy.ddada.api.racket.response.RacketSimpleResponse;
import ssafy.ddada.api.racket.response.RacketSearchResponse;
import ssafy.ddada.domain.racket.entity.Racket;
import ssafy.ddada.domain.racket.entity.RacketDocument;
import ssafy.ddada.domain.racket.repository.RacketElasticsearchRepository;
import ssafy.ddada.domain.racket.repository.RacketRepository;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RacketServiceImpl implements RacketService {

    private final RacketRepository racketRepository;
    private final RacketElasticsearchRepository racketElasticsearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public RacketSearchResponse getRacketsByElasticKeyword(String keyword) {
        Criteria criteria = new Criteria("name").matches(keyword)
                .or("manufacturer").matches(keyword);
        CriteriaQuery query = new CriteriaQuery(criteria);
        List<RacketSimpleResponse> rackets = elasticsearchOperations.search(query, RacketDocument.class)
                .stream()
                .map(searchHit -> RacketSimpleResponse.from(searchHit.getContent()))
                .toList();

        return new RacketSearchResponse(rackets.size(), rackets);
    }

    @Override
    public void indexAllRackets() {
        List<Racket> rackets = racketRepository.findAll();
        for (Racket racket : rackets) {
            indexRacket(racket);
        }
    }

    private void indexRacket(Racket racket){
        RacketDocument racketDocument = RacketDocument.builder()
                .racketId(racket.getId())
                .name(racket.getName())
                .manufacturer(racket.getManufacturer())
                .weight(racket.getWeight())
                .material(racket.getMaterial())
                .image(racket.getImage())
                .build();

        racketElasticsearchRepository.save(racketDocument);
    }

}
