package ssafy.ddada.common.exception;

import ssafy.ddada.common.exception.errorcode.SecurityErrorCode;

public class FilterErrorException extends BaseException {
    public FilterErrorException() {
        super(SecurityErrorCode.FILTER_ERROR);
    }
}
