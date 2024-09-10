package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class TooLargeImageException extends BaseException{
    public TooLargeImageException() {
        super(PlayerErrorCode.IMAGE_TOO_LARGE);
    }
}
