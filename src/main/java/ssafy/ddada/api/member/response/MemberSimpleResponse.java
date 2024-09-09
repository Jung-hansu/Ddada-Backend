package ssafy.ddada.api.member.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.member.common.Gender;
import ssafy.ddada.domain.member.common.Player;

@Schema(description = "멤버 정보 요약 응답 DTO")
public record MemberSimpleResponse(
        @Schema(description = "멤버 ID")
        Long id,
        @Schema(description = "멤버 별명")
        String nickname,
        @Schema(description = "멤버 성별")
        Gender gender
) {
    public static MemberSimpleResponse from(Player player) {
        return new MemberSimpleResponse(
                player.getId(),
                player.getNickname(),
                player.getGender()
        );
    }
}
