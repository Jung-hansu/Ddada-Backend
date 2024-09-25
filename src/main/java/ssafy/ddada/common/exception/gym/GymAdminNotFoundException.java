package ssafy.ddada.common.exception.gym;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.GymErrorCode;

public class GymAdminNotFoundException extends BaseException  {
    public GymAdminNotFoundException() {
        super(GymErrorCode.GYM_ADMIN_NOT_FOUND);
    }
}
