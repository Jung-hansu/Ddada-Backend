package ssafy.ddada.common.exception.exception.match;

import ssafy.ddada.common.exception.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class InvalidTeamPlayerNumberException extends BaseException {
    public InvalidTeamPlayerNumberException() {
        super(MatchErrorCode.INVALID_TEAM_NUMBER);
    }
}
