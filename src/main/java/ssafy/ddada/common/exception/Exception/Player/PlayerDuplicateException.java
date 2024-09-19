package ssafy.ddada.common.exception.Exception.Player;

import ssafy.ddada.common.exception.Exception.BaseException;
import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class PlayerDuplicateException extends BaseException {
    public PlayerDuplicateException() {
        super(PlayerErrorCode.PLAYER_DUPLICATE);
    }
}
