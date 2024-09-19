package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.Exception.BaseException;
import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class InvalidRankTypeException extends BaseException {
    public InvalidRankTypeException() {
        super(MatchErrorCode.INVALID_RANK_TYPE);
    }
}
