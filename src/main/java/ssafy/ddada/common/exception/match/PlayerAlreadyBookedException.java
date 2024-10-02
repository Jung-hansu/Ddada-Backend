package ssafy.ddada.common.exception.match;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.MatchErrorCode;

public class PlayerAlreadyBookedException extends BaseException {
    public PlayerAlreadyBookedException() {
        super(MatchErrorCode.PLAYER_ALREADY_BOOKED);
    }
}
