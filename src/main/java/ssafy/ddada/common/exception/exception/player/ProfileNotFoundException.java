package ssafy.ddada.common.exception.exception.player;

import ssafy.ddada.common.exception.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class ProfileNotFoundException extends BaseException {
    public ProfileNotFoundException() {
        super(PlayerErrorCode.PROFILE_NOT_FOUND);
    }
}