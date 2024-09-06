package ssafy.ddada.common.exception.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MatchErrorCode implements BaseErrorCode{

    MATCH_NOT_FOUND(404, "MATCH_404_1", "존재하지 않는 경기입니다.");

    private final Integer httpStatus;
    private final String code;
    private final String message;

}
