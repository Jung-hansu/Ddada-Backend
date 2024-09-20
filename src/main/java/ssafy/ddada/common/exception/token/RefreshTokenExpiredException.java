package ssafy.ddada.common.exception.token;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.SecurityErrorCode;

public class RefreshTokenExpiredException extends BaseException {
    public RefreshTokenExpiredException() {
        super(SecurityErrorCode.TOKEN_EXPIRED);
    }
}
