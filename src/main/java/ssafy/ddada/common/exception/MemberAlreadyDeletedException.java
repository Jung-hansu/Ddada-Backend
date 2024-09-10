package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class MemberAlreadyDeletedException extends BaseException {
    public MemberAlreadyDeletedException() {
        super(PlayerErrorCode.MEMBER_ALREADY_DELETED);
    }
}
