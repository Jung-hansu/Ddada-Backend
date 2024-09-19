package ssafy.ddada.common.exception.exception.court;

import ssafy.ddada.common.exception.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.CourtErrorCode;

public class InvalidFacilityException extends BaseException {
    public InvalidFacilityException() {
        super(CourtErrorCode.INVALID_REGION);
    }
}
