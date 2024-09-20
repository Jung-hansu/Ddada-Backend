package ssafy.ddada.common.error.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static ssafy.ddada.api.StatusCode.*;

@Getter
@AllArgsConstructor
public enum ManagerErrorCode implements BaseErrorCode{

    MANAGER_NOT_FOUND(NOT_FOUND, "MANAGER_404_1", "존재하지 않는 매니저입니다.");

    private final Integer httpStatus;
    private final String code;
    private final String message;

}
