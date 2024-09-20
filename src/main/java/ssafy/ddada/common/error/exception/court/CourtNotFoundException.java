package ssafy.ddada.common.error.exception.court;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.CourtErrorCode;

public class CourtNotFoundException extends BaseException {

    public CourtNotFoundException() {
        super(CourtErrorCode.COURT_NOT_FOUND);
    }

}
