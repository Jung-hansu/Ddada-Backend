package ssafy.ddada.common.error.exception.security;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.SecurityErrorCode;

public class InvalidSignatureTokenException extends BaseException {
    public InvalidSignatureTokenException() {
        super(SecurityErrorCode.INVALID_SIGNATURE_TOKEN);
    }
}
