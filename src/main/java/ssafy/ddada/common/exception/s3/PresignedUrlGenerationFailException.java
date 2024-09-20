package ssafy.ddada.common.exception.s3;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.S3ErrorCode;

public class PresignedUrlGenerationFailException extends BaseException {
    public PresignedUrlGenerationFailException() {
        super(S3ErrorCode.PERSIGNEDURL_GENERATION_FAILED);
    }
}
