package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.S3ErrorCode;

public class TooLargeImageException extends BaseException{
    public TooLargeImageException() {
        super(S3ErrorCode.IMAGE_TOO_LARGE);
    }
}
