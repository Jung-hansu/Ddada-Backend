package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.SecurityErrorCode;

public class IssuerTokenIncorrectException extends BaseException {
    public IssuerTokenIncorrectException() {
        super(SecurityErrorCode.INCORRECT_ISSUER_TOKEN);
    }
}
