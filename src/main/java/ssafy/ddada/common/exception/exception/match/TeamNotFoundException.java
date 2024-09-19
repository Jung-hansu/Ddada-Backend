package ssafy.ddada.common.exception.exception.match;

import ssafy.ddada.common.exception.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class TeamNotFoundException extends BaseException {

    public TeamNotFoundException() {
        super(MatchErrorCode.TEAM_NOT_FOUND);
    }

}
