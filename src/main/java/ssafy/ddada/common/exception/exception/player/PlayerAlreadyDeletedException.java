package ssafy.ddada.common.exception.exception.player;

import ssafy.ddada.common.exception.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class PlayerAlreadyDeletedException extends BaseException {
    public PlayerAlreadyDeletedException() {
        super(PlayerErrorCode.PLAYER_ALREADY_DELETED);
    }
}
