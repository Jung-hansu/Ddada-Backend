package ssafy.ddada.common.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ssafy.ddada.api.CommonResponse;
import ssafy.ddada.common.exception.manager.ManagerNotFoundException;

@Slf4j
@RestControllerAdvice
public class ManagerExceptionHandler {

    @ExceptionHandler(ManagerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResponse<?> handleManagerNotFoundException(ManagerNotFoundException e) {
        log.error("ManagerNotFoundException occurs", e);
        return CommonResponse.notFound(e.getErrorCode());
    }

}
