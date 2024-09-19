package ssafy.ddada.common.exception.Exception.Court;

import ssafy.ddada.common.exception.Exception.BaseException;
import ssafy.ddada.common.exception.errorcode.CourtErrorCode;

public class CourtNotFoundException extends BaseException {

    public CourtNotFoundException() {
        super(CourtErrorCode.COURT_NOT_FOUND);
    }

}
