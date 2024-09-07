package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class TeamNotFoundException extends BaseException{

    public TeamNotFoundException() {
        super(MatchErrorCode.TEAM_NOT_FOUND);
    }

}
