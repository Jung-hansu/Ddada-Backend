package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.CourtErrorCode;

public class InvalidRegionException extends BaseException{
    public InvalidRegionException() {
        super(CourtErrorCode.INVALID_REGION);
    }
}
