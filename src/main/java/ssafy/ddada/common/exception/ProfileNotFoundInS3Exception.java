package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class ProfileNotFoundInS3Exception extends BaseException {
    public ProfileNotFoundInS3Exception() {
        super(PlayerErrorCode.PROFILE_NOT_FOUND_IN_S3);
    }
}
