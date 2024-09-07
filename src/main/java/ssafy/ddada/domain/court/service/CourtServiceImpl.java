package ssafy.ddada.domain.court.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ssafy.ddada.api.court.response.CourtSearchResponse;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.court.entity.SearchedCourt;
import ssafy.ddada.domain.court.repository.CourtRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourtServiceImpl implements CourtService {

    private final CourtRepository courtRepository;

    @Override
    public CourtSearchResponse getCourts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SearchedCourt> courts = courtRepository
                .findByNameContainingIgnoreCaseOrAddressContainingIgnoreCase(keyword, pageable)
                .map(SearchedCourt::from);
        return CourtSearchResponse.of(courts);
    }

}
