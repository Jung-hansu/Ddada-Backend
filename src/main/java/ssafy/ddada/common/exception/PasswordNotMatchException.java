package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.MemberErrorCode;

public class PasswordNotMatchException extends BaseException {
    public PasswordNotMatchException() {
        super(MemberErrorCode.PASSWORD_NOT_MATCH);
    }
}
