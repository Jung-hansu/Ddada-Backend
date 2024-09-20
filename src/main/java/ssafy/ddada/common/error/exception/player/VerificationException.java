package ssafy.ddada.common.error.exception.player;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.PlayerErrorCode;

public class VerificationException extends BaseException {
    public VerificationException() {
        super(PlayerErrorCode.VERIFICATION_FAILURE);
    }
}
