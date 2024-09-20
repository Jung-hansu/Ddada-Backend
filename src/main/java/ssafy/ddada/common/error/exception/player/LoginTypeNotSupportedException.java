package ssafy.ddada.common.error.exception.player;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.PlayerErrorCode;

public class LoginTypeNotSupportedException extends BaseException {
    public LoginTypeNotSupportedException() {
        super(PlayerErrorCode.LOGIN_TYPE_NOT_SUPPORTED);
    }
}