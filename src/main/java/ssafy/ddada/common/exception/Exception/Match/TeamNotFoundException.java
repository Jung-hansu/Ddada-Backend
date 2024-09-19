package ssafy.ddada.common.exception.Exception.Match;

import ssafy.ddada.common.exception.Exception.BaseException;
import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class TeamNotFoundException extends BaseException {

    public TeamNotFoundException() {
        super(MatchErrorCode.TEAM_NOT_FOUND);
    }

}
