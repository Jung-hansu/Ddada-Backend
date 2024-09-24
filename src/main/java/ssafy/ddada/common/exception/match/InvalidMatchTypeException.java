package ssafy.ddada.common.exception.match;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class InvalidMatchTypeException extends BaseException {
    public InvalidMatchTypeException() {
        super(MatchErrorCode.INVALID_MATCH_TYPE);
    }
}
