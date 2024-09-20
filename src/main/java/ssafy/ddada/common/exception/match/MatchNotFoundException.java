package ssafy.ddada.common.exception.match;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class MatchNotFoundException extends BaseException {
    public MatchNotFoundException() {
        super(MatchErrorCode.MATCH_NOT_FOUND);
    }
}
