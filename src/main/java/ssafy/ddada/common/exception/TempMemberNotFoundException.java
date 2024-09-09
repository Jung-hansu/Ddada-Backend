package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.MemberErrorCode;

public class TempMemberNotFoundException extends BaseException {
    public TempMemberNotFoundException() {
        super(MemberErrorCode.TEMP_MEMBER_NOT_FOUND);
    }
}
