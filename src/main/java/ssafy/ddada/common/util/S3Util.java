package ssafy.ddada.common.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import ssafy.ddada.common.exception.NotAllowedExtensionException;
import ssafy.ddada.common.exception.ProfileNotFoundInS3Exception;
import ssafy.ddada.common.properties.S3Properties;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class S3Util {

    private final AmazonS3 amazonS3Client;
    private final S3Properties s3Properties;
    private final S3Presigner s3Presigner;

    public S3Util(AmazonS3 amazonS3Client, S3Properties s3Properties, S3Presigner s3Presigner) {
        this.amazonS3Client = amazonS3Client;
        this.s3Properties = s3Properties;
        this.s3Presigner = s3Presigner; // 올바르게 주입
    }

    public String uploadImageToS3(MultipartFile image, Long memberId, String location) {
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

        String fileName = location + memberId + extension;

        try {
            amazonS3Client.putObject(new PutObjectRequest(s3Properties.s3().bucket(), fileName, image.getInputStream(), null));
            return amazonS3Client.getUrl(s3Properties.s3().bucket(), fileName).toString();
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드에 실패했습니다.", e);
        }
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

    private Date getExpirationTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 10);
        return cal.getTime();
    }
}
