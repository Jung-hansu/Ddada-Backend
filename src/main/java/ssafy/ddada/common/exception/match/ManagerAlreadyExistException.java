package ssafy.ddada.common.exception.match;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class ManagerAlreadyExistException extends BaseException {
    public ManagerAlreadyExistException() {
        super(MatchErrorCode.MANAGER_ALREADY_EXIST);
    }
}
