package ssafy.ddada.common.exception.exception.match;

import ssafy.ddada.common.exception.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class TeamFullException extends BaseException {
    public TeamFullException() {
        super(MatchErrorCode.TEAM_FULL);
    }
}
