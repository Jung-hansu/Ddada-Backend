package ssafy.ddada.common.error.exception.security;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.SecurityErrorCode;

public class FilterException extends BaseException {
    public FilterException() {
        super(SecurityErrorCode.FILTER_ERROR);
    }
}
