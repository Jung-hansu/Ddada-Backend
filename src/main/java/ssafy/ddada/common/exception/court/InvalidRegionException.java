package ssafy.ddada.common.exception.court;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.CourtErrorCode;

public class InvalidRegionException extends BaseException {
    public InvalidRegionException() {
        super(CourtErrorCode.INVALID_REGION);
    }
}
