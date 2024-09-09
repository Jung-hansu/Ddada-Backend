package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.MemberErrorCode;

public class MessageSendingException extends BaseException {
    public MessageSendingException() {
        super(MemberErrorCode.MESSAGE_SENDING);
    }
}
