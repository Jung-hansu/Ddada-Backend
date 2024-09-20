package ssafy.ddada.common.error.exception.player;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.PlayerErrorCode;

public class EmailNotFoundException extends BaseException {
    public EmailNotFoundException() {
        super(PlayerErrorCode.EMAIL_NOT_FOUND);
    }
}
