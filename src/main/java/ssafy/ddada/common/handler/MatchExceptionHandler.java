package ssafy.ddada.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.common.exception.*;

@Slf4j
@RestControllerAdvice
public class MatchExceptionHandler {

    @ExceptionHandler(InvalidMatchStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<?> handleInvalidMatchStatusException(InvalidMatchStatusException e) {
        log.error("InvalidMatchStatusException occurs", e);
        return CommonResponse.badRequest(e.getErrorCode());
    }

    @ExceptionHandler(InvalidTeamNumberException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<?> handleInvalidTeamNumberException(InvalidTeamNumberException e) {
        log.error("InvalidTeamNumberException occurs", e);
        return CommonResponse.badRequest(e.getErrorCode());
    }

    @ExceptionHandler(TeamFullException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<?> handleInvalidTeamPlayerNumberException(TeamFullException e) {
        log.error("InvalidTeamPlayerNumberException occurs", e);
        return CommonResponse.badRequest(e.getErrorCode());
    }

    @ExceptionHandler(InvalidSetNumberException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<?> handleInvalidSetNumberException(TeamFullException e) {
        log.error("InvalidSetNumberException occurs", e);
        return CommonResponse.badRequest(e.getErrorCode());
    }

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

    @ExceptionHandler(TeamPlayerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResponse<?> handleTeamPlayerNotFoundException(TeamPlayerNotFoundException e) {
        log.error("TeamPlayerNotFoundException occurs", e);
        return CommonResponse.notFound(e.getErrorCode());
    }


}
