package ssafy.ddada.domain.auth.model;

public record UserInfo(
        String nickname,
        String email,
        String image
) {
    public UserInfo(String email) {
        this(null, email, null);
    }
}
