package ssafy.ddada.domain.racket.service;

import ssafy.ddada.api.racket.response.RacketSearchResponse;

public interface RacketService {

    // Elasticsearch
    RacketSearchResponse getRacketsByElasticKeyword(String keyword);
    void indexAllRackets();

}
