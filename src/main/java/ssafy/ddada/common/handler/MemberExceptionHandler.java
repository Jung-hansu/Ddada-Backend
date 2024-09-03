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
public class MemberExceptionHandler {
    @ExceptionHandler(AbnormalLoginProgressException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse handleAbnormalLoginProgressException(AbnormalLoginProgressException e) {
        log.error("AbnormalLoginProgressException Error", e);
        return CommonResponse.internalServerError(e.getErrorCode());
    }

    @ExceptionHandler(NotFoundMemberException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResponse handleNotFoundMemberException(NotFoundMemberException e) {
        log.error("NotFoundMemberException Error", e);
        return CommonResponse.badRequest(e.getErrorCode());
    }

    @ExceptionHandler(NotFoundTempMember.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResponse handleNotFoundTempMember(NotFoundTempMember e) {
        log.error("NotFoundTempMember Error", e);
        return CommonResponse.badRequest(e.getErrorCode());
    }

    @ExceptionHandler(MemberAlreadyDeletedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public CommonResponse handleMemberAlreadyDeletedException(MemberAlreadyDeletedException e) {
        log.error("MemberAlreadyDeletedException Error", e);
        return CommonResponse.conflict(e.getErrorCode());
    }

    @ExceptionHandler(NotAllowedExtensionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse handleNotAllowedExtension(NotAllowedExtensionException e) {
        log.error("NotAllowedExtension Error", e);
        return CommonResponse.badRequest(e.getErrorCode());
    }

    @ExceptionHandler(ImageTooLargeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse handleImageTooLargeException(ImageTooLargeException e) {
        log.error("ImageTooLargeException Error", e);
        return CommonResponse.badRequest(e.getErrorCode());
    }
}
