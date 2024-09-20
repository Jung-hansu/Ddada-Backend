package ssafy.ddada.common.error.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static ssafy.ddada.api.StatusCode.*;

@Getter
@AllArgsConstructor
public enum CourtErrorCode implements BaseErrorCode{

    INVALID_REGION(BAD_REQUEST, "COURT_400_1", "잘못된 지역입니다."),

    COURT_NOT_FOUND(NOT_FOUND, "COURT_404_1", "존재하지 않는 시설입니다.");

    private final Integer httpStatus;
    private final String code;
    private final String message;

}
