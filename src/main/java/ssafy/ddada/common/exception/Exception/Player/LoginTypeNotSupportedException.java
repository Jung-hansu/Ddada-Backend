package ssafy.ddada.common.exception.Exception.Player;

import ssafy.ddada.common.exception.Exception.BaseException;
import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class LoginTypeNotSupportedException extends BaseException {
    public LoginTypeNotSupportedException() {
        super(PlayerErrorCode.LOGIN_TYPE_NOT_SUPPORTED);
    }
}