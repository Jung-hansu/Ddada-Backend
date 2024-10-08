package ssafy.ddada.api.member.player.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "선수 랭킹 응답 DTO")
public record PlayerRankingResponse(
        @Schema(description = "선수 랭킹", example = "1")
        Integer ranking,
        @Schema(description = "선수 닉네임", example = "JohnDoe")
        String nickname,
        @Schema(description = "선수 점수", example = "1500")
        Integer rating
) {
    public static PlayerRankingResponse of(Integer ranking, String nickname, Integer rating) {
        return new PlayerRankingResponse(ranking, nickname, rating);
    }
}
