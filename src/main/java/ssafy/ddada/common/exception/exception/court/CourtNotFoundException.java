package ssafy.ddada.common.exception.exception.court;

import ssafy.ddada.common.exception.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.CourtErrorCode;

public class CourtNotFoundException extends BaseException {

    public CourtNotFoundException() {
        super(CourtErrorCode.COURT_NOT_FOUND);
    }

}
