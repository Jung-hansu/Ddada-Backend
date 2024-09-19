package ssafy.ddada.common.exception.Exception.Match;

import ssafy.ddada.common.exception.Exception.BaseException;
import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class InvalidTeamPlayerNumberException extends BaseException {
    public InvalidTeamPlayerNumberException() {
        super(MatchErrorCode.INVALID_TEAM_PLAYER_NUMBER);
    }
}
