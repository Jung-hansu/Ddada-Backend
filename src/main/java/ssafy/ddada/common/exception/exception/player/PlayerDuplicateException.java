package ssafy.ddada.common.exception.exception.player;

import ssafy.ddada.common.exception.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class PlayerDuplicateException extends BaseException {
    public PlayerDuplicateException() {
        super(PlayerErrorCode.PLAYER_DUPLICATE);
    }
}
