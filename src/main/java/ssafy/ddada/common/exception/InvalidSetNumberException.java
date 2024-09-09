package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class InvalidSetNumberException extends BaseException{
    public InvalidSetNumberException() {
        super(MatchErrorCode.INVALID_SET_NUMBER);
    }
}
