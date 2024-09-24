package ssafy.ddada.common.exception.security;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.SecurityErrorCode;

public class TokenExpiredException extends BaseException {
    public TokenExpiredException() {
        super(SecurityErrorCode.TOKEN_EXPIRED);
    }
}
