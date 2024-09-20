package ssafy.ddada.common.error.exception.validation;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.ValidationErrorCode;

public class MethodArgumentInvalidException extends BaseException {
    public MethodArgumentInvalidException() {
        super(ValidationErrorCode.METHOD_ARGUMENT_INVALID);
    }
}
