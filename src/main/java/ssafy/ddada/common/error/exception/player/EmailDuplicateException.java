package ssafy.ddada.common.error.exception.player;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.PlayerErrorCode;

public class EmailDuplicateException extends BaseException {
    public EmailDuplicateException() {
        super(PlayerErrorCode.EMAIL_DUPLICATE);
    }
}
