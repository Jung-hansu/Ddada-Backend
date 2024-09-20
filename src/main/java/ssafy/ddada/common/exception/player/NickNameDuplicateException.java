package ssafy.ddada.common.exception.player;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class NickNameDuplicateException extends BaseException {
    public NickNameDuplicateException() {
        super(PlayerErrorCode.NICKNAME_DUPLICATE);
    }
}
