package ssafy.ddada.common.error.exception.s3;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.S3ErrorCode;

public class ProfileNotFoundInS3Exception extends BaseException {
    public ProfileNotFoundInS3Exception() {
        super(S3ErrorCode.PROFILE_NOT_FOUND_IN_S3);
    }
}
