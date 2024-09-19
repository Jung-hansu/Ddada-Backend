package ssafy.ddada.common.exception.Exception.S3;

import ssafy.ddada.common.exception.Exception.BaseException;
import ssafy.ddada.common.exception.errorcode.S3ErrorCode;

public class S3UploadFailedException extends BaseException {
    public S3UploadFailedException() {
        super(S3ErrorCode.S3_IMAGE_UPLOAD_FAILED);
    }
}
