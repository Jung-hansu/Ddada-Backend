package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.CourtErrorCode;

public class InvalidFacilityException extends BaseException{
    public InvalidFacilityException() {
        super(CourtErrorCode.INVALID_FACILITY);
    }
}
