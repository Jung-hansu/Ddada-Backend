package ssafy.ddada.common.exception.global;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.GlobalErrorCode;

public class AccessDeniedRequestException extends BaseException {
        public AccessDeniedRequestException() {
            super(GlobalErrorCode.ACCESS_DENIED_REQUEST);
        }
}
