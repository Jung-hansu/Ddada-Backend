package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.CourtErrorCode;

public class CourtNotFoundException extends BaseException{

    public CourtNotFoundException() {
        super(CourtErrorCode.COURT_NOT_FOUND);
    }

}
