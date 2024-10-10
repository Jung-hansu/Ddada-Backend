package ssafy.ddada.domain.racket.service;

import ssafy.ddada.api.racket.response.RacketSearchResponse;

public interface RacketService {

    RacketSearchResponse getRacketsByElasticKeyword(String keyword);

}
