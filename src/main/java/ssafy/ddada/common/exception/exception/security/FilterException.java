package ssafy.ddada.common.exception.exception.security;

import ssafy.ddada.common.exception.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.SecurityErrorCode;

public class FilterException extends BaseException {
    public FilterException() {
        super(SecurityErrorCode.FILTER_ERROR);
    }
}
