package ssafy.ddada.common.error.exception.player;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.PlayerErrorCode;

public class PlayerAlreadyDeletedException extends BaseException {
    public PlayerAlreadyDeletedException() {
        super(PlayerErrorCode.PLAYER_ALREADY_DELETED);
    }
}
