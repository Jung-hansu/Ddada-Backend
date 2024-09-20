package ssafy.ddada.common.error.exception.manager;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.ManagerErrorCode;

public class ManagerNotFoundException extends BaseException {
    public ManagerNotFoundException() {
        super(ManagerErrorCode.MANAGER_NOT_FOUND);
    }
}
