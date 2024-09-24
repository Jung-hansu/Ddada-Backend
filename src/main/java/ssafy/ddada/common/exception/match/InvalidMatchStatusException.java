package ssafy.ddada.common.exception.match;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class InvalidMatchStatusException extends BaseException {
    public InvalidMatchStatusException() {
        super(MatchErrorCode.INVALID_MATCH_STATUS);
    }
}
