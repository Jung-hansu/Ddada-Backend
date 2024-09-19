package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.Exception.BaseException;
import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class TeamPlayerNotFoundException extends BaseException {
    public TeamPlayerNotFoundException() {
        super(MatchErrorCode.TEAM_PLAYER_NOT_FOUND);
    }
}
