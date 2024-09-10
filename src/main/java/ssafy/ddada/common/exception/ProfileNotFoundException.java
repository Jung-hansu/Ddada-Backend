package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class ProfileNotFoundException extends BaseException {
    public ProfileNotFoundException() {
        super(PlayerErrorCode.PROFILE_NOT_FOUND);
    }
}