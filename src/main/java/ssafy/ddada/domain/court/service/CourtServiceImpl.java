package ssafy.ddada.domain.court.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ssafy.ddada.api.court.request.CourtCreateRequest;
import ssafy.ddada.api.court.response.CourtDetailResponse;
import ssafy.ddada.api.court.response.CourtSimpleResponse;
import ssafy.ddada.common.exception.CourtNotFoundException;
import ssafy.ddada.common.exception.NotAllowedExtensionException;
import ssafy.ddada.common.exception.ProfileNotFoundInS3Exception;
import ssafy.ddada.common.properties.S3Properties;
import ssafy.ddada.common.util.StringUtil;
import ssafy.ddada.domain.court.command.CourtSearchCommand;
import ssafy.ddada.domain.court.entity.Court;
import ssafy.ddada.domain.court.entity.Facility;
import ssafy.ddada.domain.court.repository.CourtRepository;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourtServiceImpl implements CourtService {

    private final CourtRepository courtRepository;
    private final AmazonS3 amazonS3Client;
    private final S3Properties s3Properties;

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

        String imageUrl = (request.imageUrl() == null || request.imageUrl().isEmpty())
                ? "https://ddada-image.s3.ap-northeast-2.amazonaws.com/profileImg/default.jpg"
                : uploadImageToS3(request.imageUrl(), court.getId());

        court.setImage(imageUrl);

        courtRepository.save(court);
    }

    private String getPresignedUrlFromS3(String imagePath) {
        try {
            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(s3Properties.s3().bucket(), imagePath)
                    .withMethod(HttpMethod.GET)
                    .withExpiration(getExpirationTime());

            URL presignedUrl = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
            log.info(imagePath + " 이미지에 대한 presigned URL 생성 성공");

            return presignedUrl.toString();
        } catch (AmazonServiceException e) {
            if (e.getStatusCode() == 404) {
                return null;
            } else {
                throw new ProfileNotFoundInS3Exception();
            }
        }
    }

    // presigned URL 만료 시간을 지정하는 메서드
    private Date getExpirationTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 10); // 10분 후 만료되는 URL 설정
        return cal.getTime();
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

