package ssafy.ddada.common.exception.exception.match;

import ssafy.ddada.common.exception.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class InvalidRankTypeException extends BaseException {
    public InvalidRankTypeException() {
        super(MatchErrorCode.INVALID_RANK_TYPE);
    }
}
