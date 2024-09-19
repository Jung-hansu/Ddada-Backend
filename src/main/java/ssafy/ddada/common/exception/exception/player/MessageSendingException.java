package ssafy.ddada.common.exception.exception.player;

import ssafy.ddada.common.exception.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class MessageSendingException extends BaseException {
    public MessageSendingException() {
        super(PlayerErrorCode.MESSAGE_SENDING);
    }
}
