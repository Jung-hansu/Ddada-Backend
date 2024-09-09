package ssafy.ddada.common.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static ssafy.ddada.api.StatusCode.*;

@Getter
@AllArgsConstructor
public enum MatchErrorCode implements BaseErrorCode{

    INVALID_TEAM_NUMBER(BAD_REQUEST, "TEAM_400_1", "잘못된 팀 번호입니다."),
    INVALID_TEAM_PLAYER_NUMBER(BAD_REQUEST, "TEAM_400_2", "잘못된 선수 번호입니다."),
    INVALID_SET_NUMBER(BAD_REQUEST, "SET_400_1", "잘못된 세트 번호입니다."),

    MATCH_NOT_FOUND(NOT_FOUND, "MATCH_404_1", "존재하지 않는 경기입니다."),
    TEAM_NOT_FOUND(NOT_FOUND, "TEAM_404_1", "존재하지 않는 팀입니다."),
    ;

    private final Integer httpStatus;
    private final String code;
    private final String message;

}
