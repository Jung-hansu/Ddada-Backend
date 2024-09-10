package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class NotAllowedExtensionException extends BaseException {
    public NotAllowedExtensionException() {
        super(PlayerErrorCode.EXTENSION_NOT_ALLOWED);
    }
}