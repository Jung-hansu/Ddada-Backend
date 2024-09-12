package ssafy.ddada.domain.court.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ssafy.ddada.api.court.request.CourtCreateRequest;
import ssafy.ddada.api.court.response.CourtDetailResponse;
import ssafy.ddada.api.court.response.CourtSimpleResponse;
import ssafy.ddada.common.exception.CourtNotFoundException;
import ssafy.ddada.common.util.StringUtil;
import ssafy.ddada.domain.court.command.CourtSearchCommand;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.court.entity.Facility;
import ssafy.ddada.domain.court.repository.CourtRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourtServiceImpl implements CourtService {

    private final CourtRepository courtRepository;

    private boolean isEmptyFacilities(Long facilities) {
        return facilities == null || facilities == 0;
    }

    private boolean containsFacility(Long whole, Long part){
        return whole != null && (whole & part) == part;
    }

    @Override
    public Page<CourtSimpleResponse> getCourtsByKeyword(CourtSearchCommand courtSearchCommand) {
        Pageable pageable = PageRequest.of(courtSearchCommand.page(), courtSearchCommand.size());
        List<Court> courts;

        if (StringUtil.isEmpty(courtSearchCommand.keyword())) {
            courts = courtRepository.findAllCourts();
        } else {
            courts = courtRepository.findCourtsByKeyword(courtSearchCommand.keyword());
        }

        if (!isEmptyFacilities(courtSearchCommand.facilities())) {
            courts = courts.stream()
                    .filter(court -> containsFacility(court.getFacilities(), courtSearchCommand.facilities()))
                    .toList();
        }
        return new PageImpl<>(courts, pageable, courts.size())
                .map(CourtSimpleResponse::from);
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
                Facility.setToBits(request.facilities())// != null ? request.facilities() : new HashSet<>()
        );
        courtRepository.save(court);
    }
}

