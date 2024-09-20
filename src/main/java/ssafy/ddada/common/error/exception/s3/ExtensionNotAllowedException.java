package ssafy.ddada.common.error.exception.s3;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.S3ErrorCode;

public class ExtensionNotAllowedException extends BaseException {
    public ExtensionNotAllowedException() {
        super(S3ErrorCode.EXTENSION_NOT_ALLOWED);
    }
}