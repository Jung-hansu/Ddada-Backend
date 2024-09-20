package ssafy.ddada.common.error.exception.match;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.MatchErrorCode;

public class InvalidMatchStatusException extends BaseException {
    public InvalidMatchStatusException() {
        super(MatchErrorCode.INVALID_MATCH_STATUS);
    }
}
