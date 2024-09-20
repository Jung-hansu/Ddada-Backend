package ssafy.ddada.common.exception.token;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.TokenErrorCode;

public class TokenSaveFailedException extends BaseException {
        public TokenSaveFailedException() {
            super(TokenErrorCode.TOKEN_SAVE_FAILED);
        }
}
