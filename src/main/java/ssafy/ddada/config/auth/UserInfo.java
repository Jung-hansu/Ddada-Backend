package ssafy.ddada.config.auth;

public record UserInfo(
        String nickname,
        String email,
        String profileImage
) {
    public UserInfo(String email) {
        this(null, email, null);
    }
}
