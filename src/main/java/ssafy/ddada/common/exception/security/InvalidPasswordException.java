package ssafy.ddada.common.exception.security;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.SecurityErrorCode;

public class InvalidPasswordException extends BaseException {
    public InvalidPasswordException() {
        super(SecurityErrorCode.INVALID_PASSWORD);
    }
}
