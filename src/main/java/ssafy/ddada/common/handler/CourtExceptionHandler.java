package ssafy.ddada.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.common.exception.exception.court.CourtNotFoundException;
import ssafy.ddada.common.exception.exception.court.InvalidRegionException;

@Slf4j
@RestControllerAdvice
public class CourtExceptionHandler {

    @ExceptionHandler(InvalidRegionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<?> handleInvalidRegionException(InvalidRegionException e) {
        log.error("InvalidRegionException occurs", e);
        return CommonResponse.badRequest(e.getErrorCode());
    }

    @ExceptionHandler(CourtNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResponse<?> handleCourtNotFoundException(CourtNotFoundException e) {
        log.error("CourtNotFoundException occurs", e);
        return CommonResponse.notFound(e.getErrorCode());
    }

}
