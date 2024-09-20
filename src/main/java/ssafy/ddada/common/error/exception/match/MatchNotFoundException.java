package ssafy.ddada.common.error.exception.match;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.MatchErrorCode;

public class MatchNotFoundException extends BaseException {
    public MatchNotFoundException() {
        super(MatchErrorCode.MATCH_NOT_FOUND);
    }
}
