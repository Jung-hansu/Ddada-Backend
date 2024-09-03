package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.SecurityErrorCode;

public class ExpiredRefreshTokenException extends BaseException {
    public ExpiredRefreshTokenException() {
        super(SecurityErrorCode.EXPIRED_TOKEN);
    }
}
