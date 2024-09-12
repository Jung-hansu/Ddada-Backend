package ssafy.ddada.api.member.player.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.member.common.Gender;
import ssafy.ddada.domain.member.player.entity.Player;

@Schema(description = "멤버 정보 요약 응답 DTO")
public record PlayerSimpleResponse(
        @Schema(description = "멤버 ID")
        Long id,
        @Schema(description = "멤버 별명")
        String nickname,
        @Schema(description = "멤버 성별")
        Gender gender,
        @Schema(description = "멤버 레이팅")
        Integer rating
) {
    public static PlayerSimpleResponse from(Player player) {
        return new PlayerSimpleResponse(
                player.getId(),
                player.getNickname(),
                player.getGender(),
                player.getRating()
        );
    }
}
