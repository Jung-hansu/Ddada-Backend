package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.SecurityErrorCode;

public class NotAuthenticatedException extends BaseException {
        public NotAuthenticatedException() {
            super(SecurityErrorCode.NOT_AUTHENTIATED_ERROR);
        }
}
