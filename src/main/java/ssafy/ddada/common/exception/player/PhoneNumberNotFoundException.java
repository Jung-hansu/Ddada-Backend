package ssafy.ddada.common.exception.player;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class PhoneNumberNotFoundException extends BaseException {
    public PhoneNumberNotFoundException() {
        super(PlayerErrorCode.PHONE_NUMBER_NOT_FOUND);
    }
}
