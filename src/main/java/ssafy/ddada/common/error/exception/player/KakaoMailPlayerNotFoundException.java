package ssafy.ddada.common.error.exception.player;

import ssafy.ddada.common.error.exception.BaseException;
import ssafy.ddada.common.error.errorcode.PlayerErrorCode;

public class KakaoMailPlayerNotFoundException extends BaseException {
    public KakaoMailPlayerNotFoundException() {
        super(PlayerErrorCode.KAKAO_MAIL_PLAYER_NOT_FOUND);
    }
}
