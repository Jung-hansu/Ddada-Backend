package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.SecurityErrorCode;

public class TokenExpiredException extends BaseException {
    public TokenExpiredException() {
        super(SecurityErrorCode.EXPIRED_TOKEN);
    }
}
