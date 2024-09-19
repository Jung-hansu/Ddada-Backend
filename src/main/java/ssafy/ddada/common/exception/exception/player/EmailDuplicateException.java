package ssafy.ddada.common.exception.exception.player;

import ssafy.ddada.common.exception.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class EmailDuplicateException extends BaseException {
    public EmailDuplicateException() {
        super(PlayerErrorCode.EMAIL_DUPLICATE);
    }
}
