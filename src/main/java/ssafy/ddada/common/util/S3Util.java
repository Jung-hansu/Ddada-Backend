package ssafy.ddada.common.util;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ssafy.ddada.common.exception.NotAllowedExtensionException;
import ssafy.ddada.common.exception.ProfileNotFoundInS3Exception;
import ssafy.ddada.common.properties.S3Properties;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class S3Util {

    private final AmazonS3 amazonS3Client;
    private final S3Properties s3Properties;

    public S3Util(AmazonS3 amazonS3Client, S3Properties s3Properties) {
        this.amazonS3Client = amazonS3Client;
        this.s3Properties = s3Properties;
    }

    public String uploadImageToS3(MultipartFile image, Long memberId, String Location) {
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

        String fileName = Location + memberId + extension;

        try {
            amazonS3Client.putObject(new PutObjectRequest(s3Properties.s3().bucket(), fileName, image.getInputStream(), null));
            return amazonS3Client.getUrl(s3Properties.s3().bucket(), fileName).toString();
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드에 실패했습니다.", e);
        }
    }

    public String getPresignedUrlFromS3(String imagePath) {
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

    private Date getExpirationTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 10);
        return cal.getTime();
    }
}
