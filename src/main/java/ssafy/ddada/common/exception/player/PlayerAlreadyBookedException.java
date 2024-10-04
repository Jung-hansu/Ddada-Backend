package ssafy.ddada.common.exception.player;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class PlayerAlreadyBookedException extends BaseException {
    public PlayerAlreadyBookedException() {
        super(PlayerErrorCode.PLAYER_ALREADY_BOOKED);
    }
}
