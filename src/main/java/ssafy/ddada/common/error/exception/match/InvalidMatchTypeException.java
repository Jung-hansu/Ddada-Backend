package ssafy.ddada.common.error.exception.match;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.MatchErrorCode;

public class InvalidMatchTypeException extends BaseException {
    public InvalidMatchTypeException() {
        super(MatchErrorCode.INVALID_MATCH_TYPE);
    }
}
