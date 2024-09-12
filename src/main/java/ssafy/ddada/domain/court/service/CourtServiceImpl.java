package ssafy.ddada.domain.court.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import ssafy.ddada.api.court.request.CourtCreateRequest;
import ssafy.ddada.api.court.response.CourtDetailResponse;
import ssafy.ddada.api.court.response.CourtSimpleResponse;
import ssafy.ddada.common.exception.CourtNotFoundException;
import ssafy.ddada.common.exception.NotAllowedExtensionException;
import ssafy.ddada.common.properties.S3Properties;
import ssafy.ddada.common.util.StringUtil;
import ssafy.ddada.domain.court.command.CourtSearchCommand;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.court.entity.Facility;
import ssafy.ddada.domain.court.repository.CourtRepository;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourtServiceImpl implements CourtService {

    private final CourtRepository courtRepository;
    private final AmazonS3 amazonS3Client;
    private final S3Properties s3Properties;
    private final S3Presigner s3Presigner;

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

        List<CourtSimpleResponse> courtSimpleResponses = courts.stream()
                .map(court -> {
                    String presignedUrl = null;
                    if (court.getImage() != null) {
                        presignedUrl = getPresignedUrlFromS3(court.getImage());
                    }
                    return CourtSimpleResponse.from(court, presignedUrl);
                })
                .toList();

        return new PageImpl<>(courtSimpleResponses, pageable, courtSimpleResponses.size());
    }

    @Override
    public CourtDetailResponse getCourtById(Long courtId) {
        Court court = courtRepository.findById(courtId)
                .orElseThrow(CourtNotFoundException::new);

        String presignedUrl = getPresignedUrlFromS3(court.getImage());
        return CourtDetailResponse.from(court, presignedUrl);
    }

    public void createBadmintonCourt(CourtCreateRequest request) {
        Court court = new Court(
                null,
                request.name(),
                request.address(),
                request.contactNumber(),
                request.description(),
                null,
                new ArrayList<>(),
                Facility.setToBits(request.facilities())
        );

        courtRepository.save(court);

        String imageUrl = (request.image() == null || request.image().isEmpty())
                ? "https://ddada-image.s3.ap-northeast-2.amazonaws.com/profileImg/default.jpg"
                : uploadImageToS3(request.image(), court.getId());

        court.setImage(imageUrl);

        courtRepository.save(court);
    }

    public String getPresignedUrlFromS3(String imagePath) {
        try {
            String objectKey = imagePath.replace("https://ddada-image.s3.ap-northeast-2.amazonaws.com/", "");

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(s3Properties.s3().bucket())
                    .key(objectKey)
                    .build();

            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .getObjectRequest(getObjectRequest)
                    .signatureDuration(Duration.ofMinutes(10))
                    .build();

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(getObjectPresignRequest);
            URL presignedUrl = presignedRequest.url();

            log.info(objectKey + " 이미지에 대한 presigned URL 생성 성공");
            log.info("presigned URL: " + presignedUrl.toString());

            return presignedUrl.toString();
        } catch (Exception e) {
            log.error("Presigned URL 생성 중 오류 발생: " + e.getMessage(), e);
            throw new RuntimeException("Presigned URL 생성 실패", e);
        }
    }

    private String uploadImageToS3(MultipartFile image, Long courtId) {
        if (image == null || image.isEmpty()) {
            return null;
        }

        String originalFilename = image.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }

        List<String> allowedExtensions = List.of(".jpg", ".jpeg", ".png", ".gif", ".PNG", ".JPG", ".JPEG", ".GIF");

        if (!allowedExtensions.contains(extension)) {
            throw new NotAllowedExtensionException();
        }

        String fileName = "courtImg/" + courtId + extension;

        try {
            amazonS3Client.putObject(new PutObjectRequest(s3Properties.s3().bucket(), fileName, image.getInputStream(), null));
            return amazonS3Client.getUrl(s3Properties.s3().bucket(), fileName).toString();
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드에 실패했습니다.", e);
        }
    }
}

