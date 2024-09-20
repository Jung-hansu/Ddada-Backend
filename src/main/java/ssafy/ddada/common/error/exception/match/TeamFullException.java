package ssafy.ddada.common.error.exception.match;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.MatchErrorCode;

public class TeamFullException extends BaseException {
    public TeamFullException() {
        super(MatchErrorCode.TEAM_FULL);
    }
}
