package ssafy.ddada.common.error.exception.match;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.MatchErrorCode;

public class InvalidRankTypeException extends BaseException {
    public InvalidRankTypeException() {
        super(MatchErrorCode.INVALID_RANK_TYPE);
    }
}
