package ssafy.ddada.common.error.exception.player;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.PlayerErrorCode;

public class ProfileNotFoundException extends BaseException {
    public ProfileNotFoundException() {
        super(PlayerErrorCode.PROFILE_NOT_FOUND);
    }
}