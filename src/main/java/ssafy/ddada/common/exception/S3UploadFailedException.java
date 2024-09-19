package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.S3ErrorCode;

public class S3UploadFailedException extends BaseException{
    public S3UploadFailedException() {
        super(S3ErrorCode.IMAGE_UPLOAD_FAILED);
    }
}
