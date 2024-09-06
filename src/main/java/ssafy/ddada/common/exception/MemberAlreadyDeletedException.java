package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.MemberErrorCode;

public class MemberAlreadyDeletedException extends BaseException {
    public MemberAlreadyDeletedException() {
        super(MemberErrorCode.MEMBER_ALREADY_DELETED);
    }
}
