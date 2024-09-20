package ssafy.ddada.common.exception.match;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class TeamFullException extends BaseException {
    public TeamFullException() {
        super(MatchErrorCode.TEAM_FULL);
    }
}
