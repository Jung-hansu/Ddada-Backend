package ssafy.ddada.common.exception.validation;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.ValidationErrorCode;

public class MethodArgumentInvalidException extends BaseException {
    public MethodArgumentInvalidException() {
        super(ValidationErrorCode.METHOD_ARGUMENT_INVALID);
    }
}
