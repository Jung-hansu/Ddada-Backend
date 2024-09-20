package ssafy.ddada.common.error.exception.match;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.MatchErrorCode;

public class TeamNotFoundException extends BaseException {

    public TeamNotFoundException() {
        super(MatchErrorCode.TEAM_NOT_FOUND);
    }

}
