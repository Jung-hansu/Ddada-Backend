package ssafy.ddada.common.exception;

public class LoginTypeNotSupportedException extends RuntimeException {
    public LoginTypeNotSupportedException() {
        super("지원하지 않는 로그인 타입입니다.");
    }
}
