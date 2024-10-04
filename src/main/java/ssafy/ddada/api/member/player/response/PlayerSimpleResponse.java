package ssafy.ddada.api.member.player.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ssafy.ddada.domain.member.common.Gender;
import ssafy.ddada.domain.member.player.entity.Player;

@Schema(description = "선수 정보 요약 응답 DTO")
public record PlayerSimpleResponse(
        @Schema(description = "선수 ID")
        Long id,
        @Schema(description = "선수 별명")
        String nickname,
        @Schema(description = "선수 성별")
        Gender gender,
        @Schema(description = "선수 레이팅")
        Integer rating,
        @Schema(description = "선수 경기 수")
        Integer gameCount,
        @Schema(description = "선수 프로필 이미지 presigned url")
        String image
) {
    public static PlayerSimpleResponse from(Player player, String image) {
        if (player == null) {
            return null;
        }
        return new PlayerSimpleResponse(
                player.getId(),
                player.getNickname(),
                player.getGender(),
                player.getRating(),
                player.getGameCount(),
                image
        );
    }
}
