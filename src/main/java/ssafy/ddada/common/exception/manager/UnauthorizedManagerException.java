package ssafy.ddada.common.exception.manager;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.ManagerErrorCode;

public class UnauthorizedManagerException extends BaseException {
    public UnauthorizedManagerException() {
        super(ManagerErrorCode.UNAUTHORIZED_MANAGER);
    }
}
