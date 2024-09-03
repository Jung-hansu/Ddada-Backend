package ssafy.ddada.common.exception;

public class NoAnswerGptException extends RuntimeException {
    public NoAnswerGptException() {
        super("No answer from GPT-3"); // TODO Exception 커스텀
    }
}
