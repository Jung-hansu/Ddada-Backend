package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.MemberErrorCode;

public class EmailNotFoundException extends BaseException {
    public EmailNotFoundException() {
        super(MemberErrorCode.EMAIL_NOT_FOUND);
    }
}
