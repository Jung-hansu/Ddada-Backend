package ssafy.ddada.common.error.exception.common;

public class PublicKeyGenerationException extends RuntimeException {
    public PublicKeyGenerationException() {
        super("Public Key Generation Failed");
    }
}
