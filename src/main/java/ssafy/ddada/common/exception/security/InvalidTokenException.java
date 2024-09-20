package ssafy.ddada.common.exception.security;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.SecurityErrorCode;

public class InvalidTokenException extends BaseException {

        public InvalidTokenException() {
            super(SecurityErrorCode.INVALID_TOKEN);
        }
}
