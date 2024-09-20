package ssafy.ddada.common.exception.s3;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.S3ErrorCode;

public class ImageTooLargeException extends BaseException {
    public ImageTooLargeException() {
        super(S3ErrorCode.IMAGE_TOO_LARGE);
    }
}
