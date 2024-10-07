package ssafy.ddada.common.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.common.exception.data.DataNotFoundException;
import ssafy.ddada.common.exception.s3.ProfileNotFoundInS3Exception;

@Slf4j
@RestControllerAdvice
public class DataExceptionHandler {
    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResponse handleDataNotFoundException(DataNotFoundException e) {
        log.error("PDataNotFoundException Error", e);
        return CommonResponse.notFound(e.getErrorCode());
    }
}
