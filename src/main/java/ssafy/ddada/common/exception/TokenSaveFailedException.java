package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.TokenErrorCode;

public class TokenSaveFailedException extends BaseException{
        public TokenSaveFailedException() {
            super(TokenErrorCode.TOKEN_SAVE_FAILED);
        }
}
