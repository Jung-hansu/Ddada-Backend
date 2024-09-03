package ssafy.ddada.common.exception;

public class PublicKeyGenerationException extends RuntimeException {
    public PublicKeyGenerationException() {
        super("Public Key Generation Failed");
    }
}
