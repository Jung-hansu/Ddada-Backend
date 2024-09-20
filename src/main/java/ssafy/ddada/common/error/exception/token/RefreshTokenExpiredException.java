package ssafy.ddada.common.error.exception.token;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.SecurityErrorCode;

public class RefreshTokenExpiredException extends BaseException {
    public RefreshTokenExpiredException() {
        super(SecurityErrorCode.TOKEN_EXPIRED);
    }
}
