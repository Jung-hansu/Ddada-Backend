package ssafy.ddada.common.error.exception.player;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.PlayerErrorCode;

public class MemberNotFoundException extends BaseException {
    public MemberNotFoundException() {
        super(PlayerErrorCode.PLAYER_NOT_FOUND);
    }
}
