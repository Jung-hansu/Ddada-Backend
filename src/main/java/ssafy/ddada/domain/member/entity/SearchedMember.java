package ssafy.ddada.domain.member.entity;

public record SearchedMember(
        Long id,
        String nickname,
        Gender gender
) {
    public static SearchedMember from(Member member) {
        return new SearchedMember(
                member.getId(),
                member.getNickname(),
                member.getGender()
        );
    }
}
