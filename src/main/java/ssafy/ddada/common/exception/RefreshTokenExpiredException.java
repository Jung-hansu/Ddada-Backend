package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.SecurityErrorCode;

public class RefreshTokenExpiredException extends BaseException {
    public RefreshTokenExpiredException() {
        super(SecurityErrorCode.EXPIRED_TOKEN);
    }
}
