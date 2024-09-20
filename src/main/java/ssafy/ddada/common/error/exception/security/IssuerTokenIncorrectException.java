package ssafy.ddada.common.error.exception.security;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.SecurityErrorCode;

public class IssuerTokenIncorrectException extends BaseException {
    public IssuerTokenIncorrectException() {
        super(SecurityErrorCode.ISSUER_TOKEN_INCORRECT);
    }
}
