package ssafy.ddada.common.exception.exception.security;

import ssafy.ddada.common.exception.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.SecurityErrorCode;

public class KakaoTokenExpireException extends BaseException {
    public KakaoTokenExpireException() {
        super(SecurityErrorCode.KAKAO_TOKEN_EXPIRE);
    }
}
