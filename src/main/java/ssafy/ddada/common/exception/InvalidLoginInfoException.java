package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class InvalidLoginInfoException extends BaseException {
    public InvalidLoginInfoException() {
        super(PlayerErrorCode.LOGIN_INFO_INVALID);
    }
}
