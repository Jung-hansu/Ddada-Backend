package ssafy.ddada.common.error.errorcode;

public interface BaseErrorCode {
    String getCode();
    String getMessage();
    Integer getHttpStatus();
}
