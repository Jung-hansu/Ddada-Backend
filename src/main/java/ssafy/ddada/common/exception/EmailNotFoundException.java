package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class EmailNotFoundException extends BaseException {
    public EmailNotFoundException() {
        super(PlayerErrorCode.EMAIL_NOT_FOUND);
    }
}
