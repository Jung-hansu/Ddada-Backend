package ssafy.ddada.common.exception.Exception.Validation;

import ssafy.ddada.common.exception.Exception.BaseException;
import ssafy.ddada.common.exception.errorcode.ValidationErrorCode;

public class MethodArgumentInvalidException extends BaseException {
    public MethodArgumentInvalidException() {
        super(ValidationErrorCode.METHOD_ARGUMENT_INVALID);
    }
}
