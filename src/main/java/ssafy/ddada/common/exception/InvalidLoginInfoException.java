package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.MemberErrorCode;

public class InvalidLoginInfoException extends BaseException {
    public InvalidLoginInfoException() {
        super(MemberErrorCode.LOGIN_INFO_INVALID);
    }
}
