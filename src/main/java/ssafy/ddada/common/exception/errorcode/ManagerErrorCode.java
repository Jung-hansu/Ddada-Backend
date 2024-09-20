package ssafy.ddada.common.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static ssafy.ddada.api.StatusCode.*;

@Getter
@AllArgsConstructor
public enum ManagerErrorCode implements BaseErrorCode{

    UNAUTHORIZED_MANAGER(UNAUTHORIZED, "MANAGER_401_1", "권한이 없습니다."),

    MANAGER_NOT_FOUND(NOT_FOUND, "MANAGER_404_1", "존재하지 않는 매니저입니다.");

    private final Integer httpStatus;
    private final String code;
    private final String message;

}
