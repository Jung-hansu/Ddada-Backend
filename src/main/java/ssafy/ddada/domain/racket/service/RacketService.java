package ssafy.ddada.domain.racket.service;

import ssafy.ddada.api.racket.response.RacketSearchResponse;

public interface RacketService {

    RacketSearchResponse getRacketsByKeyword(String keyword);

    // Elasticsearch
    RacketSearchResponse getRacketsByElasticKeyword(String keyword);
    void indexAllRackets();
}
