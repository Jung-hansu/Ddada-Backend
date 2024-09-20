package ssafy.ddada.common.error.exception.security;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.SecurityErrorCode;

public class InvalidTokenException extends BaseException {

        public InvalidTokenException() {
            super(SecurityErrorCode.INVALID_TOKEN);
        }
}
