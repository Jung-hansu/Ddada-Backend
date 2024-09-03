package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.SecurityErrorCode;

public class ExpiredTokenException extends BaseException {
    public ExpiredTokenException() {
        super(SecurityErrorCode.EXPIRED_TOKEN);
    }
}
