package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.MemberErrorCode;

public class VerificationException extends BaseException {
    public VerificationException() {
        super(MemberErrorCode.VERIFICATION_FAILURE);
    }
}
