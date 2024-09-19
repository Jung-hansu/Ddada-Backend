package ssafy.ddada.common.exception.Exception.Match;

import ssafy.ddada.common.exception.Exception.BaseException;
import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class InvalidSetNumberException extends BaseException {
    public InvalidSetNumberException() {
        super(MatchErrorCode.INVALID_SET_NUMBER);
    }
}
