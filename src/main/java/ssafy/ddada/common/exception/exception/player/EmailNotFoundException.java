package ssafy.ddada.common.exception.exception.player;

import ssafy.ddada.common.exception.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class EmailNotFoundException extends BaseException {
    public EmailNotFoundException() {
        super(PlayerErrorCode.EMAIL_NOT_FOUND);
    }
}
