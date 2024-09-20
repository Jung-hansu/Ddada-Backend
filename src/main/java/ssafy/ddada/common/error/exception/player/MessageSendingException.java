package ssafy.ddada.common.error.exception.player;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.PlayerErrorCode;

public class MessageSendingException extends BaseException {
    public MessageSendingException() {
        super(PlayerErrorCode.MESSAGE_SENDING);
    }
}
