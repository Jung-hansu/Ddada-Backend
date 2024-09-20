package ssafy.ddada.common.error.exception.token;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.TokenErrorCode;

public class TokenSaveFailedException extends BaseException {
        public TokenSaveFailedException() {
            super(TokenErrorCode.TOKEN_SAVE_FAILED);
        }
}
