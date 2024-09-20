package ssafy.ddada.common.exception.token;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.TokenErrorCode;

public class TokenTypeNotMatchedException extends BaseException {
    public TokenTypeNotMatchedException() {
        super(TokenErrorCode.NOT_MATCHED_TOKEN_TYPE);
    }
}
