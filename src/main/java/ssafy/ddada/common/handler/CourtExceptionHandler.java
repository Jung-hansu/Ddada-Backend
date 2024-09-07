package ssafy.ddada.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.common.exception.CourtNotFoundException;

@Slf4j
@RestControllerAdvice
public class CourtExceptionHandler {

    @ExceptionHandler(CourtNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResponse<?> handleCourtNotFoundException(CourtNotFoundException e) {
        log.error("CourtNotFoundException occurs", e);
        return CommonResponse.notFound(e.getErrorCode());
    }

}
