package ssafy.ddada.common.error.exception.s3;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.S3ErrorCode;

public class ImageTooLargeException extends BaseException {
    public ImageTooLargeException() {
        super(S3ErrorCode.IMAGE_TOO_LARGE);
    }
}
