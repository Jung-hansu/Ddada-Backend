package ssafy.ddada.common.exception.Exception.Match;

import ssafy.ddada.common.exception.Exception.BaseException;
import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class InvalidTeamNumberException extends BaseException {
    public InvalidTeamNumberException() {
        super(MatchErrorCode.INVALID_TEAM_NUMBER);
    }
}
