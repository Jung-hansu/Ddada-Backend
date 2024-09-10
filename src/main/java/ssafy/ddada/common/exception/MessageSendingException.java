package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class MessageSendingException extends BaseException {
    public MessageSendingException() {
        super(PlayerErrorCode.MESSAGE_SENDING);
    }
}
