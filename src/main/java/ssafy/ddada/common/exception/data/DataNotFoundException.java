package ssafy.ddada.common.exception.data;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.DataErrorCode;

public class DataNotFoundException extends BaseException {
    public DataNotFoundException() {
        super(DataErrorCode.DATA_NOT_FOUND);
    }
}
