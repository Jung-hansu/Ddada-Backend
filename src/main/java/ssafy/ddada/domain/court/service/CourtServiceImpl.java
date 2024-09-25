package ssafy.ddada.domain.court.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ssafy.ddada.api.court.response.CourtDetailResponse;
import ssafy.ddada.api.court.response.CourtSimpleResponse;
import ssafy.ddada.common.exception.gym.CourtNotFoundException;
import ssafy.ddada.common.util.S3Util;
import ssafy.ddada.domain.court.command.CourtSearchCommand;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.court.repository.CourtRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourtServiceImpl implements CourtService {

    private final CourtRepository courtRepository;
    private final S3Util s3Util;

    @Override
    public Page<CourtSimpleResponse> getFilteredCourts(CourtSearchCommand command) {
        return courtRepository
                .findCourtsByKeywordAndRegion(command.keyword(), command.regions(), command.pageable())
                .map(court -> {
                    String image = Objects.requireNonNull(court.getGym().getImage());
                    String presignedUrl = s3Util.getPresignedUrlFromS3(image);
                    court.getGym().setImage(presignedUrl);
                    return court;
                })
                .map(CourtSimpleResponse::from);
    }

    @Override
    public CourtDetailResponse getCourtById(Long courtId) {
        Court court = courtRepository.findCourtWithMatchesById(courtId)
                .orElseThrow(CourtNotFoundException::new);

        String presignedUrl = s3Util.getPresignedUrlFromS3(court.getGym().getImage());  // S3Util의 메서드 사용
        court.getGym().setImage(presignedUrl);
        return CourtDetailResponse.from(court);
    }

}

