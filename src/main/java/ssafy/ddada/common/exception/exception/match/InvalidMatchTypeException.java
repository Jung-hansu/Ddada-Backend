package ssafy.ddada.common.exception.exception.match;

import ssafy.ddada.common.exception.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class InvalidMatchTypeException extends BaseException {
    public InvalidMatchTypeException() {
        super(MatchErrorCode.INVALID_MATCH_TYPE);
    }
}
