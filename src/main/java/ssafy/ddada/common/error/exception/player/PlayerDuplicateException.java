package ssafy.ddada.common.error.exception.player;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.PlayerErrorCode;

public class PlayerDuplicateException extends BaseException {
    public PlayerDuplicateException() {
        super(PlayerErrorCode.PLAYER_DUPLICATE);
    }
}
