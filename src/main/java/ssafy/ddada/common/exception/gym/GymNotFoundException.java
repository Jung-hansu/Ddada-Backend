package ssafy.ddada.common.exception.gym;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.GymErrorCode;

public class GymNotFoundException extends BaseException {
    public GymNotFoundException() {
        super(GymErrorCode.GYM_NOT_FOUND);
    }
}
