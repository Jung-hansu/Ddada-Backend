package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.MemberErrorCode;

public class NotFoundMemberException extends BaseException {
    public NotFoundMemberException() {
        super(MemberErrorCode.NOT_FOUND_MEMBER);
    }
}
