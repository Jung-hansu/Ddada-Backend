package ssafy.ddada.common.error.exception.global;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.GlobalErrorCode;

public class AccessDeniedRequestException extends BaseException {
        public AccessDeniedRequestException() {
            super(GlobalErrorCode.ACCESS_DENIED_REQUEST);
        }
}
