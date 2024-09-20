package ssafy.ddada.common.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ssafy.ddada.common.error.errorcode.BaseErrorCode;

@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException {
    private final BaseErrorCode errorCode;
}
