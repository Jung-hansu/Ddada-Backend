package ssafy.ddada.common.exception.player;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class InvalidLoginInfoException extends BaseException {
    public InvalidLoginInfoException() {
        super(PlayerErrorCode.INVALID_LOGIN_INFO);
    }
}
