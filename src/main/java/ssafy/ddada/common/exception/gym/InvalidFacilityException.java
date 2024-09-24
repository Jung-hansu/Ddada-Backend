package ssafy.ddada.common.exception.gym;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.GymErrorCode;

public class InvalidFacilityException extends BaseException {
    public InvalidFacilityException() {
        super(GymErrorCode.INVALID_REGION);
    }
}
