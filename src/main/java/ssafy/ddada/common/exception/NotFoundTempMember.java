package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.MemberErrorCode;

public class NotFoundTempMember extends BaseException {
    public NotFoundTempMember() {
        super(MemberErrorCode.TEMP_MEMBER_NOT_FOUND);
    }
}
