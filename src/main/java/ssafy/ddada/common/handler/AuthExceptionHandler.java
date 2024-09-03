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
public class AuthExceptionHandler {
    @ExceptionHandler(IncorrectIssuerTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResponse handleIncorrectIssuerTokenException(IncorrectIssuerTokenException e) {
        log.error("IncorrectIssuerTokenException", e);
        return CommonResponse.unauthorized(e.getErrorCode());
    }

    @ExceptionHandler(ExpiredTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResponse handleExpiredTokenException(ExpiredTokenException e) {
        log.error("ExpiredTokenException", e);
        return CommonResponse.unauthorized(e.getErrorCode());
    }

    @ExceptionHandler(NotMatchedTokenTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse handleNotMatchedTokenTypeException(NotMatchedTokenTypeException e) {
        log.error("NotMatchedTokenTypeException", e);
        return CommonResponse.unauthorized(e.getErrorCode());
    }

    @ExceptionHandler(FilterErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse handleFilterErrorException(FilterErrorException e) {
        log.error("FilterErrorException", e);
        return CommonResponse.internalServerError(e.getErrorCode());
    }

    @ExceptionHandler(AbnormalLoginProgressException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse handleAbnormalLoginProgressException(AbnormalLoginProgressException e) {
        log.error("AbnormalLoginProgressException", e);
        return CommonResponse.internalServerError(e.getErrorCode());
    }

    @ExceptionHandler(KakaoTokenExpireException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResponse handleKakaoTokenExpireException(KakaoTokenExpireException e) {
        log.error("KakaoTokenExpireException", e);
        return CommonResponse.internalServerError(e.getErrorCode());
    }

}
