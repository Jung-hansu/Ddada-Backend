package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class MemberDuplicateException extends BaseException{
    public MemberDuplicateException() {
        super(PlayerErrorCode.MEMBER_DUPLICATE);
    }
}
