package ssafy.ddada.common.exception.player;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class EmailNotFoundException extends BaseException {
    public EmailNotFoundException() {
        super(PlayerErrorCode.EMAIL_NOT_FOUND);
    }
}
