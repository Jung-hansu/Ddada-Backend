package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class TempMemberNotFoundException extends BaseException {
    public TempMemberNotFoundException() {
        super(PlayerErrorCode.TEMP_MEMBER_NOT_FOUND);
    }
}
