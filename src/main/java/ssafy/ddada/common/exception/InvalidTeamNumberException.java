package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class InvalidTeamNumberException extends BaseException{
    public InvalidTeamNumberException() {
        super(MatchErrorCode.INVALID_TEAM_NUMBER);
    }
}
