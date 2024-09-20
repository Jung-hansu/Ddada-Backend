package ssafy.ddada.common.error.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static ssafy.ddada.api.StatusCode.INTERNAL_SERVER_ERROR;
import static ssafy.ddada.api.StatusCode.UNAUTHORIZED;

@Getter
@AllArgsConstructor
public enum SecurityErrorCode implements BaseErrorCode {
    INVALID_TOKEN(UNAUTHORIZED, "TOKEN_401_1", "토큰이 유효하지않습니다."),
    INVALID_SIGNATURE_TOKEN(UNAUTHORIZED, "TOKEN_401_2", "토큰의 Signature가 일치하지 않습니다."),
    ISSUER_TOKEN_INCORRECT(UNAUTHORIZED, "TOKEN_401_3", "토큰 발급처가 일치하지 않습니다."),
    TOKEN_EXPIRED(UNAUTHORIZED, "TOKEN_401_4", "토큰이 만료되었습니다."),
    NOT_MATCHED_TOKEN_TYPE(UNAUTHORIZED, "TOKEN_401_5", "토큰의 타입이 일치하지 않아 디코딩할 수 없습니다."),
    NOT_AUTHENTICATED_ERROR(UNAUTHORIZED, "SECURITY_401_6", "사용자가 인증되지 않았습니다."),

    FILTER_ERROR(INTERNAL_SERVER_ERROR, "SECURITY_500_1", "인증 필터 처리 중 오류가 발생했습니다."),
    KAKAO_TOKEN_EXPIRE(UNAUTHORIZED, "SECURITY_500_2", "카카오 토큰을 만료하는 과정에서 오류가 발생했습니다."),
    ;

    private final Integer httpStatus;
    private final String code;
    private final String message;
}
