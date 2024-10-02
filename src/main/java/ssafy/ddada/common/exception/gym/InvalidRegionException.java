package ssafy.ddada.common.exception.gym;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.GymErrorCode;

public class InvalidRegionException extends BaseException {
    public InvalidRegionException() {
        super(GymErrorCode.INVALID_REGION);
    }
}
