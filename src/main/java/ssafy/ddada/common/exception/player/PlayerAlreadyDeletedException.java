package ssafy.ddada.common.exception.player;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class PlayerAlreadyDeletedException extends BaseException {
    public PlayerAlreadyDeletedException() {
        super(PlayerErrorCode.PLAYER_ALREADY_DELETED);
    }
}
