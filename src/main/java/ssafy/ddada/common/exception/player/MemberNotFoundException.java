package ssafy.ddada.common.exception.player;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class MemberNotFoundException extends BaseException {
    public MemberNotFoundException() {
        super(PlayerErrorCode.PLAYER_NOT_FOUND);
    }
}
