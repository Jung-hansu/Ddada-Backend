package ssafy.ddada.common.error.exception.match;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.MatchErrorCode;

public class InvalidTeamNumberException extends BaseException {
    public InvalidTeamNumberException() {
        super(MatchErrorCode.INVALID_TEAM_NUMBER);
    }
}
