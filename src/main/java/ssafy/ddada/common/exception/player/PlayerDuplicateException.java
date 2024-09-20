package ssafy.ddada.common.exception.player;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class PlayerDuplicateException extends BaseException {
    public PlayerDuplicateException() {
        super(PlayerErrorCode.PLAYER_DUPLICATE);
    }
}
