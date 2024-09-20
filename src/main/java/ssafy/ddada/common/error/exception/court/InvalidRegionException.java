package ssafy.ddada.common.error.exception.court;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.CourtErrorCode;

public class InvalidRegionException extends BaseException {
    public InvalidRegionException() {
        super(CourtErrorCode.INVALID_REGION);
    }
}
