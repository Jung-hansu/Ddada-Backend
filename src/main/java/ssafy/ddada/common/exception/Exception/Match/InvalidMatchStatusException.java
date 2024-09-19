package ssafy.ddada.common.exception.Exception.Match;

import ssafy.ddada.common.exception.Exception.BaseException;
import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class InvalidMatchStatusException extends BaseException {
    public InvalidMatchStatusException() {
        super(MatchErrorCode.INVALID_MATCH_STATUS);
    }
}
