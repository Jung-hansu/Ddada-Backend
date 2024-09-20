package ssafy.ddada.common.error.exception.match;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.MatchErrorCode;

public class InvalidTeamPlayerNumberException extends BaseException {
    public InvalidTeamPlayerNumberException() {
        super(MatchErrorCode.INVALID_TEAM_NUMBER);
    }
}
