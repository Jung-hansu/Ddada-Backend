package ssafy.ddada.common.exception.security;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.SecurityErrorCode;

public class InvalidSignatureTokenException extends BaseException {
    public InvalidSignatureTokenException() {
        super(SecurityErrorCode.INVALID_SIGNATURE_TOKEN);
    }
}
