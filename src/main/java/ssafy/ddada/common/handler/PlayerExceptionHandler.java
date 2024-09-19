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
public class PlayerExceptionHandler {
    @ExceptionHandler(AbnormalLoginProgressException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse handleAbnormalLoginProgressException(AbnormalLoginProgressException e) {
        log.error("AbnormalLoginProgressException Error", e);
        return CommonResponse.internalServerError(e.getErrorCode());
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResponse handleNotFoundMemberException(MemberNotFoundException e) {
        log.error("MemberNotFoundException Error", e);
        return CommonResponse.badRequest(e.getErrorCode());
    }

    @ExceptionHandler(TempPlayerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResponse handleNotFoundTempPlayer(TempPlayerNotFoundException e) {
        log.error("TempPlayerNotFoundException Error", e);
        return CommonResponse.badRequest(e.getErrorCode());
    }

    @ExceptionHandler(MemberAlreadyDeletedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public CommonResponse handleMemberAlreadyDeletedException(MemberAlreadyDeletedException e) {
        log.error("MemberAlreadyDeletedException Error", e);
        return CommonResponse.conflict(e.getErrorCode());
    }

    @ExceptionHandler(MessageSendingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse handleMessageSendingException(MessageSendingException e) {
        log.error("MessageSendingException Error", e);
        return CommonResponse.badRequest(e.getErrorCode());
    }

    @ExceptionHandler(VerificationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResponse handleSmsVerificationException(VerificationException e) {
        log.error("VerificationException Error", e);
        return CommonResponse.badRequest(e.getErrorCode());
    }

    @ExceptionHandler(EmailNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResponse handleEmailNotFoundException(EmailNotFoundException e) {
        log.error("EmailNotFoundException Error", e);
        return CommonResponse.badRequest(e.getErrorCode());
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResponse handlePasswordNotMatchException(PasswordNotMatchException e) {
        log.error("PasswordNotMatchException Error", e);
        return CommonResponse.badRequest(e.getErrorCode());
    }

    @ExceptionHandler(PhoneNumberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResponse handlePhoneNumberNotFoundException(PhoneNumberNotFoundException e) {
        log.error("PhoneNumberNotFoundException Error", e);
        return CommonResponse.badRequest(e.getErrorCode());
    }

    @ExceptionHandler(KakaoMailPlayerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResponse handleKakaoMailPlayerNotFoundException(KakaoMailPlayerNotFoundException e) {
        log.error("KakaoMailPlayerNotFoundException Error", e);
        return CommonResponse.badRequest(e.getErrorCode());
    }
}
