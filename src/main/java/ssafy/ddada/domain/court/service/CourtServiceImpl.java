package ssafy.ddada.domain.court.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ssafy.ddada.api.member.court.response.CourtDetailResponse;
import ssafy.ddada.api.member.court.response.CourtSimpleResponse;
import ssafy.ddada.common.exception.CourtNotFoundException;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.court.entity.Facility;
import ssafy.ddada.domain.court.repository.CourtRepository;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourtServiceImpl implements CourtService {

    private final CourtRepository courtRepository;

    @Override
    public Page<CourtSimpleResponse> getCourtsByKeyword(String keyword, Set<Facility> facilities, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Court> courtPage;

        if ((keyword == null || keyword.isEmpty()) && (facilities == null || facilities.isEmpty())) {
            return courtRepository.findAllCourtPreviews(pageable);
        } else {
            return courtRepository.findCourtPreviewsByKeywordAndFacilities(keyword, facilities, pageable);
        }
    }

    @Override
    public CourtDetailResponse getCourtById(Long courtId) {
        Court court = courtRepository.findById(courtId)
                .orElseThrow(CourtNotFoundException::new);
        return CourtDetailResponse.from(court);
    }

}
