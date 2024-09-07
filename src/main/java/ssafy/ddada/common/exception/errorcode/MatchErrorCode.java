package ssafy.ddada.common.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static ssafy.ddada.api.StatusCode.*;

@Getter
@AllArgsConstructor
public enum MatchErrorCode implements BaseErrorCode{

    MATCH_NOT_FOUND(NOT_FOUND, "MATCH_404_1", "존재하지 않는 경기입니다."),
    TEAM_NOT_FOUND(NOT_FOUND, "TEAM_404_1", "존재하지 않는 팀입니다.");

    private final Integer httpStatus;
    private final String code;
    private final String message;

}
