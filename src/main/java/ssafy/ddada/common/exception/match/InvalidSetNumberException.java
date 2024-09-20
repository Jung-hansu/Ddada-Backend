package ssafy.ddada.common.exception.match;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class InvalidSetNumberException extends BaseException {
    public InvalidSetNumberException() {
        super(MatchErrorCode.INVALID_SET_NUMBER);
    }
}
