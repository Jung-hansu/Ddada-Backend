package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class InvalidTeamPlayerNumberException extends BaseException {
    public InvalidTeamPlayerNumberException() {
        super(MatchErrorCode.INVALID_TEAM_PLAYER_NUMBER);
    }
}
