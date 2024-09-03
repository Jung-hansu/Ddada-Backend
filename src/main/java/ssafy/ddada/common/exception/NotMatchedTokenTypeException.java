package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.TokenErrorCode;

public class NotMatchedTokenTypeException extends BaseException {
    public NotMatchedTokenTypeException() {
        super(TokenErrorCode.NOT_MATCHED_TOKEN_TYPE);
    }
}
