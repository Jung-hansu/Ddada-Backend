package ssafy.ddada.common.exception.exception.player;

import ssafy.ddada.common.exception.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class VerificationException extends BaseException {
    public VerificationException() {
        super(PlayerErrorCode.VERIFICATION_FAILURE);
    }
}
