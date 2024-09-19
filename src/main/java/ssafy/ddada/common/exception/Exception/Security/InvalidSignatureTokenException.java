package ssafy.ddada.common.exception.Exception.Security;

import ssafy.ddada.common.exception.Exception.BaseException;
import ssafy.ddada.common.exception.errorcode.SecurityErrorCode;

public class InvalidSignatureTokenException extends BaseException {
    public InvalidSignatureTokenException() {
        super(SecurityErrorCode.INVALID_SIGNATURE_TOKEN);
    }
}
