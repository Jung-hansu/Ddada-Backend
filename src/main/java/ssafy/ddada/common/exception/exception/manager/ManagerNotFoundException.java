package ssafy.ddada.common.exception.exception.manager;

import ssafy.ddada.common.exception.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.ManagerErrorCode;

public class ManagerNotFoundException extends BaseException {
    public ManagerNotFoundException() {
        super(ManagerErrorCode.MANAGER_NOT_FOUND);
    }
}
