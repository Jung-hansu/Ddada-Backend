package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.MemberErrorCode;

public class TooLargeImageException extends BaseException{
    public TooLargeImageException() {
        super(MemberErrorCode.IMAGE_TOO_LARGE);
    }
}
