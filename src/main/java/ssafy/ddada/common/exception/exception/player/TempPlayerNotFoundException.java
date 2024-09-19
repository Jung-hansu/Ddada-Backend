package ssafy.ddada.common.exception.exception.player;

import ssafy.ddada.common.exception.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class TempPlayerNotFoundException extends BaseException {
    public TempPlayerNotFoundException() {
        super(PlayerErrorCode.TEMP_PLAYER_NOT_FOUND);
    }
}
