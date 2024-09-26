package ssafy.ddada.domain.racket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ssafy.ddada.api.racket.response.RacketDetailResponse;
import ssafy.ddada.api.racket.response.RacketSearchResponse;
import ssafy.ddada.domain.racket.entity.Racket;
import ssafy.ddada.domain.racket.repository.RacketRepository;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RacketServiceImpl implements RacketService {

    private final RacketRepository racketRepository;

    @Override
    public RacketSearchResponse getRacketsByKeyword(String keyword) {
        List<RacketDetailResponse> rackets = racketRepository
                .findByNameOrManufacturer(keyword, keyword)
                .stream()
                .map(RacketDetailResponse::from)
                .toList();

        return RacketSearchResponse.of(rackets.size(), rackets);
    }

}
