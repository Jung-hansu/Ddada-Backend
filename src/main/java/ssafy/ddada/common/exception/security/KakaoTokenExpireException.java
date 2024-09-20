package ssafy.ddada.common.exception.security;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.SecurityErrorCode;

public class KakaoTokenExpireException extends BaseException {
    public KakaoTokenExpireException() {
        super(SecurityErrorCode.KAKAO_TOKEN_EXPIRE);
    }
}
