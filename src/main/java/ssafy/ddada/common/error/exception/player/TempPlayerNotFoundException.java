package ssafy.ddada.common.error.exception.player;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.PlayerErrorCode;

public class TempPlayerNotFoundException extends BaseException {
    public TempPlayerNotFoundException() {
        super(PlayerErrorCode.TEMP_PLAYER_NOT_FOUND);
    }
}
