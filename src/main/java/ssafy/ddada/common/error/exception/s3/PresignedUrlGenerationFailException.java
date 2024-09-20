package ssafy.ddada.common.error.exception.s3;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.S3ErrorCode;

public class PresignedUrlGenerationFailException extends BaseException {
    public PresignedUrlGenerationFailException() {
        super(S3ErrorCode.PERSIGNEDURL_GENERATION_FAILED);
    }
}
