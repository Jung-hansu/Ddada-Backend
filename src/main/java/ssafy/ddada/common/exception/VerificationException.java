package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class VerificationException extends BaseException {
    public VerificationException() {
        super(PlayerErrorCode.VERIFICATION_FAILURE);
    }
}
