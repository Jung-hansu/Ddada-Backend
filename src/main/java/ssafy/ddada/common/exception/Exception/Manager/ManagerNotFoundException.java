package ssafy.ddada.common.exception.Exception.Manager;

import ssafy.ddada.common.exception.Exception.BaseException;
import ssafy.ddada.common.exception.errorcode.ManagerErrorCode;

public class ManagerNotFoundException extends BaseException {
    public ManagerNotFoundException() {
        super(ManagerErrorCode.MANAGER_NOT_FOUND);
    }
}
