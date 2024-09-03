package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.MemberErrorCode;

public class DuplicateMemberException extends BaseException{
    public DuplicateMemberException() {
        super(MemberErrorCode.DUPLICATE_MEMBER);
    }
}
