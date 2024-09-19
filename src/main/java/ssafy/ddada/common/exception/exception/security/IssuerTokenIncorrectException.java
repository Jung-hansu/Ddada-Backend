package ssafy.ddada.common.exception.exception.security;

import ssafy.ddada.common.exception.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.SecurityErrorCode;

public class IssuerTokenIncorrectException extends BaseException {
    public IssuerTokenIncorrectException() {
        super(SecurityErrorCode.ISSUER_TOKEN_INCORRECT);
    }
}
