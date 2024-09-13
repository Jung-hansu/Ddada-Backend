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
import ssafy.ddada.common.util.S3Util;
import ssafy.ddada.common.util.StringUtil;
import ssafy.ddada.domain.court.command.CourtSearchCommand;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.court.entity.Facility;
import ssafy.ddada.domain.court.repository.CourtRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourtServiceImpl implements CourtService {

    private final CourtRepository courtRepository;
    private final S3Util s3Util;  // S3Util 주입

    private boolean isEmptyFacilities(Long facilities) {
        return facilities == null || facilities == 0;
    }

    private boolean containsFacility(Long whole, Long part){
        return whole != null && (whole & part) == part;
    }

    @Override
    public Page<CourtSimpleResponse> getCourtsByKeyword(CourtSearchCommand command) {
        Pageable pageable = PageRequest.of(command.page(), command.size());
        List<Court> courts;

        if (StringUtil.isEmpty(command.keyword())) {
            courts = courtRepository.findAllCourts();
        } else {
            courts = courtRepository.findCourtsByKeyword(command.keyword());
        }

        if (!isEmptyFacilities(command.facilities())) {
            courts = courts.stream()
                    .filter(court -> containsFacility(court.getFacilities(), command.facilities()))
                    .toList();
        }

        List<CourtSimpleResponse> courtSimpleResponses = courts.stream()
                .map(court -> {
                    String presignedUrl = null;
                    if (court.getImage() != null) {
                        presignedUrl = s3Util.getPresignedUrlFromS3(court.getImage());  // S3Util의 메서드 사용
                    }
                    return CourtSimpleResponse.from(court, presignedUrl);
                })
                .toList();

        return new PageImpl<>(courtSimpleResponses, pageable, courtSimpleResponses.size());
    }

    @Override
    public CourtDetailResponse getCourtById(Long courtId) {
        Court court = courtRepository.findCourtWithMatchesById(courtId)
                .orElseThrow(CourtNotFoundException::new);

        String presignedUrl = s3Util.getPresignedUrlFromS3(court.getImage());  // S3Util의 메서드 사용
        return CourtDetailResponse.from(court, presignedUrl);
    }

    public void createBadmintonCourt(CourtCreateRequest request) {
        Court court = Court.createCourt(
                request.name(),
                request.address(),
                request.contactNumber(),
                request.description(),
                null,
                request.url(),
                Facility.setToBits(request.facilities())
        );

        courtRepository.save(court);

        // 이미지 업로드 로직 변경
        String imageUrl = (request.image() == null || request.image().isEmpty())
                ? "https://ddada-image.s3.ap-northeast-2.amazonaws.com/profileImg/default.jpg"
                : s3Util.uploadImageToS3(request.image(), court.getId(), "courtImg/");  // S3Util의 메서드 사용

        court.setImage(imageUrl);
        courtRepository.save(court);
    }
}

