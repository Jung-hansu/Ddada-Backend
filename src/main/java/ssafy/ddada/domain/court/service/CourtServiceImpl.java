package ssafy.ddada.domain.court.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ssafy.ddada.api.court.request.CourtCreateRequest;
import ssafy.ddada.api.court.response.CourtDetailResponse;
import ssafy.ddada.api.court.response.CourtSimpleResponse;
import ssafy.ddada.common.error.exception.court.CourtNotFoundException;
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
                    String image = Objects.requireNonNull(court.getImage());
                    String presignedUrl = s3Util.getPresignedUrlFromS3(image);
                    court.setImage(presignedUrl);
                    return court;
                })
                .map(CourtSimpleResponse::from);
    }

    @Override
    public CourtDetailResponse getCourtById(Long courtId) {
        Court court = courtRepository.findCourtWithMatchesById(courtId)
                .orElseThrow(CourtNotFoundException::new);

        String presignedUrl = s3Util.getPresignedUrlFromS3(court.getImage());  // S3Util의 메서드 사용
        court.setImage(presignedUrl);
        return CourtDetailResponse.from(court);
    }

    private boolean isImageEmpty(MultipartFile image) {
        return image == null || image.isEmpty();
    }

    public void createBadmintonCourt(CourtCreateRequest request) {
        Court court = courtRepository.save(
                Court.createCourt(
                        request.name(),
                        request.address(),
                        request.contactNumber(),
                        request.description(),
                        null,
                        request.url(),
                        request.region()
                )
        );

        if (!isImageEmpty(request.image())){
            String imageUrl = s3Util.uploadImageToS3(request.image(), court.getId(), "courtImg/");
            court.setImage(imageUrl);
        }
        courtRepository.save(court);
    }
}

