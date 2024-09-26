package ssafy.ddada.common.exception.match;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class ManagerAlreadyBookedException extends BaseException {
    public ManagerAlreadyBookedException() {
        super(MatchErrorCode.MANAGER_ALREADY_BOOKED);
    }
}
