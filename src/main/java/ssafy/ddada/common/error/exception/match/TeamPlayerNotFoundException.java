package ssafy.ddada.common.error.exception.match;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.MatchErrorCode;

public class TeamPlayerNotFoundException extends BaseException {
    public TeamPlayerNotFoundException() {
        super(MatchErrorCode.TEAM_PLAYER_NOT_FOUND);
    }
}
