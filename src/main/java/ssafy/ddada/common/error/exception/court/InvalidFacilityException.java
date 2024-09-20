package ssafy.ddada.common.error.exception.court;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.CourtErrorCode;

public class InvalidFacilityException extends BaseException {
    public InvalidFacilityException() {
        super(CourtErrorCode.INVALID_REGION);
    }
}
