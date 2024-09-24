package ssafy.ddada.common.exception.security;

import ssafy.ddada.common.exception.BaseException;
import ssafy.ddada.common.exception.errorcode.SecurityErrorCode;

public class PasswordUsedException extends BaseException {
    public PasswordUsedException() {super (SecurityErrorCode.PASSWORD_USED);}

}
