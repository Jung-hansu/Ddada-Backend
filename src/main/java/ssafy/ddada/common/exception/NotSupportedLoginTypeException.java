package ssafy.ddada.common.exception;

public class NotSupportedLoginTypeException extends RuntimeException {
    public NotSupportedLoginTypeException() {
        super("지원하지 않는 로그인 타입입니다.");
    }
}
