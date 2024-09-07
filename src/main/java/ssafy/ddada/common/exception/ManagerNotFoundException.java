package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.ManagerErrorCode;

public class ManagerNotFoundException extends BaseException{

    public ManagerNotFoundException() {
        super(ManagerErrorCode.MANAGER_NOT_FOUND);
    }

}
