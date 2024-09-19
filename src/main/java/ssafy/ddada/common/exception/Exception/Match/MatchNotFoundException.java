package ssafy.ddada.common.exception.Exception.Match;

import ssafy.ddada.common.exception.Exception.BaseException;
import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class MatchNotFoundException extends BaseException {
    public MatchNotFoundException() {
        super(MatchErrorCode.MATCH_NOT_FOUND);
    }
}
