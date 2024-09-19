package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class TempPlayerNotFoundException extends BaseException {
    public TempPlayerNotFoundException() {
        super(PlayerErrorCode.TEMP_PLAYER_NOT_FOUND);
    }
}
