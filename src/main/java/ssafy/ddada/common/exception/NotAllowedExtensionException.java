package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.S3ErrorCode;

public class NotAllowedExtensionException extends BaseException {
    public NotAllowedExtensionException() {
        super(S3ErrorCode.EXTENSION_NOT_ALLOWED);
    }
}