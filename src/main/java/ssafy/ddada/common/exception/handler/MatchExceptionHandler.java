package ssafy.ddada.common.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.common.exception.match.*;
import ssafy.ddada.common.exception.match.InvalidMatchTypeException;
import ssafy.ddada.common.exception.match.InvalidRankTypeException;
import ssafy.ddada.common.exception.match.TeamFullException;
import ssafy.ddada.common.exception.match.TeamPlayerNotFoundException;

@Slf4j
@RestControllerAdvice
public class MatchExceptionHandler {

    @ExceptionHandler(InvalidRankTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<?> handleInvalidRankTypeException(InvalidRankTypeException e) {
        log.error("InvalidRankTypeException occurs", e);
        return CommonResponse.badRequest(e.getErrorCode());
    }

    @ExceptionHandler(InvalidMatchTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResponse<?> handleInvalidMatchTypeException(InvalidMatchTypeException e) {
        log.error("InvalidMatchTypeException occurs", e);
        return CommonResponse.badRequest(e.getErrorCode());
    }

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

    @ExceptionHandler(PlayerAlreadyBookedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public CommonResponse<?> handlePlayerAlreadyBookedException(PlayerAlreadyBookedException e) {
        log.error("PlayerAlreadyBookedException occurs", e);
        return CommonResponse.conflict(e.getErrorCode());
    }

    @ExceptionHandler(ManagerAlreadyBookedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public CommonResponse<?> handleManagerAlreadyBookedException(ManagerAlreadyBookedException e) {
        log.error("ManagerAlreadyBookedException occurs", e);
        return CommonResponse.conflict(e.getErrorCode());
    }
}
