package ssafy.ddada.common.error.exception.security;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.SecurityErrorCode;

public class TokenExpiredException extends BaseException {
    public TokenExpiredException() {
        super(SecurityErrorCode.TOKEN_EXPIRED);
    }
}
