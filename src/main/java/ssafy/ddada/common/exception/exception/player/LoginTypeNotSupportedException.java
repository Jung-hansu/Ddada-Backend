package ssafy.ddada.common.exception.exception.player;

import ssafy.ddada.common.exception.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class LoginTypeNotSupportedException extends BaseException {
    public LoginTypeNotSupportedException() {
        super(PlayerErrorCode.LOGIN_TYPE_NOT_SUPPORTED);
    }
}