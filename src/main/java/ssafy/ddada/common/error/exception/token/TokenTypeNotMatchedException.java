package ssafy.ddada.common.error.exception.token;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.TokenErrorCode;

public class TokenTypeNotMatchedException extends BaseException {
    public TokenTypeNotMatchedException() {
        super(TokenErrorCode.NOT_MATCHED_TOKEN_TYPE);
    }
}
