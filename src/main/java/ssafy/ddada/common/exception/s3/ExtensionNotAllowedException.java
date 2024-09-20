package ssafy.ddada.common.exception.s3;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.S3ErrorCode;

public class ExtensionNotAllowedException extends BaseException {
    public ExtensionNotAllowedException() {
        super(S3ErrorCode.EXTENSION_NOT_ALLOWED);
    }
}