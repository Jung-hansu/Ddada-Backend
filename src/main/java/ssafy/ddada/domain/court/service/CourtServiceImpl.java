package ssafy.ddada.domain.court.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ssafy.ddada.api.court.request.CourtCreateRequest;
import ssafy.ddada.api.court.response.CourtDetailResponse;
import ssafy.ddada.api.court.response.CourtSimpleResponse;
import ssafy.ddada.common.exception.CourtNotFoundException;
import ssafy.ddada.domain.court.command.CourtSearchCommand;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.court.entity.Facility;
import ssafy.ddada.domain.court.repository.CourtRepository;

import java.util.ArrayList;
import java.util.HashSet;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourtServiceImpl implements CourtService {

    private final CourtRepository courtRepository;

    @Override
    public Page<CourtSimpleResponse> getCourtsByKeyword(CourtSearchCommand courtSearchCommand) {
        Pageable pageable = PageRequest.of(courtSearchCommand.page(), courtSearchCommand.size());
        Page<Court> courtPage;

        log.info("pageable: {}", pageable);
        if ((courtSearchCommand.keyword() == null || courtSearchCommand.keyword().trim().isEmpty()) &&
                (courtSearchCommand.facilities() == null || courtSearchCommand.facilities() == 0)) {
            courtPage = courtRepository.findAllCourts(pageable);
            log.info("courtPage: {}", courtPage.getContent());
        } else {
            courtPage = courtRepository.findCourtsByKeywordAndFacilities(
                    courtSearchCommand.keyword(),
                    courtSearchCommand.facilities(),
                    pageable
            );
        }
        return courtPage.map(CourtSimpleResponse::from);
    }

    @Override
    public CourtDetailResponse getCourtById(Long courtId) {
        Court court = courtRepository.findById(courtId)
                .orElseThrow(CourtNotFoundException::new);
        return CourtDetailResponse.from(court);
    }

    @Override
    public void createBadmintonCourt(CourtCreateRequest request) {
        Court court = new Court(
                null, // ID는 자동 생성
                request.name(),
                request.address(),
                request.contactNumber(),
                request.description(),
                request.imageUrl(),
                new ArrayList<>(),
                Facility.bitMask(request.facilities())// != null ? request.facilities() : new HashSet<>()
        );
        courtRepository.save(court);
    }
}

