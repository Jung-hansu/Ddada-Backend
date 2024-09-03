package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.MemberErrorCode;

public class ImageTooLargeException extends BaseException{
    public ImageTooLargeException() {
        super(MemberErrorCode.IMAGE_TOO_LARGE);
    }
}
