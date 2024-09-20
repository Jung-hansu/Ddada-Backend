package ssafy.ddada.common.error.exception.match;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.MatchErrorCode;

public class InvalidSetNumberException extends BaseException {
    public InvalidSetNumberException() {
        super(MatchErrorCode.INVALID_SET_NUMBER);
    }
}
