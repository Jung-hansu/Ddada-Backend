package ssafy.ddada.common.exception.gym;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.GymErrorCode;

public class CourtNotFoundException extends BaseException {

    public CourtNotFoundException() {
        super(GymErrorCode.COURT_NOT_FOUND);
    }

}
