package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.MemberErrorCode;

public class NotAllowedExtensionException extends BaseException {
    public NotAllowedExtensionException() {
        super(MemberErrorCode.EXTENSION_NOT_ALLOWED);
    }
}