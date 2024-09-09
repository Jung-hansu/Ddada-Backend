package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class MatchNotFoundException extends BaseException {
    public MatchNotFoundException() {
        super(MatchErrorCode.MATCH_NOT_FOUND);
    }
}
