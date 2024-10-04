package ssafy.ddada.common.exception.manager;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.ManagerErrorCode;

public class ManagerAlreadyBookedException extends BaseException {
    public ManagerAlreadyBookedException() {
        super(ManagerErrorCode.MANAGER_ALREADY_BOOKED);
    }
}
