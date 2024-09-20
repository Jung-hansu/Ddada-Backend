package ssafy.ddada.common.exception.player;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class EmailDuplicateException extends BaseException {
    public EmailDuplicateException() {
        super(PlayerErrorCode.EMAIL_DUPLICATE);
    }
}
