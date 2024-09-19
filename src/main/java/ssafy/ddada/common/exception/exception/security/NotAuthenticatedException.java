package ssafy.ddada.common.exception.exception.security;

import ssafy.ddada.common.exception.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.SecurityErrorCode;

public class NotAuthenticatedException extends BaseException {
        public NotAuthenticatedException() {
            super(SecurityErrorCode.NOT_AUTHENTICATED_ERROR);
        }
}
