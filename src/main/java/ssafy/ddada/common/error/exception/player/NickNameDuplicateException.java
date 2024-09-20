package ssafy.ddada.common.error.exception.player;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.PlayerErrorCode;

public class NickNameDuplicateException extends BaseException {
    public NickNameDuplicateException() {
        super(PlayerErrorCode.NICKNAME_DUPLICATE);
    }
}
