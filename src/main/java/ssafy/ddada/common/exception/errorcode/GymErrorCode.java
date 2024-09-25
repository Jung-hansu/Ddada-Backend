package ssafy.ddada.common.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static ssafy.ddada.api.StatusCode.*;

@Getter
@AllArgsConstructor
public enum GymErrorCode implements BaseErrorCode{

    INVALID_REGION(BAD_REQUEST, "GYM_400_1", "잘못된 지역입니다."),

    GYM_NOT_FOUND(NOT_FOUND, "GYM_404_1", "존재하지 않는 체육관입니다."),
    GYM_ADMIN_NOT_FOUND(NOT_FOUND, "GYM_ADMIN_404_1", "존재하지 않는 체육관 관리자입니다."),
    COURT_NOT_FOUND(NOT_FOUND, "COURT_404_1", "존재하지 않는 코트입니다.")
    ;

    private final Integer httpStatus;
    private final String code;
    private final String message;

}
