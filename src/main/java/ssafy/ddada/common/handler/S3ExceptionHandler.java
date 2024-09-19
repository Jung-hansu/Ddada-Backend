package ssafy.ddada.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.common.exception.*;

@RestControllerAdvice
@Slf4j
public class S3ExceptionHandler {

    @ExceptionHandler(NotAllowedExtensionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse handleNotAllowedExtension(NotAllowedExtensionException e) {
        log.error("NotAllowedExtension Error", e);
        return CommonResponse.badRequest(e.getErrorCode());
    }

    @ExceptionHandler(TooLargeImageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse handleImageTooLargeException(TooLargeImageException e) {
        log.error("TooLargeImageException Error", e);
        return CommonResponse.badRequest(e.getErrorCode());
    }

    @ExceptionHandler(ProfileNotFoundInS3Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResponse handleProfileNotFoundInS3Exception(ProfileNotFoundInS3Exception e) {
        log.error("ProfileNotFoundInS3Exception Error", e);
        return CommonResponse.notFound(e.getErrorCode());
    }

    @ExceptionHandler(S3UploadFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse handleImageUploadFailedException(S3UploadFailedException e) {
        log.error("S3UploadFailedException Error", e);
        return CommonResponse.badRequest(e.getErrorCode());
    }

    @ExceptionHandler(PresignedUrlGenerationFailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse handlePresignedUrlGenerationFailedException(PresignedUrlGenerationFailException e) {
        log.error("PresignedUrlGenerationFailException Error", e);
        return CommonResponse.badRequest(e.getErrorCode());
    }
}
