package ssafy.ddada.common.exception.player;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class VerificationException extends BaseException {
    public VerificationException() {
        super(PlayerErrorCode.VERIFICATION_FAILURE);
    }
}
