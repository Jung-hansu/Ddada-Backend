package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.MemberErrorCode;

public class ProfileNotFoundInS3Exception extends BaseException {
    public ProfileNotFoundInS3Exception() {
        super(MemberErrorCode.PROFILE_NOT_FOUND_IN_S3);
    }
}
