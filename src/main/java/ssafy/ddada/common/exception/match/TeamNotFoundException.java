package ssafy.ddada.common.exception.match;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class TeamNotFoundException extends BaseException {

    public TeamNotFoundException() {
        super(MatchErrorCode.TEAM_NOT_FOUND);
    }

}
