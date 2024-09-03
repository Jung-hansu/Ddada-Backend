package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.SecurityErrorCode;

public class IncorrectIssuerTokenException extends BaseException {
    public IncorrectIssuerTokenException() {
        super(SecurityErrorCode.INCORRECT_ISSUER_TOKEN);
    }
}
