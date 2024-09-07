package ssafy.ddada.domain.member.entity;

public record SearchedManager(
        Long id,
        String nickname
) {
    public static SearchedManager from(Manager manager){
        return new SearchedManager(
                manager.getId(),
                manager.getNickname()
        );
    }
}
