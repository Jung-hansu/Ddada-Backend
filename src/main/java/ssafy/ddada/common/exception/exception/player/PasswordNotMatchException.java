package ssafy.ddada.common.exception.exception.player;

import ssafy.ddada.common.exception.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class PasswordNotMatchException extends BaseException {
    public PasswordNotMatchException() {
        super(PlayerErrorCode.PASSWORD_NOT_MATCH);
    }
}
