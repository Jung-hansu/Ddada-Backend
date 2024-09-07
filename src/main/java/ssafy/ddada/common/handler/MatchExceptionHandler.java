package ssafy.ddada.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.common.exception.MatchNotFoundException;
import ssafy.ddada.common.exception.TeamNotFoundException;

@Slf4j
@RestControllerAdvice
public class MatchExceptionHandler {

    @ExceptionHandler(MatchNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResponse<?> handleMatchNotFoundException(MatchNotFoundException e) {
        log.error("MatchNotFoundException occurs", e);
        return CommonResponse.notFound(e.getErrorCode());
    }

    @ExceptionHandler(TeamNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResponse<?> handleTeamNotFoundException(TeamNotFoundException e) {
        log.error("TeamNotFoundException occurs", e);
        return CommonResponse.notFound(e.getErrorCode());
    }


}
