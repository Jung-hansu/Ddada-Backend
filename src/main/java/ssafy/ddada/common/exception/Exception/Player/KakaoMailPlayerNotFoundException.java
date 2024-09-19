package ssafy.ddada.common.exception.Exception.Player;

import ssafy.ddada.common.exception.Exception.BaseException;
import ssafy.ddada.common.exception.errorcode.PlayerErrorCode;

public class KakaoMailPlayerNotFoundException extends BaseException {
    public KakaoMailPlayerNotFoundException() {
        super(PlayerErrorCode.KAKAO_MAIL_PLAYER_NOT_FOUND);
    }
}
