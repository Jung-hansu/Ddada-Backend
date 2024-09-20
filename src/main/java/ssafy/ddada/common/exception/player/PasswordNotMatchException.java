package ssafy.ddada.common.exception.player;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class PasswordNotMatchException extends BaseException {
    public PasswordNotMatchException() {
        super(PlayerErrorCode.PASSWORD_NOT_MATCH);
    }
}
