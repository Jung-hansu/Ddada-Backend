package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.MemberErrorCode;

public class MemberDuplicateException extends BaseException{
    public MemberDuplicateException() {
        super(MemberErrorCode.MEMBER_DUPLICATE);
    }
}
