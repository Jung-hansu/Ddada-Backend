package ssafy.ddada.common.exception.Exception.Security;

import ssafy.ddada.common.exception.Exception.BaseException;
import ssafy.ddada.common.exception.errorcode.SecurityErrorCode;

public class InvalidTokenException extends BaseException {

        public InvalidTokenException() {
            super(SecurityErrorCode.INVALID_TOKEN);
        }
}
