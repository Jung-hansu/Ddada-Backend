package ssafy.ddada.common.error.exception.security;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.SecurityErrorCode;

public class KakaoTokenExpireException extends BaseException {
    public KakaoTokenExpireException() {
        super(SecurityErrorCode.KAKAO_TOKEN_EXPIRE);
    }
}
