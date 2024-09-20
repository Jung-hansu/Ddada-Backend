package ssafy.ddada.common.exception.s3;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.S3ErrorCode;

public class ProfileNotFoundInS3Exception extends BaseException {
    public ProfileNotFoundInS3Exception() {
        super(S3ErrorCode.PROFILE_NOT_FOUND_IN_S3);
    }
}
