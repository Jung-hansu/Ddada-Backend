package ssafy.ddada.common.error.exception.s3;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.S3ErrorCode;

public class S3UploadFailedException extends BaseException {
    public S3UploadFailedException() {
        super(S3ErrorCode.S3_IMAGE_UPLOAD_FAILED);
    }
}
