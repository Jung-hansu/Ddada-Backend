package ssafy.ddada.common.exception.match;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class InvalidTeamNumberException extends BaseException {
    public InvalidTeamNumberException() {
        super(MatchErrorCode.INVALID_TEAM_NUMBER);
    }
}
