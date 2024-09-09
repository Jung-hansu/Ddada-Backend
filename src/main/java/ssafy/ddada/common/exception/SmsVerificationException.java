package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.MemberErrorCode;

public class SmsVerificationException extends BaseException {
    public SmsVerificationException() {
        super(MemberErrorCode.SMS_VERIFICATION_FAILURE);
    }
}
