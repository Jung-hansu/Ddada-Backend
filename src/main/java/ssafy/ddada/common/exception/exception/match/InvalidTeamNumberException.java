package ssafy.ddada.common.exception.exception.match;

import ssafy.ddada.common.exception.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class InvalidTeamNumberException extends BaseException {
    public InvalidTeamNumberException() {
        super(MatchErrorCode.INVALID_TEAM_NUMBER);
    }
}
