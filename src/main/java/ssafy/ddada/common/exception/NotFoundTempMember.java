package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.MemberErrorCode;

public class NotFoundTempMember extends BaseException {
    public NotFoundTempMember() {
        super(MemberErrorCode.NOT_FOUND_TEMP_MEMBER);
    }
}
