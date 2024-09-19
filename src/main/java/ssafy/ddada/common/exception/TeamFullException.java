package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.Exception.BaseException;
import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class TeamFullException extends BaseException {
    public TeamFullException() {
        super(MatchErrorCode.TEAM_FULL);
    }
}
